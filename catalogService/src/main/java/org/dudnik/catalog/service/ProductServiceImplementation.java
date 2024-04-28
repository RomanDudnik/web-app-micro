package org.dudnik.catalog.service;

import lombok.RequiredArgsConstructor;
import org.dudnik.catalog.model.Product;
import org.dudnik.catalog.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor            // конструктор с параметрами для внедрения зависимостей(зависимость от repository)
public class ProductServiceImplementation implements ProductService {

    // внедрение зависимости - репозитория
    private final ProductRepository productRepository;

    // реализация метода для получения списка товаров
    @Override
    // для InMemoryProductRepository
//    public List<Product> findAllProducts() {
//        return productRepository.findAll();
//    }
    // для JPA ProductRepository
    public Iterable<Product> findAllProducts(String filter) {
        // добавляем проверку для фильтрации по названию
        if (filter != null && !filter.isBlank()) {
            return this.productRepository.findAllByNameLikeIgnoreCase("%" + filter +"%");
        } else {
            return productRepository.findAll();
        }
    }

    // реализация метода для создания нового товара
    @Override
    @Transactional
    public Product createProduct(String name, String description) {
       return this.productRepository.save(new Product(null, name, description));
    }

    // реализация метода для получения конкретного товара
    @Override
    public Optional<Product> findProduct(int productId) {
        return this.productRepository.findById(productId);
    }

    // реализация метода для обновления конкретного товара
    @Override
    @Transactional
    public void updateProduct(Integer id, String name, String description) {
        // Все операции внутри транзакции будут выполнены в базе данных
        this.productRepository.findById(id)
                .ifPresentOrElse(product -> {
                    product.setName(name);
                    product.setDescription(description);
//                    // при использовании JPA для обновления в базе данных
//                    // без @Transactional применяем процедуру обновления:
//                    this.productRepository.save(product);
                }, () -> {
                    throw new NoSuchElementException();
                });
    }

    // реализация метода для удаления конкретного товара
    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        this.productRepository.deleteById(id);
    }
}
