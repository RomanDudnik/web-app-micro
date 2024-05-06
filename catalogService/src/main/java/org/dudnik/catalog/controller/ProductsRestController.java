package org.dudnik.catalog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dudnik.catalog.controller.payload.NewProductPayload;
import org.dudnik.catalog.model.Product;
import org.dudnik.catalog.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.security.Principal;
import java.util.Map;

/**
 * REST контроллер для работы со списком товаров(каталогом)
 * Предоставляет методы для получения списка и добавления нового товара
 * Без форм представления
 * С возвращением данных в виде JSON
 */

@RestController
@RequiredArgsConstructor        // конструктор с параметрами для внедрения зависимостей final(зависимость от service)
@RequestMapping("catalog-api/products")
public class ProductsRestController {

    // внедряем зависимость от service
    private final ProductService productService;

    // метод для получения списка товаров
    @GetMapping
    public Iterable<Product> getProducts(@RequestParam(name = "filter", required = false) String filter,
                                         Principal principal) {
        return this.productService.findAllProducts(filter);
    }

    // метод для добавления нового товара
    // через ResponseEntity для комплексного возврата данных
    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody NewProductPayload payload,
                                              BindingResult bindingResult,
                                              UriComponentsBuilder uriComponentsBuilder)
            throws BindException {

        // проверка на ошибки с помощью BindingResult
        // BindException расширяет BindingResult
        if(bindingResult.hasErrors()) {
            // если ошибки класса BindException - выбрасываем их
            if (bindingResult instanceof BindException exception) {
                throw exception;
                // если не являются экземпляром BindException - создаем новый и выбрасываем
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Product product = this.productService.createProduct(payload.name(), payload.description());
            // возвращаем ссылку на созданный объект
            return ResponseEntity
                    // в методе created() формируем ссылку на созданный объект с помощью UriComponentsBuilder
                    .created(uriComponentsBuilder
                            .replacePath("/catalog-api/products/{productId}")
                            .build(Map.of("productId", product.getId())))
                    .body(product);

        }
    }
}
