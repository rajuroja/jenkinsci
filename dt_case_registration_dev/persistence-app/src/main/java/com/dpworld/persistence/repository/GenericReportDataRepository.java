package com.dpworld.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.GenericReportData;

@Repository
public interface GenericReportDataRepository extends JpaRepository<GenericReportData, Long> {

}
