package com.example.demo.mapper;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserPostDTO;
import com.example.demo.model.User;

public class UserMapper {

    // Convert User entity to UserDTO
    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setTelephoneNumber(user.getTelephoneNumber());
        dto.setRole(user.getRole());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    // Convert UserPostDTO to User entity
    public static User toEntity(UserPostDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setTelephoneNumber(dto.getTelephone_number());
        user.setRole(dto.getRole());
        return user;
    }

    // Convert UserDTO to User entity
    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setTelephoneNumber(dto.getTelephoneNumber());
        user.setRole(dto.getRole());
        user.setProfilePicture(dto.getProfilePicture());
        return user;
    }
}