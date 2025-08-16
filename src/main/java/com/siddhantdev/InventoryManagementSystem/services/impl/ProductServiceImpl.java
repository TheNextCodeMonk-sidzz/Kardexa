package com.siddhantdev.InventoryManagementSystem.services.impl;


import com.siddhantdev.InventoryManagementSystem.dtos.ProductDTO;
import com.siddhantdev.InventoryManagementSystem.dtos.Response;
import com.siddhantdev.InventoryManagementSystem.exceptions.NotFoundException;
import com.siddhantdev.InventoryManagementSystem.models.Category;
import com.siddhantdev.InventoryManagementSystem.models.Product;
import com.siddhantdev.InventoryManagementSystem.repositories.CategoryRepository;
import com.siddhantdev.InventoryManagementSystem.repositories.ProductRepository;
import com.siddhantdev.InventoryManagementSystem.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepository;

    private static final String IMAGE_DIRECTORY= System.getProperty("user.dir")+"/product-images";



    @Override
    public Response saveProduct(ProductDTO productDTO, MultipartFile imageFile) {
        Category category= categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()-> new NotFoundException("Category not found"));

        //map DTO to entity

        Product productToSave=Product.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity())
                .description(productDTO.getDescription())
                .category(category)
                .build();

        if(imageFile !=null && !imageFile.isEmpty()){
            log.info("Image file exist");
            String imagePath=saveImage(imageFile);
            productToSave.setImageUrl(imagePath);
        }
        //save the product entity

        productRepository .save(productToSave);

        return Response.builder()
                .status(200)
                .message("Product Successfully saved")
                .build();
    }

    @Override
    public Response updateProduct(ProductDTO productDTO, MultipartFile imageFile) {

        // check if product exist
        Product existingProduct= productRepository.findById(productDTO.getProductId())
                .orElseThrow(()-> new NotFoundException("Product Not Found"));

        // check if image is present and not null or empty
        if(imageFile!=null && !imageFile.isEmpty()){
            String imagePath= saveImage(imageFile);
            existingProduct.setImageUrl(imagePath);
        }
        // check if category is present
        // we can decide if we want to update category or not

        if(productDTO.getCategoryId()!=null && productDTO.getCategoryId()>0){
            Category category=categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(()-> new NotFoundException("Category Not Found"));

            existingProduct.setCategory(category);
        }

        // check if product fields are to be changed and updated
        if(productDTO.getName()!=null && !productDTO.getName().isBlank()){
            existingProduct.setName(productDTO.getName());
        }

        if(productDTO.getSku()!=null && !productDTO.getSku().isBlank()){
            existingProduct.setSku(productDTO.getSku());
        }

        if(productDTO.getDescription()!=null && !productDTO.getDescription().isBlank()){
            existingProduct.setDescription(productDTO.getDescription());
        }
        // price not zero and greater to zero condition
        if(productDTO.getPrice()!=null && productDTO.getPrice().compareTo(BigDecimal.ZERO) >=0){
            existingProduct.setPrice(productDTO.getPrice());
        }

        if(productDTO.getStockQuantity()!=null && productDTO.getStockQuantity()>=0){
            existingProduct.setStockQuantity(productDTO.getStockQuantity());
        }

        //save and update the product
        productRepository.save(existingProduct);

        // build our response (for postman or frontend)
        return Response.builder()
                .status(200)
                .message("Product updated successfully")
                .build();



    }

    @Override
    public Response getAllProduct() {
        List<Product> productList=productRepository.findAll(Sort.by(Sort.Direction.DESC , "id"));
        List<ProductDTO> productDTOList= modelMapper.map(productList, new TypeToken<List<ProductDTO>>(){}
                .getType());

        return Response.builder()
                .status(200)
                .message("Success")
                .products(productDTOList)
                .build();
    }

    @Override
    public Response getProductById(Long id) {

        // check if product exist
         Product product=productRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));


        return Response.builder()
                .status(200)
                .message("Success")
                .product(modelMapper.map(product, ProductDTO.class)) //ProductDTO.class → the target type (the class you want the source mapped into)
                //ProductDTO.class is the blueprint (target type) that tells ModelMapper: "Convert the source into this kind of object."
                .build();
    }

    @Override
    public Response deleteProduct(Long id) {
        productRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));

        productRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Product Deleted Successfully")
                .build();

    }

    @Override
    public Response searchProduct(String input) {
        List<Product> products=productRepository.findByNameContainingOrDescriptionContaining(input, input);

        if(products.isEmpty()){
            throw new NotFoundException("No Product found with the given input");
        }

        List<ProductDTO> productDTOList= modelMapper.map(products, new TypeToken<List<ProductDTO>>(){}
                .getType());

        return Response.builder()
                .status(200)
                .message("Success")
                .products(productDTOList)
                .build();

    }

    private String saveImage(MultipartFile imageFile){

        // validate image and check it is greater then  gig
        if(!imageFile.getContentType().startsWith("image/") || imageFile.getSize() > 1024 * 1024 * 1024) {
            throw new IllegalArgumentException("Only one Image file under 1GIG is allowed");
        }

        //create directory if not exist

        File directory = new File(IMAGE_DIRECTORY);
        if(!directory.exists()){
            directory.mkdir();
            log.info(" Directory was created");
        }
        // generate unique file name for image

        String uniqueFileName= UUID.randomUUID()+"_"+ imageFile.getOriginalFilename();

        //get absolute path of image
        String imagePath= IMAGE_DIRECTORY+ File.separator + uniqueFileName;
        try{
            File destinationFile= new File(imagePath);
            imageFile.transferTo(destinationFile); // we are writing image to this folder

        }catch (Exception e){
            throw new IllegalArgumentException("Error Saving image:  "+ e.getMessage());

        }
        return imagePath;

    }

}
