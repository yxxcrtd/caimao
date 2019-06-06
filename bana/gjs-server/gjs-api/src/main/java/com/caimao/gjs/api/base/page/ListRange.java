package com.caimao.gjs.api.base.page;

import java.io.Serializable;
import java.util.List;

/**
 * @author scott
 *  2013年12月19日21:37:26
 *  desc：分页数据组装
 */


public class ListRange<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<T> data;
	private Page page;

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
