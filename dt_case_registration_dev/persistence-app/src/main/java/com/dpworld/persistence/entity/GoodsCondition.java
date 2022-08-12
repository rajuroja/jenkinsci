package com.dpworld.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "GOODS_CONDITION")
public class GoodsCondition implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_goods_condition")
	@SequenceGenerator(name = "seq_goods_condition", sequenceName = "seq_goods_condition", allocationSize = 1)
	@Column(name = "GOODS_CONDITION_ID")
	private Byte goodsConditionId;

	@Column(name = "GOODS_CONDITION", length = 10)
	private String goodsCondition;

	public GoodsCondition() {

	}

	public GoodsCondition(Integer goodsConditionId) {
		super();
		this.goodsConditionId = goodsConditionId.byteValue();
	}

	public GoodsCondition(Byte goodsConditionId, String goodsCondition) {
		super();
		this.goodsConditionId = goodsConditionId;
		this.goodsCondition = goodsCondition;
	}

	public Byte getGoodsConditionId() {
		return goodsConditionId;
	}

	public void setGoodsConditionId(Byte goodsConditionId) {
		this.goodsConditionId = goodsConditionId;
	}

	public String getGoodsCondition() {
		return goodsCondition;
	}

	public void setGoodsCondition(String goodsCondition) {
		this.goodsCondition = goodsCondition;
	}

	@Override
	public String toString() {
		return "GoodsCondition [goodsConditionId=" + goodsConditionId + ", goodsCondition=" + goodsCondition + "]";
	}

}