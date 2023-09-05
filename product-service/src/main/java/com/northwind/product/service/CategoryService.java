package com.northwind.product.service;

import com.northwind.product.exception.BusinessException;
import com.northwind.product.exception.NotFoundException;
import com.northwind.product.model.Category;
import com.northwind.product.model.dto.ProductDTO;
import com.northwind.product.repository.CategoryRepository;
import com.northwind.product.request.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@PropertySource({"classpath:application.yml","classpath:message.properties"})
public class CategoryService {

    @Value("${message.category-not-found}")
    private String categoryNotFound;
    @Value("${message.category-delete-error}")
    private String categoryDeleteError;
    @Value("${message.product-update-successfully}")
    private String updateSuccessfully;
    @Value("${message.category-create-successfully}")
    private String categoryCreateSuccessfully;
    @Value("${message.category-delete-successfully}")
    private String categoryDeleteSuccessfully;

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private ProductService productService;

    public List<Category> getCategories() {
        return repository.findAll();
    }

    public Category getCategory(int id) {
        return repository.findById(id).orElse(null);
    }

    public String saveCategory(CategoryRequest body) {
        repository.save(
                new Category(
                        null,
                        body.getCategoryName()
                )
        );
        return categoryCreateSuccessfully;
    }

    public String updateCategory(int id, CategoryRequest body) {
        if(getCategory(id)==null){
            throw new NotFoundException(categoryNotFound);
        }
        repository.save(new Category(
                        id,
                        body.getCategoryName()
                )
        );
        return updateSuccessfully;
    }

    public String deleteCategory(int id) {
        Category category = repository.findById(id).orElse(null);
        if (null == category) {
            throw new NotFoundException(categoryNotFound);
        }

        List<ProductDTO> products = productService.getProductsByCategory(category, 0, 1, "id");

        if (null==products) {
            repository.delete(category);
            return categoryDeleteSuccessfully;
        } else {
            throw new BusinessException(categoryDeleteError + " "+ category.getCategoryName());
        }
    }

    public Map<?,?> getProductsByCategory(int id,
                                          Integer pageNo,
                                          Integer pageSize,
                                          String sortBy) {
        Category category = getCategory(id);
        if(null==category){
            return null;
        }
        Map<String,Object> result = new HashMap<>();
        result.put("category",category);
        List<ProductDTO> products = productService.getProductsByCategory(category,pageNo,pageSize,sortBy);
        result.put("products",products);
        return result;
    }
}
