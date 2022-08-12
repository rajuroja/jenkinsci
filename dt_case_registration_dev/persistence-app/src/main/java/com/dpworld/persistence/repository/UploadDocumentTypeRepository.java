package com.dpworld.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.UploadDocumentType;

@Repository
public interface UploadDocumentTypeRepository extends CrudRepository<UploadDocumentType, Integer> {

	UploadDocumentType findByUploadDocumentTypeId(Byte uploadDocumentTypeId);
	
	@Query("Select document from UploadDocumentType document where document.uploadDocumentTypeName = ?1")
	UploadDocumentType findByName(String name);
}
