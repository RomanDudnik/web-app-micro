package org.dudnik.catalog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель данных
 * Товар
 * Сущность в БД
 */

@Entity
//@Table(schema = "catalog", name = "t_product")    //для PostgreSQL
@Table(name = "t_product")    //для H2
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "c_name")
    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @Column(name = "c_description")
    @Size(max = 500)
    private String description;
}
