package com.caimao.hq.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class FundflowDetail {

	private double turnover_in;//流入成交额
	private double turnover_out;//流出成交额
	public double getTurnover_in() {
		return turnover_in;
	}
	public double getTurnover_out() {
		return turnover_out;
	}
	public void setTurnover_in(double turnover_in) {
		this.turnover_in = turnover_in;
	}
	public void setTurnover_out(double turnover_out) {
		this.turnover_out = turnover_out;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
