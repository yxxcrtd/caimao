package com.caimao.hq.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SortType {

	private String hq_type_code;

	public String getHq_type_code() {
		return hq_type_code;
	}

	public void setHq_type_code(String hq_type_code) {
		this.hq_type_code = hq_type_code;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
