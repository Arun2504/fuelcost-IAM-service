package com.mercedes.fuelcost.calculator.iam.dto;

import org.springframework.lang.NonNull;

import lombok.Data;

@Data
public class UserDTO {
	
	@NonNull
	private String userId;
	@NonNull
	private String password;

}
