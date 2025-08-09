package com.siddhantdev.InventoryManagementSystem.repositories;

import com.siddhantdev.InventoryManagementSystem.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
/// So this is going to allow our repository to make use of specification filter.(JpaSpecificationExecutor<Transaction>)
    // here we can filter our transaction based on any filter we want.
    //very great feature by spring boot


}
