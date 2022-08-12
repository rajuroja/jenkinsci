package com.dpworld.masterdataapp.service.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dpworld.common.utils.CustomException;
import com.dpworld.masterdataapp.constants.MasterDataAppConstants;
import com.dpworld.masterdataapp.service.FileUploadService;
import com.dpworld.masterdataapp.service.InbUploadDocumentsService;
import com.dpworld.masterdataapp.service.OutbUploadDocumentsService;
import com.dpworld.persistence.entity.InbUploadDocuments;
import com.dpworld.persistence.entity.OutbUploadDocuments;

@Service
public class FileUploadServiceImpl implements FileUploadService {
	
	private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

	@Value("${wms.fileupload.basepath}")
	private String basepath;
	
	@Autowired
	private InbUploadDocumentsService inbUploadDocumentsService;
	
	@Autowired
	private OutbUploadDocumentsService outbUploadDocumentsService;

	public String uploadImage(MultipartFile imageFile, long id, String imagePath) {
		//logger.info("ENTRY:: uploadFile :: Upload file");
		if (imageFile == null || imageFile.isEmpty()) {
			//logger.info("EXIT:: uploadImage :: File not found");
			return null;
		}
		try {
			String fileName = imageFile.getOriginalFilename();
			String extension = fileName.substring(imageFile.getOriginalFilename().lastIndexOf(".") + 1);
			
			logger.info("INFO:: uploadImage :: File name : {}", fileName);
			
			File imagePathFile = new File(imagePath);
			if (!imagePathFile.exists()) {
				imagePathFile.mkdirs();
			}
			
			String imageType = imageFile.getContentType();

			BufferedImage bfImage = null;

			if (imageFile != null) {
				bfImage = ImageIO.read(new ByteArrayInputStream(imageFile.getBytes()));
			}
			File imageFinal = new File(imagePath + File.separator + fileName);
			if (bfImage != null && (imageType.equalsIgnoreCase("image/jpeg") || imageType.equalsIgnoreCase("image/png"))) {
				ImageIO.write(bfImage, extension, imageFinal);
				bfImage.flush();
			} else if (imageType.equalsIgnoreCase("application/pdf") || imageType.equalsIgnoreCase("text/plain") || imageType.equalsIgnoreCase("application/doc")
					|| imageType.equalsIgnoreCase("application/ms-doc") || imageType.equalsIgnoreCase("application/octet-stream") || imageType.equalsIgnoreCase("video/mp4")
					|| imageType.equalsIgnoreCase("application/x-zip-compressed")) {
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(imageFinal));
				stream.write(imageFile.getBytes());
				stream.close();
			}
			//logger.info("EXIT:: uploadFile :: Uploaded file. {}", fileName);
			return fileName;
		} catch (Exception e) {
			logger.error("ERROR:: uploadFile :: Error occured while uploading file : {}", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String uploadOutboundDocument(OutbUploadDocuments document, MultipartFile file) {
		String imagePath = basepath + File.separator + "outbound" + File.separator + document.getOutbound().getOutboundId() + File.separator
				+ document.getUploadDocumentTypeId().getUploadDocumentTypeName();
		return uploadImage(file, document.getId(), imagePath);
	}

	@Override
	public String uploadInboundDocument(InbUploadDocuments document, MultipartFile file) {
		String imagePath = basepath + File.separator + "inbound" + File.separator + document.getInbound().getInboundId() + File.separator
				+ document.getUploadDocumentTypeId().getUploadDocumentTypeName();
		return uploadImage(file, document.getId(), imagePath);
	}

	@Override
	public String getFileBase64(String type, Long id) throws IOException {

		File imageFile = getFile(type, id);
		if (imageFile != null && imageFile.exists()) {
			byte[] fileContent = null;
			String prefix = "";
			String extension = FilenameUtils.getExtension(imageFile.getName());
			if (extension.equalsIgnoreCase(MasterDataAppConstants.PDF))
				prefix = MasterDataAppConstants.BASE64PREFIXPDF;
			else if (extension.equalsIgnoreCase(MasterDataAppConstants.JPEG) || extension.equalsIgnoreCase(MasterDataAppConstants.JPG))
				prefix = MasterDataAppConstants.BASE64PREFIXJPEG;
			else if (extension.equalsIgnoreCase(MasterDataAppConstants.PNG))
				prefix = MasterDataAppConstants.BASE64PNGMIMEPREFIX;
			fileContent = Files.readAllBytes(imageFile.toPath());
//			return fileContent;
			
			return prefix + Base64.getEncoder().encodeToString(fileContent);
		}
		throw new CustomException(MasterDataAppConstants.FILE_NOT_FOUND);
	}
	
	@Override
	public File getFile(String type, Long id) {
		File imageFile = null;
		switch (type) {
		case "inbDocument":
			InbUploadDocuments inbDocument = inbUploadDocumentsService.findById(id);
			if (inbDocument != null && inbDocument.getFilePath() != null) {
				String string = basepath + File.separator + "inbound" + File.separator + inbDocument.getInbound().getInboundId() + File.separator
						+ inbDocument.getUploadDocumentTypeId().getUploadDocumentTypeName() + File.separator + inbDocument.getFilePath();
				logger.info("INFO :: file path : {}", string);
				imageFile = new File(string);
			}
			break;

		case "outbDocument":
			OutbUploadDocuments outbDocument = outbUploadDocumentsService.findById(id);
			if (outbDocument != null && outbDocument.getFilePath() != null) {
				String string = basepath + File.separator + "outbound" + File.separator + outbDocument.getOutbound().getOutboundId() + File.separator
						+ outbDocument.getUploadDocumentTypeId().getUploadDocumentTypeName() + File.separator + outbDocument.getFilePath();
				logger.info("INFO :: file path : {}", string);
				imageFile = new File(string);
			}
			break;

		}
		if (imageFile != null && imageFile.exists()) {
			return imageFile;
		}
		throw new CustomException(MasterDataAppConstants.FILE_NOT_FOUND);
	}

}
