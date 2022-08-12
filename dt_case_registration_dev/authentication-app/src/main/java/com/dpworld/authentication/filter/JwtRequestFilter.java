package com.dpworld.authentication.filter;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dpworld.authentication.constants.SpringSecurityConstants;
import com.dpworld.authentication.service.impl.CustomUserDetailsService;
import com.dpworld.authentication.util.JWTUtil;
import com.dpworld.common.utils.MultiReadHttpServletRequestWrapper;
import com.dpworld.common.utils.MultiReadHttpServletResponseWrapper;

/**
 * This filter is used for verifying authorization header from request using JWT
 * implementation.
 *
 */
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String username = null;
		String jwt = null;
		String facilityId = null;

		final String authorizationHeader = request.getHeader(SpringSecurityConstants.AUTHORIZATION_HEADER);

		if (authorizationHeader != null && authorizationHeader.startsWith(SpringSecurityConstants.AUTHORIZATION_BEARER)) {
			jwt = authorizationHeader.substring(7);
			username = jwtUtil.extractUsername(jwt);
		}

		MultiReadHttpServletRequestWrapper wrappedRequest = new MultiReadHttpServletRequestWrapper((HttpServletRequest) request);
		MultiReadHttpServletResponseWrapper wrappedResponse = new MultiReadHttpServletResponseWrapper((HttpServletResponse) response);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (HttpMethod.POST.toString().equalsIgnoreCase(request.getMethod())) {
				String inputString = wrappedRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

				if (inputString.contains("facility_id")) {
					JSONObject jsonObject = new JSONObject(inputString);
					// facilityId=jsonObject.getString("facility_id");
					facilityId = String.valueOf(jsonObject.getInt("facility_id"));

				}
			}
			if (HttpMethod.GET.toString().equalsIgnoreCase(request.getMethod())) {
				facilityId = request.getParameter("facility_id");
			}
			UserDetails userDetails = null;
			if (StringUtils.isEmpty(facilityId)) {
				userDetails = this.userDetailsService.loadUserByUsername(username);
			} else {
				userDetails = this.userDetailsService.getUserByFacilityId(username, facilityId);
			}

			if (jwtUtil.validateToken(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(wrappedRequest, wrappedResponse);
	}

}
