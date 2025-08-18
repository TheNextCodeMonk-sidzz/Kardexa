package com.siddhantdev.InventoryManagementSystem.services.impl;


import com.siddhantdev.InventoryManagementSystem.dtos.Response;
import com.siddhantdev.InventoryManagementSystem.dtos.TransactionDTO;
import com.siddhantdev.InventoryManagementSystem.dtos.TransactionRequest;
import com.siddhantdev.InventoryManagementSystem.enums.TransactionStatus;
import com.siddhantdev.InventoryManagementSystem.enums.TransactionType;
import com.siddhantdev.InventoryManagementSystem.exceptions.NameValueRequiredException;
import com.siddhantdev.InventoryManagementSystem.exceptions.NotFoundException;
import com.siddhantdev.InventoryManagementSystem.models.Product;
import com.siddhantdev.InventoryManagementSystem.models.Supplier;
import com.siddhantdev.InventoryManagementSystem.models.Transaction;
import com.siddhantdev.InventoryManagementSystem.models.User;
import com.siddhantdev.InventoryManagementSystem.repositories.ProductRepository;
import com.siddhantdev.InventoryManagementSystem.repositories.SupplierRepository;
import com.siddhantdev.InventoryManagementSystem.repositories.TransactionRepository;
import com.siddhantdev.InventoryManagementSystem.services.TransactionService;
import com.siddhantdev.InventoryManagementSystem.services.UserService;
import com.siddhantdev.InventoryManagementSystem.specification.TransactionFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {


    @Override
    public Response purchase(TransactionRequest transactionRequest) {
        Long productId=transactionRequest.getProductId();
        Long supplierId=transactionRequest.getSupplierId();
        int quantity=transactionRequest.getQuantity();

        //validate supplier id;
        if(supplierId==null)throw new NameValueRequiredException("Supplier Id is Required");
        // validate if product is present or no
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new NotFoundException("Product Not Found"));
        // same here
        Supplier supplier=supplierRepository.findById(supplierId)
                .orElseThrow(()->new NotFoundException("Supplier Not Found"));
        User user=userService.getCurrentLoggedInUser();
        // update the quantity and resave
        product.setStockQuantity(product.getStockQuantity()+quantity);
        productRepository.save(product);

        // create a transaction

        Transaction transaction=Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Purchase made successfully")
                .build();
    }



    @Override
    public Response sell(TransactionRequest transactionRequest) {
        Long productId=transactionRequest.getProductId();
        int quantity=transactionRequest.getQuantity();

        Product product=productRepository.findById(productId)
                .orElseThrow(()->new NotFoundException("Product Not Found"));
        // same here

        User user=userService.getCurrentLoggedInUser();
        // update stock

        // update the quantity and resave
        product.setStockQuantity(product.getStockQuantity()-quantity);
        productRepository.save(product);

        //create a transaction
        Transaction transaction=Transaction.builder()
                .transactionType(TransactionType.SALE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Product Sale made successfully")
                .build();



    }

    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {

        Long productId=transactionRequest.getProductId();
        Long supplierId=transactionRequest.getSupplierId();
        int quantity=transactionRequest.getQuantity();

        //validate supplier id;
        if(supplierId==null)throw new NameValueRequiredException("Supplier Id is Required");
        // validate if product is present or no
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new NotFoundException("Product Not Found"));
        // same here
        Supplier supplier=supplierRepository.findById(supplierId)
                .orElseThrow(()->new NotFoundException("Supplier Not Found"));
        User user=userService.getCurrentLoggedInUser();

        // update the quantity and resave
        product.setStockQuantity(product.getStockQuantity()+ quantity);
        productRepository.save(product);

        //create a transaction
        Transaction transaction=Transaction.builder()
                .transactionType(TransactionType.RETURN_TO_SUPPLIER)
                .status(TransactionStatus.PROCESSING)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Product return in progress")
                .build();
    }

    @Override
    public Response getAllTransactions(int page, int size, String filter) {

        Pageable pageable=PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"id"));

        //use the Transaction specification
        Specification<Transaction> spec= TransactionFilter.byFilter(filter);
        Page<Transaction> transactionPage=transactionRepository.findAll(spec, pageable);

        List<TransactionDTO> transactionDTOS=modelMapper.map(transactionPage.getContent(), new TypeToken<List<TransactionDTO>>()
        {}.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setSupplier(null);
            transactionDTO.setProduct(null);
        });

        return Response.builder()
                .status(200)
                .message("Success")
                .transactions(transactionDTOS)
                .totalElements(transactionPage.getTotalElements())
                .totalPages(transactionPage.getTotalPages())
                .build();

    }

    @Override
    public Response getAllTransactionById(Long id) {
        Transaction transaction= transactionRepository.findById(id).
                orElseThrow(()-> new NotFoundException("Transaction not Found"));

        TransactionDTO transactionDTO=modelMapper.map(transaction, TransactionDTO.class);

        transactionDTO.setUser(null);
        return Response.builder()
                .status(200)
                .message("Success")
                .transaction(transactionDTO)
                .build();
    }

    @Override
    public Response getAllTransactionByMonthAndYear(int month, int year) {
        List<Transaction> transactions=transactionRepository.findAll(TransactionFilter.byMonthAndYear(month, year));

        List<TransactionDTO> transactionDTOS=modelMapper.map(transactions, new TypeToken<List<TransactionDTO>>(){}.getType());

        transactionDTOS.forEach(transactionDTO ->{
            transactionDTO.setUser(null);
            transactionDTO.setSupplier(null);
            transactionDTO.setProduct(null);
        } );

        return Response.builder()
                .status(200)
                .message("Success")
                .transactions(transactionDTOS)
                .build();
    }

    @Override
    public Response updateTransactionStatus(Long transactionId, TransactionStatus status) {

        Transaction existingTransaction = transactionRepository.findById(transactionId).
                orElseThrow(()-> new NotFoundException("Transaction not Found"));

        existingTransaction.setStatus(status);
        existingTransaction.setUpdatedAt(LocalDateTime.now());

        transactionRepository.save(existingTransaction);
        return Response.builder()
                .status(200)
                .message("Transaction Status Successfully Updated")
                .build();
    }

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

}
