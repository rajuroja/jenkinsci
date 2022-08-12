package com.dpworld.masterdataapp.service.impl;
 
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
 
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
 
import com.dpworld.common.model.RequestDetail;
import com.dpworld.common.utils.CustomException;
import com.dpworld.common.utils.Utils;
import com.dpworld.common.utils.WebserviceUtility;
import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.model.request.ValidateUserServiceRequest;
import com.dpworld.masterdataapp.pushBarcode.BarcodePushRequestRoot;
import com.dpworld.masterdataapp.pushBarcode.BarcodePushResponseRoot;
import com.dpworld.masterdataapp.pushInbound.InboundPushRequestRoot;
import com.dpworld.masterdataapp.pushInbound.InboundPushResponseRoot;
import com.dpworld.masterdataapp.pushOutbound.OutboundPushRequestRoot;
import com.dpworld.masterdataapp.pushOutbound.OutboundPushResponseRoot;
import com.dpworld.masterdataapp.pushReturnGoods.ReturnGoodsPushRequestRoot;
import com.dpworld.masterdataapp.pushReturnGoods.ReturnGoodsPushResponseRoot;
import com.dpworld.masterdataapp.pushReturnLocalGoods.ReturnLocalGoodsPushRequestRoot;
import com.dpworld.masterdataapp.pushReturnLocalGoods.ReturnLocalGoodsPushResponseRoot;
import com.dpworld.masterdataapp.service.WmsApiService;
import com.dpworld.masterdataapp.syncHsCode.SyncHsCodeRequestRoot;
import com.dpworld.masterdataapp.syncHsCode.SyncHsCodeResponseRoot;
import com.fasterxml.jackson.databind.JsonNode;
 
@Service
public class WmsApiServiceImpl implements WmsApiService {
 
    private static final Logger logger = LoggerFactory.getLogger(WmsApiServiceImpl.class);
 
    @Autowired
    private WebserviceUtility webserviceUtility;
 
    @Value("${dpwms.api.username}")
    private String usernameDpwms;
 
    @Value("${dpwms.api.password}")
    private String passwordDpwms;
 
    @Value("${dpwms.api.url.hs_code}")
    private String hsCodeUrl;
 
    @Value("${dpwms.api.url.sequence}")
    private String sequenceUrl;
 
    @Value("${dpwms.api.url.push_inbound}")
    private String pushInboundUrl;
 
    @Value("${dpwms.api.url.push_return_goods}")
    private String pushReturnGoodsUrl;
    
    @Value("${dpwms.api.url.push_return_local_goods}")
    private String pushReturnLocalGoodsUrl;
    
    @Value("${dpwms.api.url.push_outbound}")
    private String pushOutboundUrl;
    
    @Value("${dpwms.api.url.push_barcode}")
    private String pushBarcodeUrl;
    
    @Value("${dubaitrade.api.username}")
    private String usernameDubaiTrade;
 
    @Value("${dubaitrade.api.password}")
    private String passwordDubaiTrade;
    
    @Value("${dubaitrade.api.acceess_key}")
    private String accessKeyDubaiTrade;
    
    @Value("${dubaitrade.validate.serviceId}")
    private String dubaitradeServiceId;
    
    @Value("${dubaitrade.api.url.fetch_company_info}")
    private String dubaitradeFetchUserCompanyInfoUrl;
 
