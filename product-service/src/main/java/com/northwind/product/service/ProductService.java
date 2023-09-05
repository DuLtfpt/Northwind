package com.northwind.product.service;

import com.northwind.product.exception.BusinessException;
import com.northwind.product.exception.NotFoundException;
import com.northwind.product.model.Category;
import com.northwind.product.model.Product;
import com.northwind.product.model.dto.ProductDTO;
import com.northwind.product.repository.ProductRepository;
import com.northwind.product.request.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@PropertySource({"classpath:application.yml","classpath:message.properties"})
public class ProductService {

    @Value("${message.product-not-found}")
    private String productNotFound;
    @Value("${message.product-update-successfully}")
    private String productUpdateSuccessfully;
    @Value("${message.product-create-successfully}")
    private String productCreateSuccessfully;
    @Autowired
    ProductRepository repository;

    public List<Product> orderProducts(Integer pageNo,
                                       Integer pageSize,
                                       String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = repository.findAll(pageable);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return null;
    }

    public Product getProduct(int id) {
        return repository.findById(id).orElse(null);
    }

    public List<ProductDTO> getProductsByCategory(Category category, Integer pageNo,
                                                  Integer pageSize,
                                                  String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<ProductDTO> pagedResult = repository.findAllByCategory(category, pageable);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return null;
    }

    @Transactional(rollbackFor = {BusinessException.class, Exception.class, Throwable.class})
    public Map<Integer, Double> orderProducts(Map<Integer, Integer> order) {
        List<Integer> ids = new ArrayList<>(order.keySet());
        List<Product> products = repository
                .findAllById(ids)
                .stream()
                .filter(Product::isActive)
                .toList();

        int pSize = products.size();
        int orderQuantity;
        int stock;
        int productId;
        boolean flag = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pSize; i++) {
            stock = products.get(i).getStock();
            productId = products.get(i).getId();
            orderQuantity = order.get(productId);
            if ((stock - orderQuantity < 0) || flag) {
                sb.append("Product: ")
                        .append(productId)
                        .append(", available stock: ")
                        .append(stock)
                        .append(System.getProperty("line.separator"));
                flag = true;
                continue;
            }

            products.get(i).setStock(stock - orderQuantity);
        }
        if (flag) {
            throw new BusinessException(sb.toString());
        }
        repository.saveAll(products);
        return products
                .stream()
                .collect(Collectors
                        .toMap(Product::getId, Product::getProductPrice));
    }

    public String saveProduct(ProductRequest request) {
        repository.save(
                new Product(
                        null,
                        request.getProductName(),
                        request.getProductDetails(),
                        request.getProductPrice(),
                        request.getStock(),
                        request.isActive(),
                        new Category(
                                request.getCategoryId(),
                                null
                        )
                )
        );
        return productCreateSuccessfully;
    }

    public String updateProduct(int id, ProductRequest request) {
        if (null == getProduct(id)) {
            throw new NotFoundException(productNotFound);
        }
        repository.save(
                new Product(
                        id,
                        request.getProductName(),
                        request.getProductDetails(),
                        request.getProductPrice(),
                        request.getStock(),
                        request.isActive(),
                        new Category(
                                request.getCategoryId(),
                                null
                        )
                )
        );
        return productUpdateSuccessfully;
    }

    public String setActiveProduct(int id) {
        Product product = getProduct(id);
        if (null == product) {
            throw new NotFoundException(productNotFound);
        }
        product.setActive(false);
        repository.save(product);
        return productUpdateSuccessfully;
    }
}
