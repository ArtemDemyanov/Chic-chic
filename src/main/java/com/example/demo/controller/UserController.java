package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserPostDTO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Посмотреть всех пользователей")
    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(userService.getUsers().stream()
                .map(UserMapper::toDTO)
                .toList());
    }

    @Operation(summary = "Регистрация пользователя")
    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody UserPostDTO newUserDTO) {
        // Validate required fields
        if (newUserDTO.getName() == null ||
                newUserDTO.getEmail() == null ||
                newUserDTO.getPassword() == null ||
                newUserDTO.getTelephone_number() == null ||
                newUserDTO.getRole() == null) {
            return new ResponseEntity<>("Missing required fields", HttpStatus.BAD_REQUEST);
        }

        // Hash the password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User newUser = UserMapper.toEntity(newUserDTO);
        newUser.setPassword(encoder.encode(newUserDTO.getPassword()));

        // Save the user
        User savedUser = userService.saveUser(newUser);
        return new ResponseEntity<>(UserMapper.toDTO(savedUser), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить профиль пользователя")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO updatedUserDTO) {
        Optional<User> existingUserOptional = userService.findByID(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            User updatedUser = UserMapper.toEntity(updatedUserDTO);
            updatedUser.setId(existingUser.getId());
            updatedUser.setPassword(existingUser.getPassword()); // Preserve the hashed password
            updatedUser.setProfilePicture(existingUser.getProfilePicture()); // Preserve the profile picture

            User savedUser = userService.saveUser(updatedUser);
            return ResponseEntity.ok(UserMapper.toDTO(savedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Обновить фотографию в профиле")
    @PutMapping(path = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateProfilePicture(@RequestPart(value = "file") MultipartFile file, @PathVariable Long id) {
        Optional<User> existingUserOptional = userService.findByID(id);
        if (existingUserOptional.isPresent()) {
            User userToUpdate = existingUserOptional.get();
            if (file != null && !file.isEmpty()) {
                try {
                    userToUpdate.setProfilePicture(file.getBytes());
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update profile picture");
                }
            }
            User savedUser = userService.saveUser(userToUpdate);
            return ResponseEntity.ok(UserMapper.toDTO(savedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Получить информацию по определенному пользователю")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.findByID(id);
        return userOptional.map(UserMapper::toDTO).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userService.findByID(id);
        if (userOptional.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.ok("User Deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Получить информацию по определенному пользователю по его почте")
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> userOptional = Optional.ofNullable(userService.findByEmail(email));
        return userOptional.map(UserMapper::toDTO).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}