package com.dpworld.persistence.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

@MappedSuperclass
public class ECommonFields implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "CREATED_BY", length = 30)
	protected String createdBy;

	@NotNull
	@Column(name = "CREATED_TIME")
	protected Date createdOn;

	@Column(name = "UPDATED_BY", length = 30)
	protected String updatedBy;

	@Column(name = "UPDATED_TIME")
	protected Date updatedOn;

	@Type(type = "true_false")
	@Column(name = "IS_CANCEL")
	protected Boolean isCancel;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Boolean getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Boolean isCancel) {
		this.isCancel = isCancel;
	}

}
