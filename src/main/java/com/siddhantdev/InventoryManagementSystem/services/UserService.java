package com.siddhantdev.InventoryManagementSystem.services;

import com.siddhantdev.InventoryManagementSystem.dtos.LoginRequest;
import com.siddhantdev.InventoryManagementSystem.dtos.RegisterRequest;
import com.siddhantdev.InventoryManagementSystem.dtos.Response;
import com.siddhantdev.InventoryManagementSystem.dtos.UserDTO;
import com.siddhantdev.InventoryManagementSystem.models.User;
import jakarta.validation.Valid;

public interface UserService {

    Response registerUser(RegisterRequest registerRequest);

    Response loginUser(@Valid LoginRequest loginRequest);

    Response getAllUsers();

    User getCurrentLoggedInUser();

    Response getUserById(Long id);

    Response updateUser(Long id, UserDTO userDTO);

    Response deleteUser(Long id);

    Response getUserTransactions(Long id);





}
