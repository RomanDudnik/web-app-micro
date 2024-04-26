package org.dudnik.catalog.repository;

import org.dudnik.catalog.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для работы с товарами
 */
public interface ProductRepository {
    List<Product> findAll();

    Product save(Product product);

    Optional<Product> findById(Integer productId);

    void deleteById(Integer id);
}
