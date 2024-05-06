package org.dudnik.stock.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dudnik.stock.service.ProductsServiceClient;
import org.dudnik.stock.controller.payload.UpdateProductPayload;
import org.dudnik.stock.model.Product;
import org.dudnik.stock.service.exceptions.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * Контроллер для работы с конкретными товарами из списка
 * Обработка Http запросов
 */

@Controller
@RequiredArgsConstructor        // конструктор с параметрами для внедрения зависимостей final(зависимость от service)
@RequestMapping("catalog/products/{productId:\\d+}")
public class ProductController {

    // внедрение зависимости - сервиса
    private final ProductsServiceClient productsServiceClient;

    // внедрение зависимости - локализатора сообщений
    private final MessageSource messageSource;

    // метод для получения конкретного товара
    // для избежания дублирования кода в последующих методах
    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.productsServiceClient.findProduct(productId)
                // если товар не найден, выбрасываем исключение
                .orElseThrow(() -> new NoSuchElementException("catalog.errors.product.not_found"));
    }

    // создание страницы товара
    @GetMapping
    public String getProduct() {
        return "catalog/products/product";
    }

    // редактирование страницы товара
    @GetMapping("edit")
    public String getEditProductPage() {
        return "catalog/products/edit";
    }

    // форма сохранения изменений
    @PostMapping("edit")
    public String updateProduct(@ModelAttribute(name = "product", binding = false) Product product,
                                UpdateProductPayload payload, Model model, HttpServletResponse response) {
        try {
            this.productsServiceClient.updateProduct(product.id(), payload.name(), payload.description());
            return "redirect:/catalog/products/%d".formatted(product.id());
        } catch (BadRequestException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            // проверка валидации полей формы
            model.addAttribute("payload", payload);
            // собираем все ошибки и передаем их в модель
            model.addAttribute("errors", exception.getErrors());
            // перенаправление на страницу создания нового товара
            // в случае ошибок (рендеринг формы заново с указанием ошибок)
            return "catalog/products/edit";
        }

    }

    //форма для удаления товара
    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
        this.productsServiceClient.deleteProduct(product.id());
        return "redirect:/catalog/products/list";
    }

    // обработка ошибок при попытке получить несуществующий товар
    // перенаправление на страницу с ошибкой
    // сообщения на странице будут локализированы
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model,
                                               HttpServletResponse response, Locale locale) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", this.messageSource.getMessage(exception.getMessage(),
                new Object[0], exception.getMessage(), locale));
        return "errors/404";
    }
}
