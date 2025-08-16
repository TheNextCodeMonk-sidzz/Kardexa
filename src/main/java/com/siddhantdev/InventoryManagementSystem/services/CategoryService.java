package com.siddhantdev.InventoryManagementSystem.services;

import com.siddhantdev.InventoryManagementSystem.dtos.CategoryDTO;
import com.siddhantdev.InventoryManagementSystem.dtos.Response;

public interface CategoryService {

    Response createCategory(CategoryDTO categoryDTO);

    Response getAllCategories();

    Response getCategoryById(Long id);

    Response updateCategory(Long id, CategoryDTO categoryDTO);

    Response deleteCategory(Long id);
}

