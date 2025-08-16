package com.siddhantdev.InventoryManagementSystem.services;

import com.siddhantdev.InventoryManagementSystem.dtos.Response;
import com.siddhantdev.InventoryManagementSystem.dtos.SupplierDTO;

public interface SupplierService {


    Response addSupplier(SupplierDTO supplierDTO);

    Response updateSupplier(Long id,SupplierDTO supplierDTO);

    Response getAllSupplier();

    Response getSupplierById(Long id);



    Response deleteSupplier(Long id);
}
