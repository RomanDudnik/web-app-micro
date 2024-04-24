package org.dudnik.stock.service;

import lombok.RequiredArgsConstructor;
import org.dudnik.stock.model.Product;
import org.dudnik.stock.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor            // конструктор с аргументами для final свойств
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
    public void updateProduct(Integer productId, String name, String description) {
        this.productRepository.findById(productId)
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
