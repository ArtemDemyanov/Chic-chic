package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPostDTO {
	private String name;
	private String email;
	private String telephone_number;
	private String password;
	private String role;
}