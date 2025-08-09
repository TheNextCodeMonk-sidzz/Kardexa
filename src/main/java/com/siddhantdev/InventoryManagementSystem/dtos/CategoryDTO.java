package com.siddhantdev.InventoryManagementSystem.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
//fields that are null or not true are ignored in json res. during serialization
/// thats why we use this annotation
@JsonIgnoreProperties(ignoreUnknown = true)
///  this annotation is gonna ignore the fields that are not req in json req.

public class CategoryDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private List<ProductDTO> proucts;

}
