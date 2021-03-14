package com.mercedes.fuelcost.calculator.iam.service;

import com.mercedes.fuelcost.calculator.iam.dto.UserDTO;

public interface AuthenticationService {

	/**
	 * @param userDTO
	 * @return
	 * @throws Exception
	 */
	String generateUserToken(UserDTO userDTO) throws Exception;

}
