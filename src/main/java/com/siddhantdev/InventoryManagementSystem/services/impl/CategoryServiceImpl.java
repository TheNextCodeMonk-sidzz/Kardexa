package com.siddhantdev.InventoryManagementSystem.services.impl;


import com.siddhantdev.InventoryManagementSystem.dtos.CategoryDTO;
import com.siddhantdev.InventoryManagementSystem.dtos.Response;
import com.siddhantdev.InventoryManagementSystem.exceptions.NotFoundException;
import com.siddhantdev.InventoryManagementSystem.models.Category;
import com.siddhantdev.InventoryManagementSystem.repositories.CategoryRepository;
import com.siddhantdev.InventoryManagementSystem.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;



    @Override
    public Response createCategory(CategoryDTO categoryDTO) {
        Category categoryToSave=modelMapper.map(categoryDTO, Category.class);
        categoryRepository.save(categoryToSave);

        return Response.builder()
                .status(200)
                .message("Category was created successfully")
                .build();
    }

    @Override
    public Response getAllCategories() {
        List<Category> categories= categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        categories.forEach(category -> category.setProucts(null));
        List<CategoryDTO> categoryDTOList=modelMapper.map(categories, new TypeToken<List<CategoryDTO>>()
        {}.getType());

        return Response.builder()
                .status(200)
                .message("Success")
                .categories(categoryDTOList)
                .build();
    }

    @Override
    public Response getCategoryById(Long id) {
        Category category=categoryRepository.findById(id).orElseThrow(()->new NotFoundException("Category not Found"));

        CategoryDTO categoryDTO=modelMapper.map(category, CategoryDTO.class);

        return Response.builder()
                .status(200)
                .message("Success")
                .category(categoryDTO)
                .build();
    }

    @Override
    public Response updateCategory(Long id, CategoryDTO categoryDTO) {

        Category existingCategory=categoryRepository.findById(id).orElseThrow(()->new NotFoundException("Category not Found"));

        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);

        return Response.builder()
                .status(200)
                .message("Category was Successfully updated")
                .build();

    }

    @Override
    public Response deleteCategory(Long id) {

        categoryRepository.findById(id).orElseThrow(()->new NotFoundException("Category not Found"));

        categoryRepository.deleteById(id);


        return Response.builder()
                .status(200)
                .message("Category was Successfully deleted")
                .build();
    }
}
