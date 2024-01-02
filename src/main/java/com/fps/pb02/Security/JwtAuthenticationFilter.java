package com.fps.pb02.Security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Value("${Authorization.UserName}")
	private String userName;
	@Value("${Authorization.PassWord}")
	private String passWord;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if(isValid(request.getHeader(HttpHeaders.AUTHORIZATION))) {
	        	filterChain.doFilter(request, response);

		}
		else throw new ServletException("Access is denied!");
	}
	
	private boolean isValid(String token) {
		String str = HttpServletRequest.BASIC_AUTH.concat(" ").concat(Base64.encodeBase64String((userName + ":" + (new String(Base64.decodeBase64(passWord)))).getBytes()));
		if (str.equalsIgnoreCase(token)) {
			return true;
		}
		return false;
	}
}