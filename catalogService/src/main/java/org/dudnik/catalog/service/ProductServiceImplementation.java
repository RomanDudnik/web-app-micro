package org.dudnik.catalog.service;

import lombok.RequiredArgsConstructor;
import org.dudnik.catalog.model.Product;
import org.dudnik.catalog.repository.ProductRepository;
import org.springframework.stereotype.Service;

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
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    // реализация метода для создания нового товара
    @Override
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
    public void updateProduct(Integer id, String name, String description) {
        this.productRepository.findById(id)
                .ifPresentOrElse(product -> {
                    product.setName(name);
                    product.setDescription(description);
                }, () -> {
                    throw new NoSuchElementException();
                });
    }

    // реализация метода для удаления конкретного товара
    @Override
    public void deleteProduct(Integer id) {
        this.productRepository.deleteById(id);
    }
}
