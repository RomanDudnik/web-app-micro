package org.dudnik.stock.config;

import org.dudnik.stock.security.OAuthClientHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.dudnik.stock.service.ProductsServiceClientImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

/**
 * Конфигурация клиентского сервиса
 */

@Configuration
public class ClientBeans {
    @Bean
    public ProductsServiceClientImplementation productsServiceClient(@Value("${services.catalog.uri:http://localhost:8081}") String catalogUrl,
                                                                     ClientRegistrationRepository clientRegistrationRepository,
                                                                     OAuth2AuthorizedClientRepository authorizedClientRepository,
                                                                     @Value("${services.catalog.registration-id:keycloak}") String registrationId) {
        return new ProductsServiceClientImplementation(RestClient.builder()
                .baseUrl(catalogUrl)
                .requestInterceptor(new OAuthClientHttpRequestInterceptor(
                        new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,
                                authorizedClientRepository), registrationId))
                .build());
    }
}
