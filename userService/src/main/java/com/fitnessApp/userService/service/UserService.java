package com.fitnessApp.userService.service;

import com.fitnessApp.userService.dto.RegisterRequest;
import com.fitnessApp.userService.dto.UserResponse;
import com.fitnessApp.userService.model.User;
import com.fitnessApp.userService.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse register(@Valid RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists in db");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User savedUser = (User) userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());

        return userResponse;


    }

    public UserResponse getUserProfile(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User Not Found"));

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setLastName(user.getLastName());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;


    }
}
