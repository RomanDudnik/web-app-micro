package org.dudnik.stock.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dudnik.stock.controller.payload.UpdateProductPayload;
import org.dudnik.stock.model.Product;
import org.dudnik.stock.service.ProductService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * Контроллер для работы с конкретными товарами из списка
 * Обработка Http запросов
 */

@Controller
@RequestMapping("catalogue/products/{productId:\\d+}")   // конструктор с аргументами для final свойств
@RequiredArgsConstructor
public class ProductController {

    // внедрение зависимости - сервиса
    private final ProductService productService;

    // внедрение зависимости - локализатора
    private final MessageSource messageSource;

    // метод для получения конкретного товара
    // для избежания дублирования кода в последующих методах
    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.productService.findProduct(productId)
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    // создание страницы товара
    @GetMapping
    public String getProduct() {
        return "catalogue/products/product";
    }

    // редактирование страницы товара
    @GetMapping("edit")
    public String getEditProductPage() {
        return "catalogue/products/edit";
    }

    // форма сохранения изменений
    @PostMapping("edit")
    public String updateProduct(@ModelAttribute(name = "product", binding = false) Product product, @Valid UpdateProductPayload payload,
                                BindingResult bindingResult, Model model) {
        // проверка валидации полей формы
        // собираем все ошибки и передаем их в модель
        if (bindingResult.hasErrors()) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            // перенаправление на страницу создания нового товара
            // в случае ошибок (рендеринг формы заново с указанием ошибок)
            return "catalogue/products/edit";
        } else {
            this.productService.updateProduct(product.getId(), payload.name(), payload.description());
            return "redirect:/catalogue/products/%d".formatted(product.getId());
        }

    }

    //форма для удаления товара
    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
        this.productService.deleteProduct(product.getId());
        return "redirect:/catalogue/products/list";
    }

    // обработка ошибок при попытке получить несуществующий товар
    // перенаправление на страницу с ошибкой
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model,
                                               HttpServletResponse response, Locale locale) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", this.messageSource.getMessage(exception.getMessage(),
                new Object[0], exception.getMessage(), locale));
        return "errors/404";
    }
}
