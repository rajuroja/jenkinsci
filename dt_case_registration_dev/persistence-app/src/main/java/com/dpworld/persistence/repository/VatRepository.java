package com.dpworld.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.Vat;

@Repository
public interface VatRepository extends JpaRepository<Vat, Long> {

	Vat findFirstByOrderByCreatedOnDesc();

}