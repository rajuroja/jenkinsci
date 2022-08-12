package com.dpworld.persistence.entity;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "PUSH_INVENTORY_ITEM_HISTORY")
public class PushInventoryItemHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_push_inventory_item_hist")
	@SequenceGenerator(name = "seq_push_inventory_item_hist", sequenceName = "seq_push_inventory_item_hist", allocationSize = 1)
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
	@NotNull
	@ManyToOne
	@JoinColumn(name = "PUSH_INVENTORY_HISTORY_ID", referencedColumnName = "ID")
	private PushInventoryHistory pushInventoryHistory;

	@Column(name = "RETRY_COUNT")
	private Integer retryCount;

	@Column(name = "LAST_PUSHED_DATE")
	private Date lastPushedDate;

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

	public Date getLastPushedDate() {
		return lastPushedDate;
	}

	public void setLastPushedDate(Date lastPushedDate) {
		this.lastPushedDate = lastPushedDate;
	}

	public PushInventoryHistory getPushInventoryHistory() {
		return pushInventoryHistory;
	}

	public void setPushInventoryHistory(PushInventoryHistory pushInventoryHistory) {
		this.pushInventoryHistory = pushInventoryHistory;
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

}