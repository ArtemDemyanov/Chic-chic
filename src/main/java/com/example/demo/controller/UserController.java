package com.example.demo.controller;

import com.example.demo.dto.UserPostDTO;
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

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
public class UserController {

    private final UserService userService;

    private final UslugaService uslugaService;
    @Autowired
    public UserController(UserService userService, UslugaService uslugaService) {
        this.userService = userService;
        this.uslugaService = uslugaService;
    }

    @Operation(summary = "Посмотреть всех пользователей")
    @GetMapping("/user")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/user")
    public ResponseEntity<Optional<User>> addUser(@RequestBody UserPostDTO newUserDTO) {
        if (newUserDTO.getName() == null ||
                newUserDTO.getEmail() == null ||
                newUserDTO.getPassword() == null || newUserDTO.getTelephone_number() == null || newUserDTO.getRole() == null) {
            return new ResponseEntity<>(Optional.ofNullable(null), HttpStatus.BAD_REQUEST);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User newUser = new User(newUserDTO.getName(), newUserDTO.getEmail(), newUserDTO.getTelephone_number(),
                encoder.encode(newUserDTO.getPassword()),newUserDTO.getRole(),null,null,null);
        userService.saveUser(newUser);
        return new ResponseEntity<>(Optional.ofNullable(newUser),HttpStatus.CREATED);

    }

    @Operation(summary = "Обновить профиль пользователя")
    @PutMapping("/user/{id}")
    public ResponseEntity<Optional<User>> updateUser(@PathVariable(value="id") long Id, @RequestBody User newUser ){
        Optional<User> existingUser = userService.findByID(Id);

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            userToUpdate.setName(newUser.getName());
            userToUpdate.setEmail(newUser.getEmail());
            userService.saveUser(userToUpdate);

            return new ResponseEntity<>(Optional.ofNullable(userToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Обновить фотографию в профиле")
    @PutMapping(path="/user/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Optional<User>> updateProfilePicture(@RequestPart(value = "file") MultipartFile file, @PathVariable(value="id") long Id) {
        Optional<User> existingUser = userService.findByID(Id);

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            if (file != null && !file.isEmpty()) {
                try {
                    userToUpdate.setProfilePicture(file.getBytes());
                } catch (IOException e) {
                    return new ResponseEntity<>(Optional.empty(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            userService.saveUser(userToUpdate);

            return new ResponseEntity<>(Optional.ofNullable(userToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Получить информацию по определенному пользователю")
    @GetMapping("/user/{id}")
    public Optional<User> getUserById(@PathVariable(value = "id") long Id) {
        return userService.findByID(Id);
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable(value = "id") long Id) {
        userService.deleteUser(Id);
        return "User Deleted";
    }

    @Operation(summary = "Получить информацию по определенному пользователю по его почте")
    @GetMapping("/user/email/{email}")
    public Optional<User> getUserByEmail(@PathVariable(value = "email") String email) {
        return Optional.ofNullable(userService.findByEmail(email));
    }

    @Operation(summary = "Добавление услуги в избранное")
    @PostMapping("/user/{userId}/{uslugaId}/favorite")
    public ResponseEntity<String> addUslugaToFavorites(@PathVariable Long userId, @PathVariable Long uslugaId) {
        Optional<User> optionalUser = userService.findByID(userId);
        Optional<Usluga> optionalUsluga = uslugaService.findByID(uslugaId);
        if (optionalUser.isPresent() && optionalUsluga.isPresent()) {
            User user = optionalUser.get();
            Usluga Usluga = optionalUsluga.get();
            user.getFavoriteUslugas().add(Usluga);
            userService.saveUser(user);
            return ResponseEntity.ok("Usluga added to favorites");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Удалить услугу из избранного")
    @DeleteMapping("/user/{userId}/{uslugaId}/favorite")
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
    public ResponseEntity<List<Usluga>> getFavoriteUslugas(@PathVariable Long userId) {
        Optional<User> optionalUser = userService.findByID(userId);
        if (optionalUser.isPresent()) {
            List<Usluga> favoriteUslugas = optionalUser.get().getFavoriteUslugas();
            return ResponseEntity.ok(favoriteUslugas);
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

    @Operation(summary = "Посмотреть отзывы на определенного мастера")
    @GetMapping("/user/{userId}/reviews")
    public ResponseEntity<?> getReviewsForUser(@PathVariable Long userId) {
        Optional<User> optionalUser = userService.findByID(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Review> reviews = userService.getReviewsForUser(user);
            return ResponseEntity.ok(reviews);
        } else {
            return ResponseEntity.notFound().build();
        }
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
