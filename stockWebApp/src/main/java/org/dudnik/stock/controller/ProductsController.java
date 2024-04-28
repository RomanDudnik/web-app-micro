package org.dudnik.stock.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dudnik.stock.service.ProductsServiceClient;
import org.dudnik.stock.controller.payload.NewProductPayload;
import org.dudnik.stock.model.Product;
import org.dudnik.stock.service.exceptions.BadRequestException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Контроллер для работы со списком товаров
 * Обработка Http запросов
 */

@Controller
@RequiredArgsConstructor        // конструктор с параметрами для внедрения зависимостей(зависимость от service)
@RequestMapping("catalog/products")
public class ProductsController {

    // внедрение зависимости - сервиса
    private final ProductsServiceClient productsServiceClient;

    // метод для получения списка товаров
    @GetMapping("list")
    public String getProductsList(Model model, @RequestParam(name = "filter", required = false) String filter) {
        model.addAttribute("products", this.productsServiceClient.findAllProducts(filter));
        model.addAttribute("filter", filter);
        return "catalog/products/list";
    }

    // создание страницы нового товара
    @GetMapping("create")
    public String getNewProductPage() {
        return "catalog/products/new_product";
    }

    // обработка формы создания нового товара
    @PostMapping("create")
    public String createProduct(NewProductPayload payload, Model model,
                                HttpServletResponse response) {
        try {
            Product product = this.productsServiceClient.createProduct(payload.name(),
                    payload.description());
            // перенаправление на страницу созданного товара (после создания)
            return "redirect:/catalog/products/%d".formatted(product.id());

        } catch (BadRequestException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            // проверка валидации полей формы
            model.addAttribute("payload", payload);
            // собираем все ошибки и передаем их в модель
            model.addAttribute("errors", exception.getErrors());
            // перенаправление на страницу создания нового товара
            // в случае ошибок (рендеринг формы заново с указанием ошибок)
            return "catalog/products/new_product";
        }
    }
}
