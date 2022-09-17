package com.datn.coworkingspace.security;

import com.datn.coworkingspace.entity.Blacklist;
import com.datn.coworkingspace.service.IBlacklistService;
import com.datn.coworkingspace.service.IUserService;
import com.datn.coworkingspace.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


public class AuthTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Autowired
	private IUserService userService;

	@Autowired
	private IBlacklistService blacklistService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			
			String jwt = parseJwt(request);
			Optional<Blacklist> blacklist = blacklistService.findByToken(jwt);
			if(jwt !=null && jwtUtils.validateJwtToken(jwt) && (blacklist.isPresent() == false)) {
				String username = jwtUtils.getUserNameFromJwtToken(jwt);
				
				UserDetails userDetails = userService.loadUserByUsername(username);
				
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());
				
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			
		} catch(Exception e) {
			logger.error("Cannot set user authentication: {}", e);
		}
		
		filterChain.doFilter(request, response);
		
	}

	private String parseJwt(HttpServletRequest request) {


		String headerAuth = request.getHeader("Authorization");
		
		if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}
		
		return null;
	}

	

}
