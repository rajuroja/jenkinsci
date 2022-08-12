package com.dpworld.masterdataapp.constants;

import java.util.Arrays;
import java.util.List;

public class MasterDataAppConstants {
	
	public static final List<String> ALLOWEDFILETYPES = Arrays.asList("pdf", "jpg", "jpeg", "png", "PDF", "JPG", "JPEG",
			"PNG", "GIF", "gif", "TIFF", "tiff", "bmp", "BMP", "DOC", "doc", "xls", "XLS");
	public static final List<String> ALLOWEDFILEMIMETYPES = Arrays.asList("image/jpeg", "image/png", "application/pdf","image/bmp","application/msword"
			+ "image/gif","image/tiff", "application/vnd.ms-excel");

	public static final String FAILED = "Failed-";
	public static final String SUCCESS = "Success";
	public static final String UNAUTHORIZED = "User is not authorized to access service.";
	
	public static final String JPG = "jpg";
	public static final String JPEG = "jpeg";
	public static final String PDF = "pdf";
	public static final String PNG = "png";
	public static final String TXT = "txt";
	public static final String EXCEL = "excel";
	
	public static final String BASE64PREFIXPDF = "data:application/pdf;base64,";
	public static final String BASE64PREFIXJPEG = "data:image/jpeg;base64,";
	public static final String BASE64PNGMIMEPREFIX = "data:image/png;base64,";
	public static final String BASE64DOCMIMEPREFIX = "data:application/msword;base64,";
	public static final String BASE64DOCXMIMEPREFIX = "data:application/vnd.openxmlformats-officedocument.wordprocessingml.document;base64,";
	public static final String BASE64XLSXMIMEPREFIX = "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,";
	
	public static final String FILE_NOT_FOUND = "File not found";
	public static final String ERROR_OCCURRED_WHILE_READING_FILE = "Error occurred while reading file";
	public static final String INVALID_SERVICE_ID = "Invalid ServiceID";
	public static final String SERVICE_ID_NOT_AVAILABLE = "ServiceID Not Available";
	public static final String LIST_INVENTORY_FAILED="List Inventory Failed";
	
	public static final String NO_AGENT_CODES_FOUND = "No Agent codes found";
	
	public static final String RETAIL = "Retail";
	public static final String WHOLESALE = "Wholesale";
	public static final String EXPORT = "Export";
	
	public static final String CUSTOM_DECLARATION = "custom_declaration";
	public static final String CERTIFICATE_OF_ORIGIN = "certificate_of_origin";
	public static final String COMMERCIAL_INVOICE = "commercial_invoice";
	public static final String PACKING_LIST = "packing_list";
	public static final String SALES_INVOICE = "sales_invoice";
	
	public static final String INBOUND_ADD_SUCCESS = "Inbound added successfully.";
	public static final String INBOUND_UPDATE_SUCCESS = "Inbound updated successfully.";
	public static final String INBOUND_CANCEL_SUCCESS = "Inbound cancelled successfully.";
	public static final String INBOUND_CANCEL_FAILED = "Inbound cancel failed.";
	public static final String INBOUND_FETCH_FAILED = "Inbound fetch failed.";
	public static final String INBOUND_ONLOAD_FAILED = "Inbound failed to load onload data";
	public static final String INBOUND_UPLOAD_DATA_DELETED_SUCCESS = "Inbound Upload document cancelled successfully ";
	public static final String INBOUND_UPLOADDOC_NOT_FOUND = "Inbound Upload document details not found. ";
	public static final String INBOUND_DETAILS_NOT_FOUND = "Inbound detail not found.";
	public static final String INBOUND_RETRIEVAL_SUCCESS = "Inbound retrieved successfully.";
	public static final String INBOUND_ONLOAD_DATA_RETRIEVAL_SUCCESS = "Inbound Onload Data retrieved successfully.";
	public static final String INBOUND_VALIDATION_ERROR = "Inbound validation error message";
	public static final String INBOUND_JSON_ERROR = "Inbound json convertion error message.";
	public static final String INBOUND_CAN_NOT_BE_CANCELLED_AFTER_30DAYS = "Inbound can not be cancelled after 30 days.";
	public static final String INBOUND_CAN_NOT_BE_UPDATED_AFTER_30DAYS = "Inbound can not be updated after 30 days.";
	public static final String INBOUND_ADD_UPDATE_FAILED = "Inbound add/update failed.";

	public static final String HSCODE_SEARCH_DETAIL_INVALID = "HsCode search input detail is not valid.";
	public static final String HSCODE_SEARCH_SUCCESS = "Hscode searched successfully.";

