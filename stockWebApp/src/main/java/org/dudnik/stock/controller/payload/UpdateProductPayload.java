package org.dudnik.stock.controller.payload;

/**
 * Структура данных для формы редактирования товара
 * с валидацией
 */

public record UpdateProductPayload(String name, String description){
}
