package com.northwind.orderservice.service;

import com.northwind.orderservice.model.Order;
import com.northwind.orderservice.model.OrderDetail;
import com.northwind.orderservice.model.OrderDetailKey;
import com.northwind.orderservice.model.dto.OrderDTO;
import com.northwind.orderservice.repository.OrderRepository;
import com.northwind.orderservice.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@PropertySource({"classpath:application.yml","classpath:message.properties"})
public class OrderService {

    @Value("${message.order-success}")
    private String orderSuccess;
    @Value("${message.product-not-found}")
    private String productNotFound;
    @Value("${service.product-service.domain}")
    private String productDomain;
    @Value("${request.header.api-key}")
    private String apiKey;
    @Value("${service.product-service.api-key}")
    private String productServiceKey;
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private OrderRepository repository;
    private static final ParameterizedTypeReference<Map<Integer, Double>> MAP_TYPE_REF = new ParameterizedTypeReference<>() {
    };
    private final String QUERY_PATH = "/product-service/product-order";

    public List<OrderDTO> getOrders(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<OrderDTO> pagedResult = repository.findAllProjectedBy(pageable);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return null;
    }

    public Order getOrderById(String id) {
        return repository.findById(UUID.fromString(id)).orElse(null);
    }

    public String createOrder(String uid, List<OrderRequest> orderRequests) {
        //merge duplicate request
        List<OrderRequest> orders = mergeDuplicateProducts(orderRequests);

        //get products form product-service
        Map<Integer, Integer> body = orders
                .stream()
                .collect(Collectors.toMap(OrderRequest::getProductId, OrderRequest::getQuantity));
        Map<Integer, Double> products = exchangeProducts(body);
        //check products
        if (null == products || products.isEmpty()) {
            return productNotFound;
        }

        //create new order
        UUID orderId = UUID.randomUUID();

        List<OrderDetail> orderDetails = createOrderDetails(orderId, orders, products);
        Order order = new Order(orderId, uid, new Date(), orderDetails);
        repository.save(order);
        return orderSuccess;
    }

    private List<OrderDetail> createOrderDetails(UUID orderId, List<OrderRequest> orders, Map<Integer, Double> products) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        orders.forEach(o ->
                orderDetails.add(new OrderDetail(
                                new OrderDetailKey(o.getProductId(), orderId),
                                products.get(o.getProductId()) * (1 - o.getDiscount()),
                                o.getQuantity(),
                                o.getDiscount()
                        )
                ));
        return orderDetails;
    }

    private Map<Integer, Double> exchangeProducts(Map<Integer, Integer> body) {
        return webClientBuilder
                .build()
                .post()
                .uri(productDomain + QUERY_PATH)
                .header(apiKey, productServiceKey)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(MAP_TYPE_REF)
                .block();
    }

    private List<OrderRequest> mergeDuplicateProducts(List<OrderRequest> orderRequests) {
        Map<Integer, OrderRequest> map = new HashMap<>();
        int size = orderRequests.size();
        int id;
        int quantity;
        float discount;
        for (int i = 0; i < size; i++) {
            id = orderRequests.get(i).getProductId();
            quantity = orderRequests.get(i).getQuantity();
            discount = orderRequests.get(i).getDiscount();

            if (map.containsKey(id)) {
                quantity += map.get(id).getQuantity();
                discount += map.get(id).getDiscount();
                discount = discount < 1 ? discount : 1;
            }
            map.put(id, new OrderRequest(id, quantity, discount));
        }
        return new ArrayList<>(map.values());
    }

    public List<OrderDTO> getOrdersByUser(String id, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<OrderDTO> pagedResult = repository.findProjectedByUid(id,pageable);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return null;
    }
}
