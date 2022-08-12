package com.dpworld.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.InvoiceOutbounds;

@Repository
public interface InvoiceOutboundsRepository extends JpaRepository<InvoiceOutbounds, Long> {

}
