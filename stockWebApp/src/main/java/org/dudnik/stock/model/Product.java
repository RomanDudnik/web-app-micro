package org.dudnik.stock.model;

/**
 * Сущность "Товар"
 * Со стороны сервиса управления товарами
 * не требуется изменения содержимого класса
 * поэтому воспользуемся record
 * @param id
 * @param name
 * @param description
 */

public record Product(int id, String name, String description) {
}
