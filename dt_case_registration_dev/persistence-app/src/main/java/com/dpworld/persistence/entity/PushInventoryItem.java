package com.dpworld.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "PUSH_INVENTORY_ITEM")
public class PushInventoryItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_push_inventory_item")
	@SequenceGenerator(name = "seq_push_inventory_item", sequenceName = "seq_push_inventory_item", allocationSize = 1)
	private long id;

	@Lob
	@Column(name = "REQUEST_BODY")
	private String requestBody;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private StatusType status;

	@Lob
	@Column(name = "RESPONSE_MESSAGE")
	private String responseMessage;

	@JsonBackReference
	@ManyToOne
	@NotNull
	@JoinColumn(name = "PUSH_INVENTORY_ID", referencedColumnName = "ID")
	private PushInventory pushInventory;

	@Column(name = "RETRY_COUNT")
	private Integer retryCount;

	@Column(name = "LAST_PUSHED_DATE")
	private LocalDateTime lastPushedDate;

	@Column(name = "sku_bar_code")
	private String skuBarCode;

	@Column(name = "reference_number")
	private String referenceNumber;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public PushInventory getPushInventory() {
		return pushInventory;
	}

	public void setPushInventory(PushInventory pushInventory) {
		this.pushInventory = pushInventory;
	}

	public LocalDateTime getLastPushedDate() {
		return lastPushedDate;
	}

	public void setLastPushedDate(LocalDateTime lastPushedDate) {
		this.lastPushedDate = lastPushedDate;
	}

	public String getSkuBarCode() {
		return skuBarCode;
	}

	public void setSkuBarCode(String skuBarCode) {
		this.skuBarCode = skuBarCode;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	@Override
	public String toString() {
		return "PushInventoryItem [id=" + id + ", status=" + status + ", pushInventory=" + pushInventory + ", skuBarCode=" + skuBarCode + "]";
	}

}