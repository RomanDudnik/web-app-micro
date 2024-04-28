package org.dudnik.stock.security;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.io.IOException;

/**
 * Класс
 * Для передачи информации об аутентификации пользователя
 * на REST сервис catalog
 */

@RequiredArgsConstructor
public class OAuthClientRequestInterceptor implements ClientHttpRequestInterceptor {

    /**
     * Получаем токен доступа для текущего пользователя
     * И обращаемся к защищенному ресурсу catalog
     */

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    // идентификатор регистрации OAuth, для получения ключей доступа
    private final String registrationId;

    // для получения информации о текущем пользователе
    @Setter
    private SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();   // по умолчанию


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        // проверка заголовка и получение токена
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            // передаем авторизованного клиена для получения ключей доступа или для рефреша токена
            OAuth2AuthorizedClient authorizedClient = this.authorizedClientManager.authorize(
                    OAuth2AuthorizeRequest.withClientRegistrationId(this.registrationId)
                            // получаем информацию о текущем пользователе (работа в контексте пользователя)
                            .principal(this.securityContextHolderStrategy.getContext().getAuthentication())
                            .build());
            request.getHeaders().setBearerAuth(authorizedClient.getAccessToken().getTokenValue());
        }

        return execution.execute(request, body);
    }
}
