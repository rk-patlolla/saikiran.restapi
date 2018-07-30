package com.restapi.app.utils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.restapi.app.pojo.Student;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Value("${jwt.SIGNING_KEY}")
	private String SIGNING_KEY;


	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(Student user) {
		return doGenerateToken(user);
	}

	private String doGenerateToken(Student user) {

		Claims claims = Jwts.claims().setSubject(user.getMobileNo());
		claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority(user.getUserrole())));

		return Jwts.builder().setClaims(claims).setIssuer("http://restapi.com")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 5 * 60 *60 * 1000))
				.signWith(SignatureAlgorithm.HS256, SIGNING_KEY)

				.compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
