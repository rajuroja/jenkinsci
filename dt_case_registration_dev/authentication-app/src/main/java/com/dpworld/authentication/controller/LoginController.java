package com.dpworld.authentication.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dpworld.authentication.model.AuthenticationRequest;
import com.dpworld.authentication.model.AuthenticationResponse;
import com.dpworld.authentication.service.StandardAuthenticationProvider;
import com.dpworld.authentication.service.UserDetailsService;
import com.dpworld.authentication.util.JWTUtil;
import com.dpworld.common.model.ApiResponse;
import com.dpworld.persistence.entity.UserDetails;

@RestController
public class LoginController {

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private StandardAuthenticationProvider authenticationManager;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * This method is for login user. This method getting user's credentials from AuthenticationRequest and set user name
	 * and token in AuthenticationResponse if user is authenticated.
	 * 
	 * @param AuthenticationRequest is class with username, companyId and license number property.
	 * @return ApiResponse write AuthenticationResponse in JSON response.
	 */
	
	@PostMapping("/login")
	public ApiResponse<AuthenticationResponse> getToken(@Valid @RequestBody AuthenticationRequest authRequest) {

		//logger.info("ENTRY:: getToken:: Authentication request for username:{}", authRequest.getUsername());
		try {

			UserDetails userDetail = userDetailsService.addOrFetchUserDetail(authRequest);
			String dummyPassword = userDetail.getCompanyId() + "<<<>>>" + userDetail.getLicenseNumber();

			Authentication authenticatedUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetail.getUsername(), dummyPassword));
			ApiResponse<AuthenticationResponse> response = new ApiResponse<>(Boolean.TRUE, HttpStatus.OK.name(), "Login Success");
			AuthenticationResponse authResponse = new AuthenticationResponse();
			authResponse.setUsername(userDetail.getUsername());
			authResponse.setToken(jwtUtil.generateToken(authenticatedUser));
			authResponse.setLastLoginTime(userDetail.getLastLoginTime());
			response.setData(authResponse);
			//logger.info("EXIT:: getToken:: Authentication request for username : {}", authRequest.getUsername());
			return response;
		} catch (Exception e) {
			logger.error("ERROR:: getToken:: Error occured while generation token : {}", e);
			return new ApiResponse<>(true, HttpStatus.INTERNAL_SERVER_ERROR.name(), "Error in token generation.");
		}
	}

}
