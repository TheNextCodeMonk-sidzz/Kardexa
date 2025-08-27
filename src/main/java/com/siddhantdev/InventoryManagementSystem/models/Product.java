package com.siddhantdev.InventoryManagementSystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Products")
@Data
@Builder
// Entity model classes using annotations
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Column(unique = true)
    @NotBlank(message = "SKU is required")  //// sku is Stock Keeping Unit, a unique identifier for products.
                                            //2 product cant have same sku. so sku is required
    private String sku;

    @Positive(message = "Product Price must be positive")
    private BigDecimal price;

    //these all annotations are used to validate the data. in FE we say required, in BE we use these annotations to validate the data
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    private String description;
    private LocalDateTime  expiryDate;
    private String imageUrl;

    private final LocalDateTime createdAt = LocalDateTime.now();


    //many products can be in same category. thats why we use Manytoone rel.
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", description='" + description + '\'' +
                ", expiryDate=" + expiryDate +
                ", imageUrl='" + imageUrl + '\'' +
                ", createdAt=" + createdAt +
                ", category=" + category +
                '}';
    }
}
