package com.BrainBlitz.dto.request;

import jakarta.validation.constraints.NotNull;

public class VerifyOtpRequest {

	@NotNull
	private String email;
	
	@NotNull
	private int otp;

	public VerifyOtpRequest(@NotNull String email, @NotNull int otp) {
		super();
		this.email = email;
		this.otp = otp;
	}

	public VerifyOtpRequest() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
		this.otp = otp;
	}
	
	
}
