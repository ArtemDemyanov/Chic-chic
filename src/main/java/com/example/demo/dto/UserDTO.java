package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String telephoneNumber;
    private String role;
    private byte[] profilePicture;
    private Date createdAt;
    private Date updatedAt;
    private boolean isBanned;
}