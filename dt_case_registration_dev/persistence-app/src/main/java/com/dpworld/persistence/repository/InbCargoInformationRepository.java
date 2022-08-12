package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dpworld.persistence.entity.InbCargoInformation;

@Repository
public interface InbCargoInformationRepository extends JpaRepository<InbCargoInformation, Long> {

	@Modifying
	@Transactional
	@Query("DELETE FROM InbCargoInformation u WHERE u.id IN (?1)")
	void deleteAllByIdsIn(List<Long> ids);

	@Modifying
	@Transactional
	@Query("DELETE FROM InbCargoInformation u WHERE u.id = ?1")
	void deleteById(Long existingCargoInfo);

}
