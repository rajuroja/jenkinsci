package com.dpworld.persistence.entity;

public enum ReturnType {

	NONE("None"), RETURNGOODS("ReturnGoods"), RETURNLOCALGOODS("ReturnLocalGoods");

	private String returnType;

	private ReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getReturnType() {
		return returnType;
	}

}
