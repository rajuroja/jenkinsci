package com.dpworld.common.utils;

import java.util.Base64;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.dpworld.common.model.RequestDetail;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class WebserviceUtility {

	private static final Logger logger = LoggerFactory.getLogger(WebserviceUtility.class);

	@Autowired
	private RestTemplate restTemplate;

	public JsonNode callPostWebServiceRequest(RequestDetail<?> requestDetail, Map<String, String> extraHeaders, String mediaType) throws RestClientException {

		//logger.info("ENTRY:: callPostWebServiceRequest:: Post service request.");

		HttpHeaders headers = createHeaders(requestDetail.getUsername(), requestDetail.getPassword());
		headers.set("content-type", mediaType);
		if (extraHeaders != null && !extraHeaders.isEmpty()) {
			for (Entry<String, String> entry : extraHeaders.entrySet())
				headers.set(entry.getKey(), entry.getValue());
		}
		HttpEntity<?> request = new HttpEntity<>(requestDetail.getRequestBody(), headers);
		
		try {
			ResponseEntity<JsonNode> response = restTemplate.postForEntity(requestDetail.getUrl(), request, JsonNode.class);
			//logger.info("EXIT:: callPostWebServiceRequest:: Post service request. response={}", response.getBody());
			return response.getBody();
		} catch (RestClientException e) {
			logger.error("ERROR:: callPostWebServiceRequest:: Error occured while post service request. error={}", e.getMessage());
			throw e;
		}
	}
	
	@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 10000))
	public String callPostWebServiceRequestString(RequestDetail<?> requestDetail, Map<String, String> extraHeaders, String mediaType) throws RestClientException {
		
		//logger.info("ENTRY:: callPostWebServiceRequest:: Post service request.");
		
		HttpHeaders headers = createHeaders(requestDetail.getUsername(), requestDetail.getPassword());
		headers.set("content-type", mediaType);
		if (extraHeaders != null && !extraHeaders.isEmpty()) {
			for (Entry<String, String> entry : extraHeaders.entrySet())
				headers.set(entry.getKey(), entry.getValue());
		}
		HttpEntity<?> request = new HttpEntity<>(requestDetail.getRequestBody(), headers);
		
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(requestDetail.getUrl(), request, String.class);
			//logger.info("EXIT:: callPostWebServiceRequest:: Post service request. \n response = {}", response.getBody());
			return response.getBody();
		} catch (RestClientException e) {
			logger.error("ERROR:: callPostWebServiceRequest:: Error occured while post service request. error={}", e);
			throw e;
		}
	}

	public JsonNode callGETWebServiceRequest(RequestDetail<?> requestDetail) throws RestClientException {

		//logger.info("ENTRY:: callGETWebServiceRequest:: Get service request.");

		try {
			HttpHeaders headers = createHeaders(requestDetail.getUsername(), requestDetail.getPassword());
			HttpEntity<String> request = new HttpEntity<String>(headers);
			ResponseEntity<JsonNode> response = restTemplate.exchange(requestDetail.getUrl(), HttpMethod.GET, request, JsonNode.class);
			//logger.info("EXIT:: callGETWebServiceRequest:: Get service request. response={}", response.getBody());
			return response.getBody();
		} catch (RestClientException e) {
			logger.error("ERROR:: callGETWebServiceRequest:: Error occured while get service request. error={}", e);
			throw e;
		}
	}

	private static HttpHeaders createHeaders(String username, String password) {
		return new HttpHeaders() {
			private static final long serialVersionUID = 1L;
			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
				String authHeader = "Basic " + new String(encodedAuth);
				set("Authorization", authHeader);
			}
		};
	}

}
