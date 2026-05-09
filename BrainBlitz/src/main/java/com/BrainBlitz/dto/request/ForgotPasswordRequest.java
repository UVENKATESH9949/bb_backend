package com.BrainBlitz.dto.request;

import jakarta.validation.constraints.NotNull;

public class ForgotPasswordRequest {

	@NotNull
	private String email;

	public ForgotPasswordRequest(@NotNull String email) {
		super();
		this.email = email;
	}

	public ForgotPasswordRequest() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
