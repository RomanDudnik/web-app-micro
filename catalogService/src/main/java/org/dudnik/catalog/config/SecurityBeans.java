package org.dudnik.catalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация Spring Security
 * Настройка интеграции с OAuth2
 * сервер keycloak
 */

@Configuration
public class SecurityBeans {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // конфигурируем правила безопасности
        // возвращаем сформированный запрос из правил доступа по SCOPE
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // запросы по настроенным Скоупам (keycloak)
                        .requestMatchers(HttpMethod.POST, "/catalog-api/products")
                        .hasAnyAuthority("SCOPE_edit_catalog")
                        .requestMatchers(HttpMethod.PUT, "/catalog-api/products/{productId}\\d")
                        .hasAnyAuthority("SCOPE_edit_catalog")
                        .requestMatchers(HttpMethod.DELETE, "/catalog-api/products/{productId}\\d")
                        .hasAnyAuthority("SCOPE_edit_catalog")
                        // у всех пользователей доступен просмотр каталога
                        .requestMatchers(HttpMethod.GET)
                        .hasAnyAuthority("SCOPE_view_catalog")
                        // все остальные запросы отклоняются
                        .anyRequest().denyAll())
                // отключаем csrf
                .csrf(CsrfConfigurer::disable)
                // отключаем сессии
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // указываем явно ключи доступа для OAuth2
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(Customizer.withDefaults()))
                .build();
    }
}
