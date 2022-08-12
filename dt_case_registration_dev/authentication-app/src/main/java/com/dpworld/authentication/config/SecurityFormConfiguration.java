package com.dpworld.authentication.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dpworld.authentication.constants.SpringSecurityConstants;
import com.dpworld.authentication.filter.JwtRequestFilter;
import com.dpworld.authentication.handler.CustomAccessDeniedHandler;
import com.dpworld.authentication.service.StandardAuthenticationProvider;

/**
 * This is used for security configuration.
 * 
 * @author Intech Creative Services Pvt. Ltd.
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource("classpath:security-config.properties")
public class SecurityFormConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private StandardAuthenticationProvider authenticationProvider;

	@Autowired
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;
	
	@Value("${cors.allowed-origins}")
	private String allowedOrigins;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	/**
	 * This method is used for security configuration implemented using JWT only
	 **/

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();
		http.authorizeRequests().antMatchers(SpringSecurityConstants.PERMITTED_URL_LOGIN).permitAll()
				.antMatchers(SpringSecurityConstants.PERMITTED_URL_ROOT).permitAll()
				.antMatchers(SpringSecurityConstants.PERMITTED_URL_INDEX).permitAll()
				.antMatchers(SpringSecurityConstants.PERMITTED_URL_JSON).permitAll()
				.antMatchers(SpringSecurityConstants.PERMITTED_URL_CSS).permitAll()
				.antMatchers(SpringSecurityConstants.PERMITTED_URL_STATIC).permitAll()
				.antMatchers(SpringSecurityConstants.PERMITTED_SWAGGER_URL).permitAll()
				.antMatchers(SpringSecurityConstants.PERMITTED_URL_CASE_REGISTRATION).permitAll()
				.antMatchers(SpringSecurityConstants.PERMITTED_URL_CASE_REGISTRATION_LIST).permitAll()
				.anyRequest().authenticated().and()
				.exceptionHandling().accessDeniedHandler(accessDeniedHandler)
				.authenticationEntryPoint(customAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
		http.headers().frameOptions().disable();
	}

	@Bean
	public JwtRequestFilter authenticationTokenFilterBean() throws Exception {
		return new JwtRequestFilter();
	}

//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("*"));
//		configuration.setAllowedMethods(Arrays.asList("*"));
//		configuration.setAllowedHeaders(Arrays.asList("*"));
//		configuration.setAllowCredentials(true);
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		return source;
//	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
