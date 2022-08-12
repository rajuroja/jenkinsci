package com.dpworld.persistence.model.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.OptBoolean;

@JsonInclude(Include.NON_NULL)
public class SResponse implements Serializable {
	private static final long serialVersionUID = 7565215354480104468L;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", lenient = OptBoolean.FALSE, timezone = JsonFormat.DEFAULT_TIMEZONE)
	List<Map<String, Object>> dataSet;

	private boolean hasNextPage = false;

	public SResponse(List<Map<String, Object>> dataSet) {
		this.dataSet = dataSet;
	}

	public SResponse(List<Map<String, Object>> dataSet, boolean hasNextPage) {
		this.dataSet = dataSet;
		this.hasNextPage = hasNextPage;
	}

	public SResponse() {
	}

	public List<Map<String, Object>> getDataSet() {
		if (dataSet == null) {
			dataSet = new ArrayList<>();
		}
		return dataSet;
	}

	public void setDataSet(List<Map<String, Object>> dataSet) {
		this.dataSet = dataSet;
	}

	public boolean isHasNextpage() {
		return hasNextPage;
	}

	public void setHasNextpage(boolean hasNextpage) {
		this.hasNextPage = hasNextpage;
	}

}
