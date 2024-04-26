package org.dudnik.catalog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dudnik.catalog.controller.payload.UpdateProductPayload;
import org.dudnik.catalog.model.Product;
import org.dudnik.catalog.service.ProductService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * REST контроллер для работы с конкретными товарами
 * Без форм представления
 * С возвращением данных в виде JSON
 */

@RestController
@RequiredArgsConstructor            // конструктор с параметрами для внедрения зависимостей(зависимость от service)
@RequestMapping("catalog-api/products/{productId:\\d+}")
public class ProductRestController {

    // внедряем зависимость от service
    private final ProductService productService;

    // внедряем зависимость от messageSource - позволяет локализировать ошибки
    private final MessageSource messageSource;

    // для унификации логики внутренних методов в контроллере
    // используем @ModelAttribute для получения конкретного объекта
    @ModelAttribute("product")
    public Product getProduct(@PathVariable("productId") int productId) {
        return this.productService.findProduct(productId)
                    .orElseThrow(() -> new NoSuchElementException("catalog.errors.product.not_found"));
    }

    // метод для получения конкретного товара по id
    // возвращаем найденный объект полученный из модели
    @GetMapping
    public Product findProduct(@ModelAttribute("product") Product product) {
        return product;
    }

    // метод для редактирования конкретного товара по id
    @PutMapping
    public ResponseEntity<?> updateProduct(@PathVariable("productId") int productId,
                                              @Valid @RequestBody UpdateProductPayload payload,
                                              BindingResult bindingResult)
            throws BindException {
        // проверка на ошибки с помощью BindingResult
        if(bindingResult.hasErrors()) {
            // если ошибки класса BindException - выбрасываем их
            if (bindingResult instanceof BindException exception) {
                throw exception;
                // если не являются экземпляром BindException - создаем новый и выбрасываем
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            // обновляем данные
            this.productService.updateProduct(productId, payload.name(), payload.description());
            // возвращаем ссылку на обновленный объект
            return ResponseEntity
                    .noContent()  // если данный ответ без изменений данных (нет смысла возвращать ссылку на объект)
                    .build();
        }
    }

    // метод для удаления конкретного товара по id
    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") int productId) {
        this.productService.deleteProduct(productId);
        return ResponseEntity
                .noContent()    // нет смысла возвращать ссылку на объект который удалили
                .build();
    }

    // метод обработки ошибок
    // при попытке получить несуществующий объект
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException exception,
                                                                      Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                    this.messageSource.getMessage(exception.getMessage(), new Object[0],
                            exception.getMessage(), locale)));
    }
}
