package com.dpworld.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.PushInventoryItem;

@Repository
public interface PushInventoryItemRepository extends JpaRepository<PushInventoryItem, Long> {

}