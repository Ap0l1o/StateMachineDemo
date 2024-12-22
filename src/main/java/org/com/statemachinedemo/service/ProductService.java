package org.com.statemachinedemo.service;

import org.com.statemachinedemo.entity.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    private final Map<Long, Product> productRepository = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public Product createProduct(String name, BigDecimal price, int stock) {
        Product product = new Product();
        product.setId(idGenerator.incrementAndGet());
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        productRepository.put(product.getId(), product);
        return product;
    }

    public List<Product> getAvailableProducts() {
        List<Product> availableProducts = new ArrayList<>();
        for (Product product : productRepository.values()) {
            if (product.getStock() > 0) {
                availableProducts.add(product);
            }
        }
        return availableProducts;
    }

    public Product updateProductStock(Long productId, int newStock) {
        Product product = productRepository.get(productId);
        if (product != null) {
            product.setStock(newStock);
            return product;
        }
        return null;
    }

    public void saleProduct(Long productId, int nums) {
        Product product = productRepository.get(productId);
        if (product != null) {
            product.setStock(product.getStock() - nums);
        }
    }

    public Product getProductById(Long productId) {
        return productRepository.get(productId);
    }

    public void deleteProduct(Long productId) {
        productRepository.remove(productId);
    }
}