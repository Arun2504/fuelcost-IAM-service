package com.mercedes.fuelcost.calculator.iam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mercedes.fuelcost.calculator.iam.dto.UserDTO;
import com.mercedes.fuelcost.calculator.iam.service.AuthenticationService;

@Controller
@RequestMapping(value = "iam")
public class AuthenticationController {
	
	@Autowired
	AuthenticationService authenticationService;
	
	@PostMapping(value = "/token/generate")
	public ResponseEntity<String> generateUserToken(@Validated @RequestBody UserDTO userDTO) throws Exception{
		return new ResponseEntity<String>(authenticationService.generateUserToken(userDTO),HttpStatus.OK);
		
	}

}
