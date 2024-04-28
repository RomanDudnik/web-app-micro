package org.dudnik.catalog.service;

import org.dudnik.catalog.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    // для InMemoryProductRepository
//    List<Product> findAllProducts();

    // для JPA ProductRepository методы работают с Iterable объектами
    Iterable<Product> findAllProducts(String filter);

    Product createProduct(String name, String description);

    Optional<Product> findProduct(int productId);

    void updateProduct(Integer id, String name, String description);

    void deleteProduct(Integer id);
}
