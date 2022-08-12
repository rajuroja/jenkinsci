package com.dpworld.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.PushInventory;
import com.dpworld.persistence.entity.StatusType;

@Repository
public interface PushInventoryRepository extends JpaRepository<PushInventory, Long> {

	List<PushInventory> findByStatus(StatusType statusType);

	List<PushInventory> findByStatusNot(StatusType statusType);
	
	@Query("Select inventory from PushInventory inventory JOIN PushInventoryItem item on inventory.id = item.pushInventory.id where "
			+ "	inventory.inbDeclarationNumber=?1 AND item.skuBarCode=?2 AND item.status=?3")
	List<PushInventory> findByInbDeclarationAndSkuBarcodeAndStatus(String declarationNumber, String skuBarcode, StatusType status);
	
	@Query("Select inventory from PushInventory inventory JOIN PushInventoryItem item on inventory.id = item.pushInventory.id where "
			+ "	inventory.inbDeclarationNumber=?1 AND item.skuBarCode=?2 AND item.status=?3 AND inventory.id<?4")
	List<PushInventory> findByInbDeclarationAndSkuBarcodeAndStatusAndLessThanId(String declarationNumber, String skuBarcode, StatusType status, Long id);

}
