package com.example.demo.controller;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserPostDTO;
import com.example.demo.dto.UslugaDTO;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UslugaMapper;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.model.Usluga;
import com.example.demo.service.UserService;
import com.example.demo.service.UslugaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UslugaService uslugaService;

    @Autowired
    public UserController(UserService userService, UslugaService uslugaService) {
        this.userService = userService;
        this.uslugaService = uslugaService;
    }

    @Operation(summary = "Изменить роль пользователя (только для админа)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id, @RequestBody String newRole) {
        Optional<User> optionalUser = userService.findByID(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(newRole);
            userService.saveUser(user);
            return ResponseEntity.ok("User role updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Посмотреть всех пользователей")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
            updatedUser.setTelephoneNumber(existingUser.getTelephoneNumber());
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

    @Operation(summary = "Посмотреть всех пользователей с ролью 'мастер'")
    @GetMapping("/masters")
    public ResponseEntity<List<UserDTO>> getMasters() {
        List<UserDTO> masters = userService.getUsers().stream()
                .filter(user -> "master".equalsIgnoreCase(user.getRole()))
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(masters);
    }

    @Operation(summary = "Получить информацию по определенному пользователю по его почте")
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> userOptional = Optional.ofNullable(userService.findByEmail(email));
        return userOptional.map(UserMapper::toDTO).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}/{uslugaId}/favorite")
    public ResponseEntity<String> addUslugaToFavorites(@PathVariable Long userId, @PathVariable Long uslugaId) {
        Optional<User> optionalUser = userService.findByID(userId);
        Optional<Usluga> optionalUsluga = uslugaService.findByID(uslugaId);
        if (optionalUser.isPresent() && optionalUsluga.isPresent()) {
            User user = optionalUser.get();
            Usluga usluga = optionalUsluga.get();
            if (!user.getFavoriteUslugas().contains(usluga)) {
                user.getFavoriteUslugas().add(usluga);
                userService.saveUser(user);
                return ResponseEntity.ok("Usluga added to favorites");
            } else {
                return ResponseEntity.badRequest().body("Usluga already in favorites");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Удалить услугу из избранного")
    @DeleteMapping("/{userId}/{uslugaId}/favorite")
    public ResponseEntity<String> removeUslugaFromFavorites(@PathVariable Long userId, @PathVariable Long uslugaId) {
        Optional<User> optionalUser = userService.findByID(userId);
        Optional<Usluga> optionalUsluga = uslugaService.findByID(uslugaId);
        if (optionalUser.isPresent() && optionalUsluga.isPresent()) {
            User user = optionalUser.get();
            Usluga Usluga = optionalUsluga.get();
            user.getFavoriteUslugas().remove(Usluga);
            userService.saveUser(user);
            return ResponseEntity.ok("Usluga removed from favorites");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Посмотреть услуги из избранного")
    @GetMapping("/{userId}/favorite-Uslugas")
    public ResponseEntity<List<UslugaDTO>> getFavoriteUslugas(@PathVariable Long userId) {
        Optional<User> optionalUser = userService.findByID(userId);
        if (optionalUser.isPresent()) {
            List<Usluga> favoriteUslugas = optionalUser.get().getFavoriteUslugas();
            List<UslugaDTO> dtoList = favoriteUslugas.stream()
                    .map(UslugaMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Добавить отзыв")
    @PostMapping("/user/{reviewerId}/review/{reviewedUserId}")
    public ResponseEntity<?> addReview(@PathVariable Long reviewerId, @PathVariable Long reviewedUserId, @RequestBody String content) {
        Optional<User> optionalReviewer = userService.findByID(reviewerId);
        Optional<User> optionalReviewedUser = userService.findByID(reviewedUserId);

        if (optionalReviewer.isPresent() && optionalReviewedUser.isPresent()) {
            User reviewer = optionalReviewer.get();
            User reviewedUser = optionalReviewedUser.get();
            Review review = new Review(reviewer, reviewedUser, content);
            userService.saveReview(review);
            return ResponseEntity.ok("Review added successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*@Operation(summary = "Посмотреть отзывы на определенного мастера")
    @GetMapping("/user/{userId}/reviews")
    public ResponseEntity<?> getReviewsForUser(@PathVariable Long userId) {
        Optional<User> optionalUser = userService.findByID(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Review> reviews = userService.getReviewsForUser(user);
            List<ReviewDTO> dtoList = reviews.stream().map(ReviewMapper::toDTO).collect(Collectors.toList());
            return ResponseEntity.ok(dtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    @Operation(summary = "Посмотреть отзывы на определенного мастера")
    @GetMapping("/user/{userId}/reviews")
    public ResponseEntity<?> getReviewsForUser(@PathVariable Long userId, Principal principal) {

        // 1) находим профиль мастера
        User reviewedUser = userService.findByID(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 2) находим того, кто делает запрос
        User requester = userService.findByEmail(principal.getName());
        if (requester == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 3) получаем список в зависимости от роли
        List<Review> reviews = userService.getReviewsForViewer(reviewedUser, requester);

        List<ReviewDTO> dtoList = reviews.stream()
                .map(ReviewMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "Удалить отзыв")
    @DeleteMapping("/user/review/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        Optional<Review> optionalReview = userService.findReviewById(reviewId);

        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            String currentUsername = getCurrentUsername();
            if (review.getReviewer().getEmail().equals(currentUsername)) {
                userService.deleteReview(reviewId);
                return ResponseEntity.ok("Review deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to delete this review.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
