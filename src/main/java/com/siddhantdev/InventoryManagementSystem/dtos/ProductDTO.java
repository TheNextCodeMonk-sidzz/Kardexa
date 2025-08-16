package com.siddhantdev.InventoryManagementSystem.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
//fields that are null or not true are ignored in json res. during serialization
/// thats why we use this annotation
@JsonIgnoreProperties(ignoreUnknown = true)
///  this annotation is gonna ignore the fields that are not req in json req.

public class ProductDTO {

    private Long id;

    private Long categoryId;
    private Long productId;
    private Long supplierId;

    private String name;

    private String sku;


    private BigDecimal price;

    //these all annotations are used to validate the data. in FE we say required, in BE we use these annotations to validate the data

    private Integer stockQuantity;

    private String description;
    private LocalDateTime  expiryDate;
    private String imageUrl;

    private LocalDateTime createdAt;


}
