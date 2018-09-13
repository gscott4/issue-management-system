package org.jee8ng.security.boundary;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

/*
 * JWTFilter uses @Priority as AUTHENTICATION to get called before others
 */
@Provider
@JWTRequired
@Priority(Priorities.AUTHENTICATION)
public class JWTFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String header = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		// Check if header has the token
		if (header == null || !header.startsWith("Bearer ")) {
			// this results in a 401 response
			throw new NotAuthorizedException("Authorization header must be provided");
		}

		// Get token from the HTTP Authorization header
		String token = header.substring("Bearer".length()).trim();
		System.out.println("token found [" + token + "]");
		String user = getUserIfValid(token);
		System.out.println("user found " + user);
		// Set user in context if required
	}

	private String getUserIfValid(String token) {
		Key key = new SecretKeySpec("secret".getBytes(), "DES");
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			String scope = claims.getBody().get("scope", String.class);
			System.out.println("scope " + scope);
			return claims.getBody().getSubject();
		} catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
			// don't trust the JWT
			throw new NotAuthorizedException("Invalid JWT");
		}
	}
}
