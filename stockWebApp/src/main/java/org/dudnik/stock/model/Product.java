package org.dudnik.stock.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель данных
 * Товар
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Integer id;

    private String name;

    private String description;
}