	public static final String BARCODE_ADD_SUCCESS = "Barcode added successfully.";
	public static final String BARCODE_SEARCH_DETAIL_INVALID = "Barcode search input detail is not valid.";
	public static final String BARCODE_SEARCH_SUCCESS = "Barcode searched successfully.";
	public static final String BARCODE_VALIDATION_ERROR = "Barcode validation error message";

	public static final String OUTBOUND_ADD_SUCCESS = "Outbound added successfully.";
	public static final String OUTBOUND_UPDATE_SUCCESS = "Outbound Updated successfully.";
	public static final String OUTBOUND_DETAILS_NOT_FOUND = "Outbound detail not found.";
	public static final String OUTBOUND_DELETE_SUCCESS = "Outbound deleted successfully.";
	public static final String OUTBOUND_RETRIEVAL_SUCCESS = "Outbound retrieved successfully.";
	public static final String OUTBOUND_CAN_NOT_BE_CANCELLED_AFTER_30DAYS = "Outbound can not be cancelled after 30 days.";
	public static final String OUTBOUND_CAN_NOT_BE_CANCELLED_AFTER_INVOICE_GENERATED = "Outbound can not be cancelled after invoice generated.";
	public static final String OUTBOUND_CAN_NOT_BE_UPDATED_AFTER_INVOICE_GENERATED = "Outbound can not be updated after invoice generated.";
	public static final String OUTBOUND_CAN_NOT_BE_UPDATED_AFTER_30DAYS = "Outbound can not be updated after 30 days.";
	public static final String OUTBOUND_ADD_UPDATE_FAILED = "Outbound add/update failed.";
	public static final String OUTBOUND_ONLOAD_DATA_RETRIEVAL_SUCCESS = "Outbound Onload Data retrieved successfully.";
	public static final String OUTBOUND_ONLOAD_FAILED = "Outbound failed to load onload data";
	public static final String OUTBOUND_CANCEL_SUCCESS = "Outbound cancelled successfully.";
	public static final String OUTBOUND_CANCEL_FAILED = "Outbound cancel failed.";
	public static final String OUTBOUND_FETCH_FAILED = "Outbound fetch failed.";
	public static final String OUTBOUND_UPLOAD_DATA_DELETED_SUCCESS = "Outbound Upload document cancelled successfully ";
	public static final String OUTBOUND_UPLOADDOC_NOT_FOUND = "Outbound Upload document details not found. ";
	public static final String OUTBOUND_VALIDATION_ERROR = "Outbound validation error message";
	public static final String OUTBOUND_JSON_ERROR = "Outbound json convertion error message";

	public static final String INVALID_TYPE_OF_INVENTORY = "Invalid type of inventory";
	public static final String INVOICE_LIST_SUCCESS = "Invoice listed successfully";
	public static final String INVOICE_GENERATE_SUCCESS = "Invoice generated successfully";
	public static final String INVOICE_VIEW_SUCCESS = "Invoice view successfully";
	public static final String INVOICE_EXPORT_FAILED = "Invoice export failed";
	public static final String NO_DATA_TO_EXPORT = "No data available to export";
	public static final String INVOICE_DETAILS_NOT_FOUND = "Invoice details not found";
	public static final String INVENTORY_CANCELLED_SUCCESSFULLY = "Inventory cancelled successfully";
	
	public static final String WMS_API_REFERENCE_NO_ERROR = "Error occured while fetching reference number.";
	public static final String WMS_API_VALIDATING_USER_SERVICE_ERROR = "Error occured while validating user service.";
	public static final String WMS_API_FETCH_COMPANY_INFO_ERROR = "Error occured while fetching company information.";
	public static final String WMS_API_FETCH_HSCODE_ERROR = "Error occured while fetching HS Code.";

	public static final String BUSINESS_CODE_NOT_FOUND = "Business Code not found";
	public static final String INVALID_DATE_FORMAT = "Invalid date format";
	
	public static final String INVALID_TYPE_OF_REPORT = "Invalid type of report";
	public static final String REPORT_LIST_SUCCESS = "Report listed successfully";
	public static final String REPORT_LIST_FAILED = "Report list failed";
	public static final String REPORT_GENERATE_SUCCESS = "Report generated successfully";
	public static final String REPORT_GENERATE_FAILED = "Report generate failed";
	public static final String REPORT_DETAILS_NOT_FOUND = "Report details not found";
	public static final String REPORT_VIEW_SUCCESS = "Report view successfully";
	public static final String REPORT_VIEW_FAILED = "Report view failed";
	
	public static final String SOMETHING_WENT_WRONG = "Something went wrong";
	
	public static final String DATE_UPDATE_SUCCESS = "Date Updated successfully";
}
