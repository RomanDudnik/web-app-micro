## Многомодульное серверное приложение для регистрации товаров на складе и хранение каталога товаров в БД

## Основные модули приложения:
### CatalogService
Сервис каталога товаров напрямую взаимодействует с базой данных. Именно он будет предоставлять взаимодействие с БД для сервиса StockApplication. Именно здесь будет реализована идея стандартного подхода к проектированию RESTful API. Этот сервис обеспечит CRUD операции над моделью с данными. Также именно в этом сервисе будет реализована валидация полей данных.
Хранение данных реализовано в отдельной БД на базе PostgreeSQL, аутентификация и авторизация пользователей проводится на базе Spring security с использованием токенов JWT на сервере Keycloak.

### StockApplication
Данный сервис представляет собой приложение клиент для взаимодействия с сервисом каталога товаров (CatalogService). С помощью этого приложения сотрудники склада с легкостью смогут взаимодействовать через Web интерфейс.
Сервис предоставляет функционал для взаимодействия со всеми элементами системы, при этом обрабатывает ролевую модель, оперируя только JWT, а весь контроль безопасности реализован на стороне REST API, что позволяет обеспечить безопасность, даже при попытках несанкционированного доступа к данным или функционалу.
Формирование макетов страниц будет реализовано с помощью с Thymeleaf. Web интерфейс не будет включать в себя front разработку, а будет представлять из себя интерфейс работы с данными.



#### БД по дефолту H2
Этого хватает для тестирования приложения

### Для запуска:
### PostgreSQL (при необходимости)
БД catalogService

* Запуск в Docker:
* docker run --name catalog-db -p 5432:5432 -e POSTGRES_DB=catalog -e POSTGRES_USER=catalog -e POSTGRES_PASSWORD=catalog postgres:16

### Keycloak
OAuth 2.0/OIDC-сервер для авторизации сервисов и аутентификации пользователей.

#### Запуск в Docker:
* docker run --name stock-keycloak -p8082:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -v /config/keycloak/import:/opt/keycloak/data/import quay.io/keycloak/keycloak:latest start-dev --import-realm
#### Конфиг Keycloak: 
* config/keycloak/import/stock.json

Технологический стек
### В этом проекте были использованы следующие технологии:
* Spring Framework
* Spring Boot
* Spring MVC
* Thymeleaf
* Lombok
* Spring Data JPA
* Hibernate
* H2 Database 
* PostgreSQL
* RestClient
* Spring Security
* Spring Boot oauth2-client
* Spring Boot oauth2-resource-server
* Keycloak
* SpringValidation
