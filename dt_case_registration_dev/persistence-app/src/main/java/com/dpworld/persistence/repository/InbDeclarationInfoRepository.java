package com.dpworld.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.InbDeclarationInformation;

@Repository
public interface InbDeclarationInfoRepository extends JpaRepository<InbDeclarationInformation, Long> {

	@Query("Select declaration from InbDeclarationInformation declaration where declaration.id=?1")
	InbDeclarationInformation findById(long id);

	@Query("SELECT declaration FROM InbDeclarationInformation declaration WHERE declaration.id = ?1 AND declaration.inbound IS NOT NULL")
	InbDeclarationInformation findByIdAndWithInbound(long id);
}
