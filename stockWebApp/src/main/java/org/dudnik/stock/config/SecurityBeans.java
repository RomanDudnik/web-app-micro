package org.dudnik.stock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Конфигурация Spring Security
 * OAuth2 для аутентификации
 * сервер keycloak
 */
@Configuration
public class SecurityBeans {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // возвращаем http сессию
        return http
                // требует авторизации пользователя к ресурсу
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // ограничиваем доступ к ресурсу кроме пользователя в качестве менеджера
                        .anyRequest().hasRole("MANAGER"))
                // OAuth2 логин
                .oauth2Login(Customizer.withDefaults())
                // OAuth2 авторизация
                .oauth2Client(Customizer.withDefaults())
                .build();
    }

    /**
     * Добавляем компонент для работы с OAuth2
     * Groups to roles
     * Загружает дополнительные данные о пользователе
     */

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
        // создаем компонент для работы с OAuth2
        OidcUserService oidcUserService = new OidcUserService();
        // возвращаем информацию о пользователе (lambda выражением)
        return userRequest -> {
            // получаем информацию о пользователе
            OidcUser oidcUser = oidcUserService.loadUser(userRequest);  // для запроса загружаем информацию о пользователе
               // получаем права пользователя
            // список ролей
            List<GrantedAuthority> authorities =
                Stream.concat(oidcUser.getAuthorities().stream(),
                    Optional.ofNullable(oidcUser.getClaimAsStringList("groups"))
                            // на случай пустого списка
                            .orElseGet(List::of)
                            // преобразуем в роли
                            .stream()
                            // фильтруем роли
                            .filter(role -> role.startsWith("ROLE_"))
                            .map(SimpleGrantedAuthority::new)
                            .map(GrantedAuthority.class::cast))
                    .toList();
            return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());

        };
    }
}
