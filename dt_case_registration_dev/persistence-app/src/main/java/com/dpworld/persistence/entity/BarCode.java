package com.dpworld.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "BAR_CODE",  indexes = { @Index(columnList = "BARCODE"), @Index(columnList = "HS_CODE_ID") })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "barCodeId", scope = BarCode.class)
public class BarCode extends ECommonFields implements Serializable {

	private static final long serialVersionUID = 1L;

	public BarCode() {
		super();
	}

	public BarCode(long barCodeId) {
		super();
		this.barCodeId = barCodeId;
	}

	@Id
	@Column(name = "BARCODE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bar_code")
	@SequenceGenerator(name = "seq_bar_code", sequenceName = "seq_bar_code", allocationSize = 1)
	private long barCodeId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "HS_CODE_ID", referencedColumnName = "HS_CODE_ID")
	private HsCode hsCodeId;

	@Column(name = "BARCODE", length = 40)
	private String barCode;

	@Column(name = "BARCODE_DESCRIPTION")
	private String barCodeDescription;

	@Type(type = "true_false")
	@Column(name = "PUSHED_TO_OIC")
	protected Boolean pushedToOic;

	public long getBarCodeId() {
		return barCodeId;
	}

	public void setBarCodeId(long barCodeId) {
		this.barCodeId = barCodeId;
	}

	public HsCode getHsCodeId() {
		return hsCodeId;
	}

	public void setHsCodeId(HsCode hsCodeId) {
		this.hsCodeId = hsCodeId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getBarCodeDescription() {
		return barCodeDescription;
	}

	public void setBarCodeDescription(String barCodeDescription) {
		this.barCodeDescription = barCodeDescription;
	}

	public Boolean getPushedToOic() {
		return pushedToOic;
	}

	public void setPushedToOic(Boolean pushedToOic) {
		this.pushedToOic = pushedToOic;
	}

}
