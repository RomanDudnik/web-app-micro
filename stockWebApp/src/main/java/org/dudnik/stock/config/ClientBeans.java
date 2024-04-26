package org.dudnik.stock.config;

import org.springframework.beans.factory.annotation.Value;
import org.dudnik.stock.service.ProductsServiceClientImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Конфигурация клиентского сервиса
 */

@Configuration
public class ClientBeans {
    @Bean
    public ProductsServiceClientImplementation productsServiceClient(@Value("${service.catalog.uri:http://localhost:8081}") String catalogUrl) {
        return new ProductsServiceClientImplementation(RestClient.builder()
                .baseUrl(catalogUrl)
                .build());
    }
}
