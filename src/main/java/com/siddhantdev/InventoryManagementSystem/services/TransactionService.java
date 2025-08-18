package com.siddhantdev.InventoryManagementSystem.services;

import com.siddhantdev.InventoryManagementSystem.dtos.Response;
import com.siddhantdev.InventoryManagementSystem.dtos.TransactionRequest;
import com.siddhantdev.InventoryManagementSystem.enums.TransactionStatus;

public interface TransactionService {

    //methods required
    Response purchase(TransactionRequest transactionRequest);

    Response sell(TransactionRequest transactionRequest);

    Response returnToSupplier(TransactionRequest transactionRequest);

    Response getAllTransactions(int page, int size, String filter);

    Response getAllTransactionById(Long id);

    Response getAllTransactionByMonthAndYear(int month, int year);

    Response updateTransactionStatus(Long transactionId, TransactionStatus status);



}
