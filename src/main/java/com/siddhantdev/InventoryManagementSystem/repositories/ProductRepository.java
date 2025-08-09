package com.siddhantdev.InventoryManagementSystem.repositories;

import com.siddhantdev.InventoryManagementSystem.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {


}