    @Override
    public JsonNode fetchUserCompanyInfo(String username, String serviceId) throws CustomException, Exception {
 
        //logger.info("ENTRY:: fetchUserCompanyInfo:: Fetch User company info from dt portal. username={}", username);
 
        RequestDetail<Map<String, String>> details = new RequestDetail<Map<String, String>>();
        details.setUsername(usernameDubaiTrade);
        details.setPassword(passwordDubaiTrade);
        if (serviceId == null || serviceId.trim().isEmpty()) {
            logger.error("ERROR:: fetchUserCompanyInfo:: Error occured while fetching user company info from dt portal : {}", MasterDataAppConstants.SERVICE_ID_NOT_AVAILABLE);
            throw new CustomException(MasterDataAppConstants.SERVICE_ID_NOT_AVAILABLE);
        } else if (serviceId.equals(dubaitradeServiceId)) {
            details.setUrl(dubaitradeFetchUserCompanyInfoUrl);
        } else {
            logger.error("ERROR:: fetchUserCompanyInfo:: Error occured while fetching user company info from dt portal : {}", MasterDataAppConstants.INVALID_SERVICE_ID);
            throw new CustomException(MasterDataAppConstants.INVALID_SERVICE_ID);
        }
 
        Map<String, String> requestBody = new HashMap<>(), headers = new HashMap<>();
        requestBody.put("userName", username);
        details.setRequestBody(requestBody);
        headers.put("X-DTUM-Access-Key", accessKeyDubaiTrade);
        try {
            JsonNode response = webserviceUtility.callPostWebServiceRequest(details, headers, MediaType.APPLICATION_JSON);
            String code = response.get("code").asText();
            
            if (!code.equalsIgnoreCase("DT-00001"))
                throw new CustomException(response.get("error").asText());
            
            //logger.info("EXIT:: fetchUserCompanyInfo:: Fetched User company info from dt portal. response={}", response);
            return response;
        } catch (Exception e) {
            logger.error("ERROR:: fetchUserCompanyInfo:: Error occured while fetching user company info from dt portal. error={}", e.getMessage());
            throw e;
        }
    }
 
    @Override
    public JsonNode validateUserService(ValidateUserServiceRequest request) throws Exception {
        
        //logger.info("ENTRY:: validateUserService:: Fetch user service validation from dt portal. request={}", request);
        
        RequestDetail<Map<String, String>> requestDetails = new RequestDetail<Map<String, String>>();
        requestDetails.setUsername(usernameDubaiTrade);
        requestDetails.setPassword(passwordDubaiTrade);
        requestDetails.setUrl(request.getUrl());
 
        Map<String, String> requestBody = new HashMap<>(), headers = new HashMap<>();
        requestBody.put("userName", request.getUserName());
        requestBody.put("serviceId", request.getServiceId());
        requestDetails.setRequestBody(requestBody);
        headers.put("X-DTUM-Access-Key", accessKeyDubaiTrade);
 
        try {
            JsonNode response = webserviceUtility.callPostWebServiceRequest(requestDetails, headers, MediaType.APPLICATION_JSON);
            //logger.info("EXIT:: validateUserService:: Fetched user service validation from dt portal. response={}", response);
            return response;
        } catch (Exception e) {
            logger.error("ERROR:: validateUserService:: Error occured while fetching user service validation from dt portal. error={}", e);
            throw new Exception(MasterDataAppConstants.WMS_API_VALIDATING_USER_SERVICE_ERROR);
        }
    }
 
