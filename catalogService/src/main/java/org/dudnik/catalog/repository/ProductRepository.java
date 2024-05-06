package org.dudnik.catalog.repository;

import org.dudnik.catalog.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс репозитория для работы с товарами
 * расширим до CrudRepository для работы с БД
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    // метод для поиска товаров по имени без учета регистра
    // применяем механизм интерпретатора имен методов
    Iterable<Product> findAllByNameLikeIgnoreCase(String filter);
}
