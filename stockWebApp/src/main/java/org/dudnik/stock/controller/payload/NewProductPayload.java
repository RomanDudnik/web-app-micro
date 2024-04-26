package org.dudnik.stock.controller.payload;

/**
 * Структура данных для формы создания нового товара
 * с валидацией
 */

public record NewProductPayload(String name, String description) {
}
