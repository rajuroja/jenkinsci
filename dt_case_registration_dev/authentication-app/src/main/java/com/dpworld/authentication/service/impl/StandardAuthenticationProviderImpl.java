/**
 * 
 */
package com.dpworld.authentication.service.impl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.dpworld.authentication.service.StandardAuthenticationProvider;

/**
 * 
 * @author Intech Creative Services Pvt. Ltd.
 *
 */
@Service
public class StandardAuthenticationProviderImpl implements StandardAuthenticationProvider {

	private static final Logger logger = LoggerFactory.getLogger(StandardAuthenticationProviderImpl.class);

	@Override
	public Authentication authenticate(Authentication authentication) {
		logger.info("Inside Authentication for user {} ", authentication.getName());
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		Authentication auth = new UsernamePasswordAuthenticationToken(username, password, new ArrayList<GrantedAuthority>());
		return auth;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dpworld.tos.auth.service.StandardAuthenticationProvider#supports( java. lang.Class)
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
