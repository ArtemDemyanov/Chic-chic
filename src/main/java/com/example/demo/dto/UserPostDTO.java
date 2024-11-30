package com.example.demo.dto;

public class UserPostDTO {
	String name;
	String email;
	String telephone_number;
	String password;
	String role;
	
	public UserPostDTO(String name, String email, String telephone_number, String password, String role) {
		super();
		this.name = name;
		this.email = email;
		this.telephone_number = telephone_number;
		this.password = password;
		this.role=role;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone_number() {
		return telephone_number;
	}

	public void setTelephone_number(String telephone_number) {
		this.telephone_number = telephone_number;
	}




	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole(){return role;}
	public void setRole(String role){this.role=role;}

}
