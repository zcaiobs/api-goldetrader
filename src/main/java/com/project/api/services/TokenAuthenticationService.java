package com.project.api.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.stereotype.Service;

@Service
public class TokenAuthenticationService {

	private Algorithm algorithm = Algorithm.HMAC256("MySecret");

	public String addAuthentication(String email, long timeExpires) {
		try {
			
			String token = JWT.create()
								.withIssuer("Auth0")
								.withKeyId(email)
								.withIssuedAt(new Date())
								.withExpiresAt( Date.from(
									LocalDateTime.now().plusMinutes(timeExpires)
										.atZone(ZoneId.systemDefault())
										.toInstant()))
								.sign(algorithm);
			return token;
		} catch (Exception e) {
			System.out.println("Error! " + e);
			return "Error! "+ e;
		}
	}

	public String verifyAuthentication(String token) {
		try {
			JWTVerifier jwtVerifier = JWT.require(algorithm)
											.withIssuer("Auth0")
											.build();

			DecodedJWT jwt = jwtVerifier.verify(token); 
			return jwt.getKeyId();
		} catch(Exception e) {
			System.out.println("Error! " + e);
			return "Error";
		}

	}

}
