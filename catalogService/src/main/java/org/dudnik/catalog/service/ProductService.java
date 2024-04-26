package org.dudnik.catalog.service;

import org.dudnik.catalog.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProducts();

    Product createProduct(String name, String description);

    Optional<Product> findProduct(int productId);

    void updateProduct(Integer id, String name, String description);

    void deleteProduct(Integer id);
}
