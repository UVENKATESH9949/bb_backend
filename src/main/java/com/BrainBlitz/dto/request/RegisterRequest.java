package com.BrainBlitz.dto.request;

import jakarta.validation.constraints.NotNull;

public class RegisterRequest {

	@NotNull
	private String name;
	@NotNull
	private String email;
	@NotNull
	private String password;
	
	public RegisterRequest() {
		super();
	}

	public RegisterRequest(@NotNull String name, @NotNull String email, @NotNull String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
