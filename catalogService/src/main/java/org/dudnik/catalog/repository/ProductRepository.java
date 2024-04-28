package org.dudnik.catalog.repository;

import org.dudnik.catalog.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для работы с товарами
 * расширим до CrudRepository для работы с БД
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    // для работы с InMemoryProductRepository
//    List<Product> findAll();
//
//    Product save(Product product);
//
//    Optional<Product> findById(Integer productId);
//
//    void deleteById(Integer id);
    // метод для поиска товаров по имени без учета регистра
    // применяем механизм интерпретатора имен методов
    Iterable<Product> findAllByNameLikeIgnoreCase(String filter);
}
