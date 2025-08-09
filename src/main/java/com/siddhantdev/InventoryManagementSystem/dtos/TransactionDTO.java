package com.siddhantdev.InventoryManagementSystem.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.siddhantdev.InventoryManagementSystem.enums.TransactionStatus;
import com.siddhantdev.InventoryManagementSystem.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class TransactionDTO {

    private Long id;

    private BigDecimal totalPrice;

    private Integer totalProducts;



    private TransactionType transactionType; ///purchase,sale,return


    private TransactionStatus status;
    //pending, completed,precessing

    private String description;
    private String note;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ProductDTO product;

    private UserDTO user;
    private SupplierDTO supplier;


}
