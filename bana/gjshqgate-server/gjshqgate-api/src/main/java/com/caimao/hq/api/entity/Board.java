/**
 *
 */
package com.caimao.hq.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Administrator
 *
 */
public class Board extends  BaseBean{


	private String prod_name;//产品名称


	private String prod_code;//产品代码


	public String getProd_name() {
		return prod_name;
	}


	public String getProd_code() {
		return prod_code;
	}


	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}


	public void setProd_code(String prod_code) {
		this.prod_code = prod_code;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
