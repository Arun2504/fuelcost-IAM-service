package com.mercedes.fuelcost.calculator.iam.serviceImpl;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.mercedes.fuelcost.calculator.iam.common.ServiceConstants;
import com.mercedes.fuelcost.calculator.iam.dto.UserDTO;
import com.mercedes.fuelcost.calculator.iam.service.AuthenticationService;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthenticaionServiceImpl implements AuthenticationService {

	@Autowired
	Environment environment;

	public String generateUserToken(UserDTO userDTO) throws Exception {

		if (!validateUserIdAndPassword(userDTO.getUserId(), userDTO.getPassword())) {

			throw new AuthenticationException("UserId or Password is not valid");
		}
		Random random = new Random();
		Integer randomWithNextInt = random.nextInt();
		String id = randomWithNextInt.toString();
		String issuer = environment.getProperty(ServiceConstants.AUTH_ISSUER_PROP);
		String subject = environment.getProperty(ServiceConstants.AUTH_SUBJECT_PROP);
		Long ttlMillis = Long.parseLong(environment.getProperty(ServiceConstants.AUTH_SESSION_TIME_PROP));
		String secret = environment.getProperty(ServiceConstants.AUTH_SIGNING_KEY_PROP);
		Map<String, Object> claimsMap = new HashMap<>();
		claimsMap.put("userId", userDTO.getUserId());

		return createJWT(id, issuer, subject, ttlMillis, secret, claimsMap);
	}

	private Boolean validateUserIdAndPassword(String userId, String password) {

		if (userId.equals(environment.getProperty(ServiceConstants.AUTH_USERID_PROP))
				&& password.equals(environment.getProperty(ServiceConstants.AUTH_PASSWORD_PROP))) {
			return true;
		} else {
			return false;
		}

	}

	public static String createJWT(String id, String issuer, String subject, long ttlMillis, String secret,
			Map<String, Object> claimsMap) throws Exception {

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = secret.getBytes(Charset.defaultCharset());
		Date exp = null;
		if (ttlMillis > 0) {
			long expMillis = nowMillis + ttlMillis;
			exp = new Date(expMillis);
		}

		// Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).setIssuer(issuer)
				.addClaims(claimsMap).setIssuedAt(now).setExpiration(exp).signWith(signatureAlgorithm, apiKeySecretBytes);

		return builder.compact();
	}

}
