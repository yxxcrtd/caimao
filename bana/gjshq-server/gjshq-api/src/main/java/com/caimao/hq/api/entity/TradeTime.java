package com.caimao.hq.api.entity;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TradeTime {

	private long opentime;//开市时间   格式为  yyyyMMddHHmm  转换的时间戳
	private long closetime;//闭市时间
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public long getOpentime() {
		return opentime;
	}

	public void setOpentime(long opentime) {
		this.opentime = opentime;
	}

	public long getClosetime() {
		return closetime;
	}

	public void setClosetime(long closetime) {
		this.closetime = closetime;
	}
}
