package org.dudnik.catalog.repository;

import org.dudnik.catalog.model.Product;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Реализация репозитория для работы с товарами
 * (в данной реализации - в памяти)
 */

@Repository
public class InMemoryProductRepository implements ProductRepository {

    // Для хранения данных в памяти, создадим список с товарами
    private final List<Product> products = Collections.synchronizedList(new ArrayList<>());

//    public InMemoryProductRepository() {
//        // Заполняем список данными для тестирования
//        IntStream.range(1, 4).forEach(i -> {
//            this.products.add(new Product(i, "Product " + i, "Description " + i));
//        });
//    }

    // Реализация метода для возвращения списка с товарами
    @Override
    public List<Product> findAll() {
        return Collections.unmodifiableList(this.products); // возвращаем неизменяемый список this.products
    }
    // Реализация метода для создания нового товара
    @Override
    public Product save(Product product) {
        // присваиваем новый id новому товару
        // ищем максимальное значение id в списке и прибавляем 1
        product.setId(this.products.stream()
                .max(Comparator.comparingInt(Product::getId))
                .map(Product::getId)
                .orElse(0) + 1);
        // добавляем новый элемент в список
        this.products.add(product);
        return product;
    }

    // Реализация метода для получения конкретного товара
    @Override
    public Optional<Product> findById(Integer productId) {
        // возвращаем первый элемент списка с заданным id
        // и сравниваем его с идентификатором текущего товара
        return this.products.stream()
                .filter(product -> Objects.equals(productId, product.getId()))
                .findFirst();
    }

    // Реализация метода для удаления конкретного товара
    @Override
    public void deleteById(Integer id) {
        this.products.removeIf(product -> Objects.equals(id, product.getId()));
    }
}
