-PostgreSQL
БД catalogService

Запуск в Docker:
docker run --name catalog-db -p 5432:5432 -e POSTGRES_DB=catalog -e POSTGRES_USER=catalog -e POSTGRES_PASSWORD=catalog postgres:16

-Keycloak
OAuth 2.0/OIDC-сервер для авторизации сервисов и аутентификации пользователей.

Запуск в Docker:
docker run --name stock-keycloak -p8082:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -v /config/keycloak/import:/opt/keycloak/data/import quay.io/keycloak/keycloak:latest start-dev --import-realm