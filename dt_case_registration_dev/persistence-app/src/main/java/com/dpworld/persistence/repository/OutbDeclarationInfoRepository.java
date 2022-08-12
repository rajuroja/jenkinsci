package com.dpworld.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.OutbDeclarationInformation;

@Repository
public interface OutbDeclarationInfoRepository extends JpaRepository<OutbDeclarationInformation, Long> {
	
	@Query("Select declaration from OutbDeclarationInformation declaration where declaration.id=?1")
	OutbDeclarationInformation findById(long id);

	@Query("SELECT declaration FROM OutbDeclarationInformation declaration WHERE declaration.id = ?1 AND declaration.outbound IS NOT NULL")
	OutbDeclarationInformation findByIdAndWithOutbound(long id);
}
