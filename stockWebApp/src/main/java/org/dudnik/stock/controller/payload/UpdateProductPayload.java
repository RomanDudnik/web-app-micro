package org.dudnik.stock.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Структура данных для формы редактирования товара
 * с валидацией
 */

public record UpdateProductPayload(

        @NotNull (message = "{catalog.products.update.errors.name_is_null}")
        @Size(min = 3, max = 50, message = "{catalog.products.update.errors.name_size_is_invalid}")
        String name,

        @Size(max = 500, message = "{catalog.products.update.errors.description_size_is_invalid}")
        String description){
}
