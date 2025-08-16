package com.siddhantdev.InventoryManagementSystem.services.impl;

import com.siddhantdev.InventoryManagementSystem.dtos.Response;
import com.siddhantdev.InventoryManagementSystem.dtos.SupplierDTO;
import com.siddhantdev.InventoryManagementSystem.exceptions.NotFoundException;
import com.siddhantdev.InventoryManagementSystem.models.Supplier;
import com.siddhantdev.InventoryManagementSystem.repositories.SupplierRepository;
import com.siddhantdev.InventoryManagementSystem.services.SupplierService;
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
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    private final ModelMapper modelMapper;



    @Override
    public Response addSupplier(SupplierDTO supplierDTO) {
        Supplier supplierToSave=modelMapper.map(supplierDTO, Supplier.class);

        supplierRepository.save(supplierToSave);

        return Response.builder()
                .status(200)
                .message("Supplier was saved successfully")
                .build();
    }

    @Override
    public Response updateSupplier(Long id, SupplierDTO supplierDTO) {

        Supplier existingSupplier=supplierRepository.findById(id).orElseThrow(()->new NotFoundException("Supplier not Found"));

        if(supplierDTO.getName()!=null)existingSupplier.setName(supplierDTO.getName());

        if(supplierDTO.getContactInfo()!=null)existingSupplier.setContactInfo(supplierDTO.getContactInfo());

        if(supplierDTO.getAddress()!=null)existingSupplier.setAddress(supplierDTO.getAddress());


        supplierRepository.save(existingSupplier);

        return Response.builder()
                .status(200)
                .message("Supplier was Successfully updated")
                .build();
    }

    @Override
    public Response getAllSupplier() {
        List<Supplier> suppliers= supplierRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<SupplierDTO> supplierDTOList=modelMapper.map(suppliers, new TypeToken<List<SupplierDTO>>()
        {}.getType());

        return Response.builder()
                .status(200)
                .message("Success")
                .suppliers(supplierDTOList)
                .build();
    }

    @Override
    public Response getSupplierById(Long id) {
        Supplier supplier=supplierRepository.findById(id).orElseThrow(()->new NotFoundException("Supplier not Found"));

        SupplierDTO supplierDTO=modelMapper.map(supplier, SupplierDTO.class);

        return Response.builder()
                .status(200)
                .message("Success")
                .supplier(supplierDTO)
                .build();
    }

    @Override
    public Response deleteSupplier(Long id) {
        supplierRepository.findById(id).orElseThrow(()->new NotFoundException("Supplier not Found"));

        supplierRepository.deleteById(id);


        return Response.builder()
                .status(200)
                .message("Supplier was Successfully deleted")
                .build();
    }
}
