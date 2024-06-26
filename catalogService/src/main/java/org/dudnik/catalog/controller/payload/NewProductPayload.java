package org.dudnik.catalog.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Структура данных для формы создания нового товара
 * с валидацией
 */

public record NewProductPayload(

        @NotNull(message = "{catalog.products.create.errors.name_is_null}")
        @Size(min = 3, max = 50, message = "{catalog.products.create.errors.name_size_is_invalid}")
        String name,

        @Size(max = 500, message = "{catalog.products.create.errors.description_size_is_invalid}")
        String description) {
}
