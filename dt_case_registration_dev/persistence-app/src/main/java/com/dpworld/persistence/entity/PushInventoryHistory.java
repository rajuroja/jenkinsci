package com.dpworld.persistence.entity;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "PUSH_INVENTORY_HISTORY")
public class PushInventoryHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_push_inventory_history")
	@SequenceGenerator(name = "seq_push_inventory_history", sequenceName = "seq_push_inventory_history", allocationSize = 1)
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
	private Date createdDate;

	@Column(name = "LAST_PUSHED_DATE")
	private Date lastPushedDate;

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

	@JsonManagedReference
	@OneToMany(mappedBy = "pushInventoryHistory", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<PushInventoryItemHistory> pushInventoryItems;

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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastPushedDate() {
		return lastPushedDate;
	}

	public void setLastPushedDate(Date lastPushedDate) {
		this.lastPushedDate = lastPushedDate;
	}

	public Long getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
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

	public List<PushInventoryItemHistory> getPushInventoryItems() {
		return pushInventoryItems;
	}

	public void setPushInventoryItems(List<PushInventoryItemHistory> pushInventoryItems) {
		this.pushInventoryItems = pushInventoryItems;
	}

}
