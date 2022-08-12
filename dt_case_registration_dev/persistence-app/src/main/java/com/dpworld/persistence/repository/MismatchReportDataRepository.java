package com.dpworld.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.MismatchReportData;

@Repository
public interface MismatchReportDataRepository extends JpaRepository<MismatchReportData, Long> {

}
