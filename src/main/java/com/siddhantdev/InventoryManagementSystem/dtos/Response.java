package com.siddhantdev.InventoryManagementSystem.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.siddhantdev.InventoryManagementSystem.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

/// for more cleaner code
/// used for managing our response
    /// generic
    private int status;
    private String message;
    // for login
    private String token;
    private UserRole role;
    private String expirationTime;// for token


    //for pagination
    private Integer totalPages;
    private Long totalElements;

    //data output

    private UserDTO user;
    private List<UserDTO> users; ///to display all users

    private SupplierDTO supplier;
    private List<SupplierDTO> suppliers;

    private CategoryDTO category;
    private List<CategoryDTO> categories;

    private ProductDTO product;
    private List<ProductDTO> products;

    private TransactionDTO transaction;
    private List<TransactionDTO> transactions;

    private final LocalDateTime timestamp= LocalDateTime.now();





}
