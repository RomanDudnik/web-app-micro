package org.dudnik.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Приложение-клиент для регистрации товаров на складе
 * управления товарами на складе
 * связь с сервисом - каталог товаров
 * возможность работы в браузере посредством шаблонов представлений
 */

@SpringBootApplication
public class StockApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class,args);
    }
}
