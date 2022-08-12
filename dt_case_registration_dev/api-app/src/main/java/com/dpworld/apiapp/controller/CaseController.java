package com.dpworld.apiapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dpworld.apiapp.service.CaseService;
import com.dpworld.common.model.ApiResponse;
import com.dpworld.persistence.model.search.SResponse;

@RestController
@RequestMapping("/fetchcaseregistration")
public class CaseController {
	
	@Autowired
	CaseService caseService;

	@GetMapping("/getlovs")
	public ApiResponse<SResponse> getCaseLOVs() {
		try {
			List<Map<String,Object>> list =new ArrayList<>();
			list.add(caseService.getCaseLOVs());
			return new ApiResponse<>(Boolean.TRUE, HttpStatus.OK.name(), "Case Type", new SResponse(list));
		} catch (Exception e) {
			return new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.name(), "Failed-" + e.getMessage());
		}
	}
	
	@GetMapping("/getComplainTypelovs")
	public ApiResponse<SResponse> getComplainTypeLov() {
		try {
			List<Map<String,Object>> list =new ArrayList<>();
			list.add(caseService.getComplainTypeLov());
			return new ApiResponse<>(Boolean.TRUE, HttpStatus.OK.name(), "Complain Type", new SResponse(list));
		} catch (Exception e) {

			return new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.name(), "Failed-" + e.getMessage());
		}
	}
	
	@GetMapping("/getPortTypelovs")
	public ApiResponse<SResponse> getPortTypeLovs(@RequestParam int caseType) {
		try {
			List<Map<String,Object>> list =new ArrayList<>();
			list.add(caseService.getPortTypeLovs(caseType));
			return new ApiResponse<>(Boolean.TRUE, HttpStatus.OK.name(), "Port type", new SResponse(list));
		} catch (Exception e) {

			return new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.name(), "Failed-" + e.getMessage());
		}
	}
	
	@GetMapping("/getTerminallovs")
	public ApiResponse<SResponse> getTerminalLovs(@RequestParam int portType) {
		try {
			List<Map<String,Object>> list =new ArrayList<>();
			list.add(caseService.getTerminalLovs(portType));
			return new ApiResponse<>(Boolean.TRUE, HttpStatus.OK.name(), "Terminal type", new SResponse(list));
		} catch (Exception e) {
			return new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.name(), "Failed-" + e.getMessage());
		}
	}

}
