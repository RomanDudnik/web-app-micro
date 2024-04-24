package org.dudnik.stock.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dudnik.stock.controller.payload.NewProductPayload;
import org.dudnik.stock.model.Product;
import org.dudnik.stock.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы со списком товаров
 * Обработка Http запросов
 */

@Controller
@RequiredArgsConstructor        // конструктор с аргументами для final свойств
@RequestMapping("catalog/products")
public class ProductsController {

    // внедрение зависимости - сервиса
    private final ProductService productService;

    // метод для получения списка товаров
    @GetMapping("list")
    public String getProductsList(Model model) {
        model.addAttribute("products", this.productService.findAllProducts());
        return "catalog/products/list";
    }

    // создание страницы нового товара
    @GetMapping("create")
    public String getNewProductPage() {
        return "catalog/products/new_product";
    }

    // обработка формы создания нового товара
    @PostMapping("create")
    public String createProduct(@Valid NewProductPayload payload,
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
            return "catalog/products/new_product";
        } else {
            Product product = this.productService.createProduct(payload.name(), payload.description());
            // перенаправление на страницу созданного товара (после создания)
            return "redirect:/catalog/products/%d".formatted(product.getId());
        }

    }

}
