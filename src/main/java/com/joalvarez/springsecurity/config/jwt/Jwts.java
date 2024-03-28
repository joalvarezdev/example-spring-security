package com.joalvarez.springsecurity.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class Jwts {

	@Value("${jwt.secret-key:top-secret}")
	private String secretKey;
	@Value("${jwt.user-generator:top-secret}")
	private String userGenerator;
	@Value("${jwt.token-expiration:1_800_000}")
	private long expirationTime;

	public String generateToken(Authentication authentication) {
		var algorithm = Algorithm.HMAC256(this.secretKey);

		var username = authentication.getPrincipal().toString();
		var authorities = authentication.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		var currentDate = Instant.now().atZone(ZoneId.systemDefault()).toInstant();
		var expiration = currentDate.plus(this.expirationTime, ChronoUnit.MILLIS);

		return JWT.create()
			.withIssuer(this.userGenerator)
			.withSubject(username)
			.withClaim("authorities", authorities)
			.withClaim("expiration", expiration.toEpochMilli())
			.withIssuedAt(currentDate)
			.withExpiresAt(expiration)
			.withJWTId(UUID.randomUUID().toString())
			.withNotBefore(currentDate)
			.sign(algorithm);
	}

	public void validateToken(String token) {
		var algorithm = Algorithm.HMAC256(this.secretKey);

		try {
			var verifier = JWT.require(algorithm)
				.withIssuer(this.userGenerator)
				.build();

			verifier.verify(token);
		} catch (JWTVerificationException e) {
			System.out.println(e.getMessage());
			System.out.println("Token invalid, not Authorized");
		}
	}

	public String getUsername(String token) {
		return this.decodeToken(token).getSubject();
	}

	public Claim getSpecificClaim(String token, String claim) {
		return this.decodeToken(token).getClaim(claim);
	}

	public Map<String, Claim> getAllClaims(String token) {
		return this.decodeToken(token).getClaims();
	}

	private DecodedJWT decodeToken(String token) {
		return JWT.decode(token);
	}

}