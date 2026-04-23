package com.BrainBlitz.dto.request;

import jakarta.validation.constraints.NotNull;

public class ResetPasswordRequest {

	@NotNull
	private String email;
	
	public ResetPasswordRequest(@NotNull String email) {
		// TODO Auto-generated constructor stub
		this.email = email;
	}

	public ResetPasswordRequest() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
