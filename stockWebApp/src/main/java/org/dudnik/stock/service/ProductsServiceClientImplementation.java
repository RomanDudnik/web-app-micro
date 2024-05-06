package org.dudnik.stock.service;

import lombok.RequiredArgsConstructor;
import org.dudnik.stock.controller.payload.NewProductPayload;
import org.dudnik.stock.controller.payload.UpdateProductPayload;
import org.dudnik.stock.model.Product;
import org.dudnik.stock.service.exceptions.BadRequestException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Реализация клиентcкого сервиса
 * REST для работы с сервисом каталога
 */

@RequiredArgsConstructor
public class ProductsServiceClientImplementation implements ProductsServiceClient {

    // воспользуемся RestClient для выполнения http запросов
    private final RestClient restClient;


    // способ получения доступа к объявленным в списке типам
    // получаем список - вытаскиваем парметризированные данные
    private static final ParameterizedTypeReference<List<Product>>
            PRODUCTS_TYPE_REF = new ParameterizedTypeReference<>() {
    };

    @Override
    public List<Product> findAllProducts(String filter) {
        return this.restClient
                .get()      // метод GET
                .uri("/catalog-api/products?filter={filter}", filter) // формируем URI
                .retrieve()     // получаем ответ
                // нужен ответ в параметризированном виде (преобразование типизированных данных(список))
                .body(PRODUCTS_TYPE_REF); // преобразование типизированных данных
    }

    // метод создания нового продукта
    @Override
    public Product createProduct(String name, String description) {
        try {
            return this.restClient
                    .post()      // метод POST
                    .uri("/catalog-api/products") // формируем URI
                    .contentType(MediaType.APPLICATION_JSON)    // формат в котором мы передаем данные
                    .body(new NewProductPayload(name, description))   // передаем данные
                    .retrieve()     // получаем ответ
                    .body(Product.class);
            // выбрасываем исключение, если BadRequest
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    // метод получения конкретного продукта
    @Override
    public Optional<Product> findProduct(int productId) {
        try {
            return Optional.ofNullable(this.restClient
                    .get()// метод GET
                    .uri("/catalog-api/products/{productId}", productId)
                    .retrieve()
                    .body(Product.class));
            // выбрасываем исключение, если NotFound
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }


    // метод обновления продукта
    @Override
    public void updateProduct(int productId, String name, String description) {
        try {
            this.restClient
                    .put()      // метод PUT
                    .uri("/catalog-api/products/{productId}", productId) // формируем URI
                    .contentType(MediaType.APPLICATION_JSON)    // формат в котором мы передаем данные
                    .body(new UpdateProductPayload(name, description))  // передаем данные
                    .retrieve()     // получаем ответ
                    .toBodilessEntity();    // если требуется доступ к ответу
            // выбрасываем исключение, если BadRequest
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteProduct(int productId) {
        try {
            this.restClient
                    .delete()
                    .uri("/catalog-api/products/{productId}", productId)
                    .retrieve()
                    .toBodilessEntity();       // если требуется доступ к ответу
            // выбрасываем исключение, если NotFound
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NoSuchElementException("catalog.errors.product.not_found");
        }
    }
}
