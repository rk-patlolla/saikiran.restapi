package com.restapi.app.filters;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapi.app.exception.TokenError;
import com.restapi.app.utils.CustomDetails;
import com.restapi.app.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private CustomDetails userDetailsService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Value("${jwt.HEADER_STRING}")
	private String HEADER_STRING;
	@Value("${jwt.TOKEN_PREFIX}")
	private String TOKEN_PREFIX;
	@Autowired
	MessageSource errormessage;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader(HEADER_STRING);
		String username = null;
		String authToken = null;
		if (header != null && header.startsWith(TOKEN_PREFIX)) {
			authToken = header.replace(TOKEN_PREFIX, "");
			try {
				username = jwtTokenUtil.getUsernameFromToken(authToken);
			} catch (IllegalArgumentException e) {
				logger.error("an error occured during getting username from token", e);
				TokenError error = new TokenError(errormessage.getMessage("msg.error", null, Locale.US));
				byte[] responseToSend = restResponseBytes(error);
				((HttpServletResponse) response).setHeader("Content-Type", "application/json");
				((HttpServletResponse) response).setStatus(401);
				response.getOutputStream().write(responseToSend);
				return;
			} catch (ExpiredJwtException e) {
				logger.warn("the token is expired and not valid anymore", e);
				TokenError error = new TokenError(errormessage.getMessage("msg.token.expire", null, Locale.US));
				byte[] responseToSend = restResponseBytes(error);
				((HttpServletResponse) response).setHeader("Content-Type", "application/json");
				((HttpServletResponse) response).setStatus(401);
				response.getOutputStream().write(responseToSend);
				return;

			} catch (SignatureException e) {
				logger.error("Authentication Failed. Username or Password not valid.");
			}
		} else {
			logger.warn("couldn't find bearer string, will ignore the header");
		}
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			if (jwtTokenUtil.validateToken(authToken, userDetails)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				logger.info("authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response);
	}

	private byte[] restResponseBytes(TokenError error) throws IOException {
		String serialized = new ObjectMapper().writeValueAsString(error);
		return serialized.getBytes();
	}

}
