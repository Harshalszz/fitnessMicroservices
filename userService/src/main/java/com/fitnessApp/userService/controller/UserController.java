package com.fitnessApp.userService.controller;

import com.fitnessApp.userService.dto.RegisterRequest;
import com.fitnessApp.userService.dto.UserResponse;
import com.fitnessApp.userService.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userId){

        return ResponseEntity.ok(userService.getUserProfile(userId));

    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> getUserProfile(@Valid @RequestBody RegisterRequest request){

        return ResponseEntity.ok(userService.register(request));

    }
 // endpoint that will be call by activity webclient to validate the user

    @GetMapping("/{userId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String userId){

        return ResponseEntity.ok(userService.existByUserId(userId));

    }


}
