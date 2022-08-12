package com.dpworld.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.dpworld.persistence.enums.InventoryType;
import com.dpworld.persistence.enums.OperationType;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "PUSH_INVENTORY")
public class PushInventory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_push_inventory")
	@SequenceGenerator(name = "seq_push_inventory", sequenceName = "seq_push_inventory", allocationSize = 1)
	private long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private StatusType status;

	@Enumerated(EnumType.STRING)
	@Column(name = "INVENTORY_TYPE")
	private InventoryType inventoryType;

	@Enumerated(EnumType.STRING)
	@Column(name = "OPERATION_TYPE")
	private OperationType operationType;

	@Column(name = "CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "LAST_PUSHED_DATE")
	private LocalDateTime lastPushedDate;

	@Column(name = "INVENTORY_ID")
	private Long inventoryId;

	@Column(name = "RETRY_COUNT")
	private Integer retryCount;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "FACILITY_ID_CODE")
	private String facilityIdCode;

	@Column(name = "BARCODE") // this bar code means AGENT CODE here
	private String barcode;

	@Column(name = "INB_DECLARATION_NUMBER")
	private String inbDeclarationNumber;

	@JsonManagedReference
	@OneToMany(mappedBy = "pushInventory", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	List<PushInventoryItem> pushInventoryItems = new ArrayList<PushInventoryItem>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	public InventoryType getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(InventoryType inventoryType) {
		this.inventoryType = inventoryType;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastPushedDate() {
		return lastPushedDate;
	}

	public void setLastPushedDate(LocalDateTime lastPushedDate) {
		this.lastPushedDate = lastPushedDate;
	}

	public Long getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getFacilityIdCode() {
		return facilityIdCode;
	}

	public void setFacilityIdCode(String facilityIdCode) {
		this.facilityIdCode = facilityIdCode;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public List<PushInventoryItem> getPushInventoryItems() {
		return pushInventoryItems;
	}

	public void setPushInventoryItems(List<PushInventoryItem> pushInventoryItems) {
		this.pushInventoryItems = pushInventoryItems;
	}

	public String getInbDeclarationNumber() {
		return inbDeclarationNumber;
	}

	public void setInbDeclarationNumber(String inbDeclarationNumber) {
		this.inbDeclarationNumber = inbDeclarationNumber;
	}

	@Override
	public String toString() {
		return "PushInventory [id=" + id + ", status=" + status + ", inventoryId=" + inventoryId + ", inbDeclarationNumber=" + inbDeclarationNumber + "]";
	}

}