    @Override
    public SyncHsCodeResponseRoot getHsCodeMasterData(SyncHsCodeRequestRoot requestRoot) throws CustomException {
        
        logger.info("ENTRY:: getHsCodeMasterData:: Fetch Hscode : request = {}", requestRoot);
        
        RequestDetail<SyncHsCodeRequestRoot> details = new RequestDetail<>();
        details.setUsername(usernameDpwms);
        details.setPassword(passwordDpwms);
        details.setUrl(hsCodeUrl);
        details.setRequestBody(requestRoot);
        
        try {
            String result = webserviceUtility.callPostWebServiceRequestString(details, null, MediaType.APPLICATION_XML);
            JAXBContext jaxbContext = JAXBContext.newInstance(SyncHsCodeResponseRoot.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            logger.info("EXIT:: getHsCodeMasterData:: Fetched Hscode : response = {}", result);
            return (SyncHsCodeResponseRoot) jaxbUnmarshaller.unmarshal(new StringReader(result.toString()));
        } catch (Exception e) {
            logger.error("ERROR:: getHsCodeMasterData:: Error occured while fetching hscode from dp wms. error={}", e);
            throw new CustomException(MasterDataAppConstants.WMS_API_FETCH_HSCODE_ERROR);
        }
    }
 
    @Override
    public String getReferenceCode(String type) throws Exception {
 
        //logger.info("ENTRY:: getReferenceCode:: Fetch Reference code from dp wms. type={}", type);
        
        RequestDetail<?> details = new RequestDetail<>();
        details.setUsername(usernameDpwms);
        details.setPassword(passwordDpwms);
        details.setUrl(sequenceUrl + type);
        
        try {
            JsonNode response = webserviceUtility.callGETWebServiceRequest(details);
            String str = response.get("sequnce_Number").asText();
            if (str.equals("-1")) {
                logger.error("ERROR:: getReferenceCode:: Error occured while fetching Reference code from dp wms. error={}", response.get("status").asText());
                throw new CustomException(response.get("status").asText());
            }
            //logger.info("EXIT:: getReferenceCode:: Fetched Reference code from dp wms.");
            return str;
        } catch (Exception e) {
            logger.error("ERROR:: getReferenceCode:: Error occured while fetching Reference code from dp wms. error={}", e);
            throw new Exception(MasterDataAppConstants.WMS_API_REFERENCE_NO_ERROR);
        }
    }
 
    @Override
    public InboundPushResponseRoot inboundPushData(InboundPushRequestRoot updateRequest) throws CustomException {
 
        logger.info("ENTRY:: inboundPushData:: Inbound Push Data. {}, {}, {}, {}", System.lineSeparator(), Utils.objectToXml(updateRequest), System.lineSeparator(), pushInboundUrl);
 
        RequestDetail<InboundPushRequestRoot> details = new RequestDetail<>();
        details.setUsername(usernameDpwms);
        details.setPassword(passwordDpwms);
        details.setUrl(pushInboundUrl);
        details.setRequestBody(updateRequest);
 
        try {
            String response = webserviceUtility.callPostWebServiceRequestString(details, null, MediaType.APPLICATION_XML);
            JAXBContext jaxbContext = JAXBContext.newInstance(InboundPushResponseRoot.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
			xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
			xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new StringReader(response.toString()));
            logger.info("EXIT:: inboundPushData:: Inbound Push Data.");
			return (InboundPushResponseRoot) jaxbUnmarshaller.unmarshal(xmlStreamReader);
        } catch (Exception e) {
            logger.error("ERROR:: inboundPushData:: Error occured while Inbound Push Data. error={}", e);
            throw new CustomException(e.getMessage());
        }
    }
 
    @Override
    public ReturnGoodsPushResponseRoot returnGoodsPushData(ReturnGoodsPushRequestRoot root) throws CustomException {
        
        logger.info("ENTRY:: returnGoodsPushData:: returnGoods Push Data : {}, {}, {}, {}", System.lineSeparator(), Utils.objectToXml(root), System.lineSeparator(), pushReturnGoodsUrl);
        
        RequestDetail<ReturnGoodsPushRequestRoot> details = new RequestDetail<>();
        details.setUsername(usernameDpwms);
        details.setPassword(passwordDpwms);
        details.setUrl(pushReturnGoodsUrl);
        details.setRequestBody(root);
        
        try {
            String response = webserviceUtility.callPostWebServiceRequestString(details, null, MediaType.APPLICATION_XML);
            JAXBContext jaxbContext = JAXBContext.newInstance(ReturnGoodsPushResponseRoot.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
			xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
			xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new StringReader(response.toString()));
			logger.info("EXIT:: returnGoodsPushData:: returnGoods Push Data.");
			return (ReturnGoodsPushResponseRoot) jaxbUnmarshaller.unmarshal(xmlStreamReader);
        } catch (Exception e) {
            logger.error("ERROR:: returnGoodsPushData:: Error occured while returnGoods Push Data. error={}", e);
            throw new CustomException(e.getMessage());
        }
    }
    
    @Override
    public ReturnLocalGoodsPushResponseRoot returnLocalGoodsPushData(ReturnLocalGoodsPushRequestRoot root) throws CustomException {
        
        logger.info("ENTRY:: returnLocalGoodsPushData:: returnLocalGoods Push Data. {}, {}, {}, {}", System.lineSeparator(), Utils.objectToXml(root), System.lineSeparator(), pushReturnLocalGoodsUrl);
        
        RequestDetail<ReturnLocalGoodsPushRequestRoot> details = new RequestDetail<>();
        details.setUsername(usernameDpwms);
        details.setPassword(passwordDpwms);
        details.setUrl(pushReturnLocalGoodsUrl);
        details.setRequestBody(root);
        
        try {
            String response = webserviceUtility.callPostWebServiceRequestString(details, null, MediaType.APPLICATION_XML);
            JAXBContext jaxbContext = JAXBContext.newInstance(ReturnLocalGoodsPushResponseRoot.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
			xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
			xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new StringReader(response.toString()));
			
			logger.info("EXIT:: returnLocalGoodsPushData:: returnGoods Push Data.");
			return (ReturnLocalGoodsPushResponseRoot) jaxbUnmarshaller.unmarshal(xmlStreamReader);
        } catch (Exception e) {
            logger.error("ERROR:: returnGoodsPushData:: Error occured while returnGoods Push Data. error={}", e);
            throw new CustomException(e.getMessage());
        }
    }
 
    @Override
    public OutboundPushResponseRoot outboundPushData(OutboundPushRequestRoot updateRequest) throws CustomException {
        
        logger.info("ENTRY:: outboundPushData:: Outbound Push Data, {}, {}, {}, {}", System.lineSeparator(), Utils.objectToXml(updateRequest), System.lineSeparator(), pushOutboundUrl);
 
        RequestDetail<OutboundPushRequestRoot> details = new RequestDetail<>();
        details.setUsername(usernameDpwms);
        details.setPassword(passwordDpwms);
        details.setUrl(pushOutboundUrl);
        details.setRequestBody(updateRequest);
        
        try {
            String response = webserviceUtility.callPostWebServiceRequestString(details, null, MediaType.APPLICATION_XML).replace("&lt;", "<").replace("&amp;lt;", "<").replace("&gt;", ">")
                    .replace("&amp;gt;", ">");
            JAXBContext jaxbContext = JAXBContext.newInstance(OutboundPushResponseRoot.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
			xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
			xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new StringReader(response.toString()));
			
			logger.info("EXIT:: outboundPushData:: Outbound Push Data.");
			return (OutboundPushResponseRoot) jaxbUnmarshaller.unmarshal(xmlStreamReader);
        } catch (Exception e) {
            logger.error("ERROR:: outboundPushData:: Error occured while Outbound Push Data. error={}", e);
            throw new CustomException(e.getMessage());
        }
    }
 
    @Override
    public BarcodePushResponseRoot barCodePushData(BarcodePushRequestRoot root) throws CustomException {
        
        logger.info("ENTRY:: barCodePushData:: BarCode Push Data. {}, {}, {}, {}", System.lineSeparator(), Utils.objectToXml(root), System.lineSeparator(), pushBarcodeUrl);
        
        RequestDetail<BarcodePushRequestRoot> details = new RequestDetail<>();
        details.setUsername(usernameDpwms);
        details.setPassword(passwordDpwms);
        details.setUrl(pushBarcodeUrl);
        details.setRequestBody(root);
        try {
            String response = webserviceUtility.callPostWebServiceRequestString(details, null, MediaType.APPLICATION_XML);
            JAXBContext jaxbContext = JAXBContext.newInstance(BarcodePushResponseRoot.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
			xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
			xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new StringReader(response.toString()));
			logger.info("EXIT:: barCodePushData:: barCode Push Data.");
			return (BarcodePushResponseRoot) jaxbUnmarshaller.unmarshal(xmlStreamReader);
            
        } catch (Exception e) {
            logger.error("ERROR:: barCodePushData:: Error occured while barCode Push Data. error={}", e);
            throw new CustomException(e.getMessage());
        }
    }
 
}