package com.dpworld.masterdataapp.utility;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.service.WmsApiService;
import com.dpworld.masterdataapp.webServiceResponse.JsonResponseData;
import com.dpworld.masterdataapp.webServiceResponse.JsonResponseData.Agents;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

@Component
public class TmWmsUtils {

	@Autowired
	private WmsApiService wmsApiService;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Value("${dubaitrade.validate.agent-type-code}")
	private String allowedAgentTypeCode;

	public List<Agents> getImporterAgentsByLoggedInUser(String serviceId) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		JsonNode response = wmsApiService.fetchUserCompanyInfo(auth.getName(), serviceId);
		JsonResponseData data = objectMapper.convertValue(response, JsonResponseData.class);
		if (data.getData() != null && data.getData().getAgentsList() != null && !data.getData().getAgentsList().isEmpty()) {
			return data.getData().getAgentsList().stream().filter(p -> (p.getAgentTypeCode() != null && p.getAgentTypeCode().equalsIgnoreCase(allowedAgentTypeCode))).collect(Collectors.toList());
		}
		return null;
	}

	public List<String> getImporterAgentCodesByLoggedInUser(String serviceId) throws Exception {
		List<Agents> list = getImporterAgentsByLoggedInUser(serviceId);
		if (list != null && !list.isEmpty())
			return list.stream().map(Agents::getAgentCode).collect(Collectors.toList());
		return null;
	}
	
	public boolean validateFileType(MultipartFile file) {
		if (!file.isEmpty()) {
			MagicMatch match = null;
			try {
				match = Magic.getMagicMatch(file.getBytes());
			} catch (MagicParseException | MagicMatchNotFoundException | MagicException | IOException e) {
				return false;
			} 
			if (match == null || !(MasterDataAppConstants.ALLOWEDFILETYPES.contains(FilenameUtils.getExtension(file.getOriginalFilename())))
				|| !(MasterDataAppConstants.ALLOWEDFILEMIMETYPES.contains(match.getMimeType()))
				|| !(file.getContentType().equalsIgnoreCase(match.getMimeType()))) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	public int getPushApiResponseCode(int itemsCount, int removedItemsCount, int successCount, int failedCount) {
		if ((itemsCount == removedItemsCount) || (itemsCount == failedCount)
				|| (removedItemsCount > 0 && failedCount > 0 && (removedItemsCount + failedCount) == itemsCount)) {
			return 300;
		} else if (itemsCount == successCount) {
			return 100;
		} else if ((successCount > 0 && failedCount > 0 && removedItemsCount == 0)
				|| (successCount > 0 && failedCount == 0 && removedItemsCount > 0)
				|| (successCount > 0 && failedCount > 0 && removedItemsCount > 0)) {
			return 200;
		}
		return 0;
	}
}
