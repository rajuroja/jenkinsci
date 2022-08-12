package com.dpworld.masterdataapp.controller;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dpworld.common.model.ApiResponse;
import com.dpworld.masterdataapp.model.request.ValidateUserServiceRequest;
import com.dpworld.masterdataapp.service.WmsApiService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

	private static Logger logger = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	private WmsApiService wmsApiService;

	@Value("${dubaitrade.validate.serviceId}")
	private String dubaitradeServiceId;

	@Value("${dubaitrade.api.url.validate_user_service}")
	private String dubaitradeValidateUserServiceUrl;

	@Autowired
	private HttpSession httpSession;

	@GetMapping("/service")
	public ApiResponse<?> validateService(@RequestParam(name = "serviceId", required = false) String serviceId) throws Exception {

		//logger.info("ENTRY:: validateService:: Validate user service with serviceId: {}", serviceId);
		JsonNode response = null;
		httpSession.setAttribute("serviceId", serviceId);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (serviceId == null || serviceId.trim().isEmpty())
			return new ApiResponse<>(FALSE, HttpStatus.UNAUTHORIZED.name(), "Invalid Service ID");

		if (serviceId.equals(dubaitradeServiceId))
			response = wmsApiService.validateUserService(new ValidateUserServiceRequest(auth.getName(), dubaitradeServiceId, dubaitradeValidateUserServiceUrl));
		else
			return new ApiResponse<>(FALSE, HttpStatus.UNAUTHORIZED.name(), "Invalid Service ID");

		if (response == null || response.toString().trim().isEmpty() || response.toString().equals("{}"))
			return new ApiResponse<>(FALSE, HttpStatus.UNAUTHORIZED.name(), "UserName Not Registered with Dubai Trade");

		String code = response.get("code").asText();
		if (!code.equalsIgnoreCase("DT-00001")) {
			//logger.info("EXIT:: validateService:: Validated user service. response={}", response);
			return new ApiResponse<>(FALSE, HttpStatus.UNAUTHORIZED.name(), response.get("error").asText());
		}

		//logger.info("EXIT:: validateService:: Validated user service. response={}", response);
		return new ApiResponse<>(TRUE, HttpStatus.OK.name(), response.get("message").asText());
	}

}
