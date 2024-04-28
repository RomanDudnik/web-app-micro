package org.dudnik.stock.service;

import org.dudnik.stock.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для работы с клиентом
 * для подключения к сервису товаров
 */

public interface ProductsServiceClient {

    List<Product> findAllProducts(String filter);

    Product createProduct(String name, String description);

    Optional<Product> findProduct(int productId);

    void updateProduct(int productId, String name, String description);

    void deleteProduct(int productId);
}
