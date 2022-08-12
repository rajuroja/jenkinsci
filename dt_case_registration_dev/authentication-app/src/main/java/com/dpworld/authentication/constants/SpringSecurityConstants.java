package com.dpworld.authentication.constants;

public class SpringSecurityConstants {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String AUTHORIZATION_BEARER = "Bearer ";
	public static final String PERMITTED_URL_LOGIN = "/login";
	public static final String PERMITTED_URL_INDEX = "/index.html";
	public static final String PERMITTED_URL_JSON = "/*.json";
	public static final String PERMITTED_URL_CSS = "/css/**";
	public static final String PERMITTED_URL_STATIC = "/static/**";
	public static final String PERMITTED_URL_ROOT = "/";
	public static final String[] PERMITTED_SWAGGER_URL = { 
			"/v2/api-docs", 
			"/swagger-resources",
			"/swagger-resources/**", 
			"/configuration/ui", 
			"/configuration/security", 
			"/swagger-ui.html", 
			"/webjars/**",
			"/v3/api-docs/**", "/swagger-ui/**" 
			};
	
	public static final String LOGIN_CONTROLLER = "logincontroller :";
	public static final String PERMITTED_URL_CASE_REGISTRATION = "/guestCaseRegistration/**";
	public static final String PERMITTED_URL_CASE_REGISTRATION_LIST = "/fetchcaseregistration/**";

}
