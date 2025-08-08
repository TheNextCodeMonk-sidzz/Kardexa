package com.siddhantdev.InventoryManagementSystem.models;

import com.siddhantdev.InventoryManagementSystem.enums.UserRole;
import jakarta.persistence.*;
import jakarta.transaction.Transaction;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="categories")
@Data
@Builder

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

//    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
//    private List<Product> proucts;

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
