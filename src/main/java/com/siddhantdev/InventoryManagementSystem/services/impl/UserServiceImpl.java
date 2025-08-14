package com.siddhantdev.InventoryManagementSystem.services.impl;

import com.siddhantdev.InventoryManagementSystem.dtos.LoginRequest;
import com.siddhantdev.InventoryManagementSystem.dtos.RegisterRequest;
import com.siddhantdev.InventoryManagementSystem.dtos.Response;
import com.siddhantdev.InventoryManagementSystem.dtos.UserDTO;
import com.siddhantdev.InventoryManagementSystem.enums.UserRole;
import com.siddhantdev.InventoryManagementSystem.exceptions.InvalidCredentialsException;
import com.siddhantdev.InventoryManagementSystem.exceptions.NotFoundException;
import com.siddhantdev.InventoryManagementSystem.models.User;
import com.siddhantdev.InventoryManagementSystem.repositories.UserRepository;
import com.siddhantdev.InventoryManagementSystem.security.JwtUtils;
import com.siddhantdev.InventoryManagementSystem.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public Response registerUser(RegisterRequest registerRequest) {

        UserRole role= UserRole.MANAGER;
        if(registerRequest.getRole()!= null){
            role= registerRequest.getRole();
        }

        User userToSave= User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .role(role)
                .build();

        userRepository.save(userToSave);

        return Response.builder()
                .status(200)
                .message("User was registered successfully")
                .build();
    }

    @Override
    public Response loginUser(@Valid LoginRequest loginRequest) {

        User user=userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->new NotFoundException("Email Not Found"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Password Does Not Match");
        }
        String token= jwtUtils.generateToken(user.getEmail());

        return Response.builder()
                .status(200)
                .message("User Logged In Successfully")
                .role(user.getRole())
                .token(token)
                .expirationTime("6 Month")
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users=userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        //set transaction to null to avoid passing the transactions in the response
        users.forEach(user->user.setTransactions(null));


        List<UserDTO> userDTOS= modelMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType() );

        return Response.builder()
                .status(200)
                .message("Success")
                .users(userDTOS)
                .build();
    }



    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        String email= authentication.getName();

        User user=userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User Not Found"));

        user.setTransactions(null);

        return user;

    }

    @Override
    public Response getUserById(Long id) {

        User user=userRepository.findById(id).orElseThrow(()-> new NotFoundException("User Not Found"));

        UserDTO userDTO=modelMapper.map(user, UserDTO.class);

        userDTO.setTransactions(null);


        return Response.builder()
                .status(200)
                .message("Success")
                .user(userDTO)
                .build();

    }

    @Override
    public Response updateUser(Long id, UserDTO userDTO) {

        User existingUser=userRepository.findById(id).orElseThrow(()-> new NotFoundException("User Not Found"));

        if(userDTO.getEmail()!=null) existingUser.setEmail(userDTO.getEmail());
        if(userDTO.getPhoneNumber()!=null) existingUser.setPhoneNumber(userDTO.getPhoneNumber());

        if(userDTO.getName()!=null) existingUser.setName(userDTO.getName());
        if(userDTO.getRole()!=null) existingUser.setRole(userDTO.getRole());

        if(userDTO.getPassword() !=null || !userDTO.getPassword().isEmpty()){
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userRepository.save(existingUser);

        return Response.builder()
                .status(200)
                .message("User Successfully Updated")
                .build();


    }

    @Override
    public Response deleteUser(Long id) {

        userRepository.findById(id).orElseThrow(()-> new NotFoundException("User Not Found"));

        userRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("User Successfully Deleted")
                .build();

    }


    @Override
    public Response getUserTransactions(Long id) {


        User user=userRepository.findById(id).orElseThrow(()-> new NotFoundException("User Not Found"));

        UserDTO userDTO=modelMapper.map(user, UserDTO.class);

        userDTO.getTransactions().forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setSupplier(null);

        });

        return Response.builder()
                .status(200)
                .message("Success")
                .user(userDTO)
                .build();
    }

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;



}
