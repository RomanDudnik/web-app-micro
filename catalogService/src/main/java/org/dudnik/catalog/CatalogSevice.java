package org.dudnik.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Сервис каталога товаров (модуль для логики управления товарами)
 * представляет собой REST API, предоставляющий управление каталогом товаров
 * Интеграция валидации данных
 */

@SpringBootApplication
public class CatalogSevice {
    public static void main(String[] args) {
        SpringApplication.run(CatalogSevice.class, args);
    }
}
