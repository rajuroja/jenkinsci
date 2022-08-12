package com.dpworld.masterdataapp.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dpworld.common.model.ApiResponse;
import com.dpworld.common.utils.CustomException;
import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.service.FileUploadService;
import com.dpworld.masterdataapp.utility.TmWmsUtils;

@RestController
//@RequestMapping("")
public class FileController {

	private static Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private FileUploadService fileUploadService;
	
	@Value("${wms.fileupload.maxsize}")
	private long maxsize;
	
	@Autowired
	private TmWmsUtils tmWmsUtils;
	
	private Long maxsizeInMb = maxsize / 1000000;

	@GetMapping("/read-file/{type}/{id}")
	public ApiResponse<?> readFile(@PathVariable("type") String type, @PathVariable("id") Long id) {
		//logger.info("ENTRY :: readFile : with request type: {}, id: {}", type, id);
		String base64 = null;
		try {
			base64 = fileUploadService.getFileBase64(type, id);
		} catch (IOException e) {
			logger.error("ERROR:: Error occurred while reading file : {}", e);
			return new ApiResponse<>(false, HttpStatus.BAD_REQUEST.name(), MasterDataAppConstants.FAILED + " " + MasterDataAppConstants.ERROR_OCCURRED_WHILE_READING_FILE);
		} catch (CustomException e) {
			logger.error("ERROR:: Error occurred while reading file : {}", e);
			return new ApiResponse<>(false, HttpStatus.BAD_REQUEST.name(), MasterDataAppConstants.FAILED + " " + e.getMessage());
		}
		if (base64 != null && !base64.isEmpty()) {
			//logger.info("EXIT :: readFile :: {}", MasterDataAppConstants.SUCCESS);
			return new ApiResponse<>(true, HttpStatus.OK.name(), "success", base64);
		}
		return new ApiResponse<>(false, HttpStatus.BAD_REQUEST.name(), MasterDataAppConstants.FAILED);
	}
	
	@GetMapping("/download-file/{type}/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable("type") String type, @PathVariable("id") Long id) throws IOException {
		//logger.info("ENTRY :: downloadFile : with request type: {}, id: {}", type, id);
		File file= null;
		try {
			file = fileUploadService.getFile(type, id);
		} catch (CustomException e) {
			logger.error("ERROR:: Error occurred while downloading file : {}", e.getMessage());
			return new ResponseEntity<>("File download failed.", HttpStatus.BAD_REQUEST);
		}
		if (file != null) {
			//logger.info("EXIT :: downloadFile :: {}", MasterDataAppConstants.SUCCESS);
			Path path = Paths.get(file.getAbsolutePath());
			ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.set("Conetent-Disposition", "attachment; filename=" + file.getName());
			//logger.info("EXIT :: downloadFile :: {}", file.getName());
			return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
		}
		return new ResponseEntity<>(MasterDataAppConstants.FAILED, HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/validateFileTypeAndSize")
	public ApiResponse<?> validateFileTypeAndSize(@RequestPart("file") MultipartFile file) {
		if (!file.isEmpty()) {
			if (!tmWmsUtils.validateFileType(file))
				return new ApiResponse<>(false, HttpStatus.BAD_REQUEST.name(), "Invalid type of file");
			else if (file.getSize() > maxsize)
				return new ApiResponse<>(false, HttpStatus.BAD_REQUEST.name(), "File size should be less than or equal to " + maxsizeInMb + "MB");
			return new ApiResponse<>(true, HttpStatus.OK.name(), "success");
		}
		return new ApiResponse<>(false, HttpStatus.BAD_REQUEST.name(), "File not selected");
	}
}
