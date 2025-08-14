package com.siddhantdev.InventoryManagementSystem.repositories;

import com.siddhantdev.InventoryManagementSystem.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {


}
