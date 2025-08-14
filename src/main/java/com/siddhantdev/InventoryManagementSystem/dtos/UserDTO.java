package com.siddhantdev.InventoryManagementSystem.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.siddhantdev.InventoryManagementSystem.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class UserDTO {
    private Long id;


    private String name;


    private String email;

    /// dont convert the password to jos
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    private String phoneNumber;


    private UserRole role;


    private List<TransactionDTO> transactions;


    private LocalDateTime createdAt;

}
