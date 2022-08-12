package com.dpworld.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.PushInventoryHistory;

@Repository
public interface PushInventoryHistoryRepository extends JpaRepository<PushInventoryHistory, Long> {

}
