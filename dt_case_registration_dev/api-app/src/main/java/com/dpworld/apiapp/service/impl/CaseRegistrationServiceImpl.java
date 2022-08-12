package com.dpworld.apiapp.service.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dpworld.apiapp.constants.ApiAppConstants;
import com.dpworld.apiapp.model.CaseDetailsRequest;
import com.dpworld.apiapp.service.CaseRegistrationService;
import com.dpworld.masterdataapp.utility.TmWmsUtils;
import com.dpworld.persistence.entity.CaseRegistrationDetails;
import com.dpworld.persistence.repository.CaseRegistrationRepository;

@Service
public class CaseRegistrationServiceImpl implements CaseRegistrationService {

	private static Logger logger = LoggerFactory.getLogger(CaseRegistrationServiceImpl.class);

	@Autowired
	CaseRegistrationRepository caseRegistrationRepo;

	@Autowired
	private TmWmsUtils tmWmsUtils;

	@Value("${wms.fileupload.maxsize}")
	private long maxsize;

	@Value("${wms.fileupload.basepath}")
	private String basepath;

	private Long maxsizeInMb = maxsize / 1000000;

	@Override
	public CaseRegistrationDetails addOrUpdateUser(CaseDetailsRequest caseDetailsRequest, boolean isUpdate,
			CaseRegistrationDetails caseRegistrationDetailsEntity, List<MultipartFile> caseFiles) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!isUpdate) {
			BeanUtils.copyProperties(caseDetailsRequest, caseRegistrationDetailsEntity, "complainType");
			caseRegistrationDetailsEntity.setCreatedOn(new Date());
			caseRegistrationDetailsEntity.setCreatedBy(auth.getName());
		}
		caseRegistrationDetailsEntity.setIsActive(ApiAppConstants.ACTIVE);
		caseRegistrationDetailsEntity.setUpdatedBy(auth.getName());
		caseRegistrationDetailsEntity.setUpdatedOn(new Date());
		try {
			caseRegistrationRepo.save(caseRegistrationDetailsEntity);
			if (caseFiles != null && !caseFiles.isEmpty()) {
				for (MultipartFile file : caseFiles)
					if (!file.isEmpty()) {
						if (!tmWmsUtils.validateFileType(file))
							logger.error("Invalid type of file");
						else if (file.getSize() > maxsize)
							logger.error("File size should be less than or equal to " + maxsizeInMb + "MB");
					}
			}
			for (MultipartFile file : caseFiles) {
				if (!file.isEmpty()) {
					String imagePath = new String();
					if (caseDetailsRequest.getCaseType() == 1) {
						imagePath = basepath + File.separator + "complain" + File.separator
								+ caseRegistrationDetailsEntity.getCaseRegId();
					} else {
						imagePath = basepath + File.separator + "enquiry" + File.separator
								+ caseRegistrationDetailsEntity.getCaseRegId();
					}
					uploadImage(file, caseRegistrationDetailsEntity.getCaseRegId(), imagePath);
				}
			}
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Error in Complain Registration");
		}
		return caseRegistrationDetailsEntity;
	}

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
}
