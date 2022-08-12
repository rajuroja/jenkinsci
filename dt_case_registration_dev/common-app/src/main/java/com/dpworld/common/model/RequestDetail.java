package com.dpworld.common.model;

import java.util.Map;

import org.springframework.util.MultiValueMap;

public class RequestDetail<T> {

	private String url;
	private Map<String, Object> queryParams;
	private T requestBody;
	private String username;
	private String password;

	private MultiValueMap<String, String> map;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, Object> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Map<String, Object> queryParams) {
		this.queryParams = queryParams;
	}

	public T getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(T requestBody) {
		this.requestBody = requestBody;
	}

	public MultiValueMap<String, String> getMap() {
		return map;
	}

	public void setMap(MultiValueMap<String, String> map) {
		this.map = map;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "RequestDetail [url=" + url + ", queryParams=" + queryParams + ", requestBody=" + requestBody + ", username="
				+ username + ", password=" + password + ", map=" + map + "]";
	}

}
