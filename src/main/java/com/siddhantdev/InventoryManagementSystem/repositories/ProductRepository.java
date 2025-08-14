package com.siddhantdev.InventoryManagementSystem.repositories;

import com.siddhantdev.InventoryManagementSystem.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //first entity and then its type

    // now for search functionality
    List<Product> findByNameContainingOrDescriptionContaining(String name, String description);
    // this is how jpa works

}
