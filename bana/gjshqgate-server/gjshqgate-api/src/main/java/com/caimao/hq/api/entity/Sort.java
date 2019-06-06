package com.caimao.hq.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Sort {

	//排序方式0表示升序1表示降序

	private String sort_type;
	//排序字段ID
	private int sort_field_id;
	//实际排序的字段名称
	private int sort_field_name;

	private int start_pos;//起始位置


	private SortType sort_type_grp;
	private SortDetail sort_detail_data;
	public String getSort_type() {
		return sort_type;
	}
	public int getSort_field_id() {
		return sort_field_id;
	}
	public int getSort_field_name() {
		return sort_field_name;
	}
	public int getStart_pos() {
		return start_pos;
	}
	public SortType getSort_type_grp() {
		return sort_type_grp;
	}
	public SortDetail getSort_detail_data() {
		return sort_detail_data;
	}
	public void setSort_type(String sort_type) {
		this.sort_type = sort_type;
	}
	public void setSort_field_id(int sort_field_id) {
		this.sort_field_id = sort_field_id;
	}
	public void setSort_field_name(int sort_field_name) {
		this.sort_field_name = sort_field_name;
	}
	public void setStart_pos(int start_pos) {
		this.start_pos = start_pos;
	}
	public void setSort_type_grp(SortType sort_type_grp) {
		this.sort_type_grp = sort_type_grp;
	}
	public void setSort_detail_data(SortDetail sort_detail_data) {
		this.sort_detail_data = sort_detail_data;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
