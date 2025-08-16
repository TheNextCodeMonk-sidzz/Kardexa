package com.siddhantdev.InventoryManagementSystem.services;


import com.siddhantdev.InventoryManagementSystem.dtos.ProductDTO;
import com.siddhantdev.InventoryManagementSystem.dtos.Response;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    Response saveProduct(ProductDTO productDTO, MultipartFile imageFile);

    Response updateProduct(ProductDTO productDTO, MultipartFile imageFile);

    Response getAllProduct();

    Response getProductById(Long id);

    Response deleteProduct(Long id);

    Response searchProduct(String input);

}
