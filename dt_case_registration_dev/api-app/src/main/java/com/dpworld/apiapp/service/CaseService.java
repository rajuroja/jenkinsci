package com.dpworld.apiapp.service;

import java.util.Map;

public interface CaseService {
	public Map<String, Object> getCaseLOVs() throws Exception; 
	
	public Map<String, Object> getComplainTypeLov() throws Exception; 

	public Map<String, Object> getPortTypeLovs(int caseType) throws Exception;
	public Map<String, Object> getTerminalLovs(int portType) throws Exception;

}
