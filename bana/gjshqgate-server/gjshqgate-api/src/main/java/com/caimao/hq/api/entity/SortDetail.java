package com.caimao.hq.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SortDetail {


	private String prod_code;//产品代码
	private String hq_type_code;//类型代码
	private String prod_name;//产品名称
	private String data_timestamp;//时间戳
	private String trade_mins;//交易分钟数
	private String trade_status;//交易状态字符型枚举值START：市场启动(初始化之后,集合竞价前)PRETR：盘前OCALL：开始集合竞价TRADE：交易(连续撮合)HALT：暂停交易SUSP：停盘BREAK：休市POSTR：盘后ENDTR：交易结束
	private double preclose_px;//昨收价
	private double open_px;//开盘价
	private double last_px;//最新价
	private double high_px;//最高价
	private double low_px;//最低价
	private double close_px;//收盘价

	private double avg_px;//平均价
	private double wavg_px;//加权平均价
	private double business_count;//成交笔数
	private double business_balance;//成交金额
	private double business_amount;//成交数量
	private double up_px;//涨停价格
	private double down_px;//跌停价格
	private double current_amount;//当前数量
	private double business_amount_in;//内盘成交量
	private double business_amount_out;//外盘成交量
	private String bid_grp;//值为字符串类型，逗号分隔，每3个元素（委托价格，委托数量，委托笔数）表示一个档位信息处理方法，先对字符串按逗号分隔，得到一个数组，比如分隔后有15个，则15 % 3 = 5，表示有5档
	private String offer_grp;
	private double entrust_rate;//委比
	private double entrust_diff;//委差
	private double w52_low_px;//52周最低价
	private double w52_high_px;//52周最高价
	private double px_change;//价格涨跌
	private double px_change_rate;//涨跌幅
	private double amplitude;//振幅
	private double popc_px;//盘前/盘后价格

	private double trade_section;//当前交易阶段：0 闭市(没有延迟数据)1 盘前2 盘中3 盘后4 实时盘后(没有延迟数据)
	private double circulate_amount;//流通股本
	private double total_shares;//总股本
	private double market_value;//证券市值
	private double circulation_value;//流通市值
	private double vol_ratio;//量比
	private double turnover_ratio;//换手率
	private double amount;//持仓量
	private double prev_amount;//昨持仓
	private double amount_delta;//日增持
	private double prev_settlement;//昨结算
	private double settlement;//今结算价
	private double iopv;//基金净值
	private double debt_fund_value;//国债基金净值
	private double eps;//每股收益(摊薄)
	private double bps;//每股净资产
	private double dyn_pb_rate;//动态市净率
	private double pe_rate;//市盈率
	private double fin_quarter;//财务季度
	private double fin_end_date;//财务截至日期
	public String getProd_code() {
		return prod_code;
	}
	public String getHq_type_code() {
		return hq_type_code;
	}
	public String getProd_name() {
		return prod_name;
	}
	public String getData_timestamp() {
		return data_timestamp;
	}
	public String getTrade_mins() {
		return trade_mins;
	}
	public String getTrade_status() {
		return trade_status;
	}
	public double getPreclose_px() {
		return preclose_px;
	}
	public double getOpen_px() {
		return open_px;
	}
	public double getLast_px() {
		return last_px;
	}
	public double getHigh_px() {
		return high_px;
	}
	public double getLow_px() {
		return low_px;
	}
	public double getClose_px() {
		return close_px;
	}
	public double getAvg_px() {
		return avg_px;
	}
	public double getWavg_px() {
		return wavg_px;
	}
	public double getBusiness_count() {
		return business_count;
	}
	public double getBusiness_balance() {
		return business_balance;
	}
	public double getBusiness_amount() {
		return business_amount;
	}
	public double getUp_px() {
		return up_px;
	}
	public double getDown_px() {
		return down_px;
	}
	public double getCurrent_amount() {
		return current_amount;
	}
	public double getBusiness_amount_in() {
		return business_amount_in;
	}
	public double getBusiness_amount_out() {
		return business_amount_out;
	}
	public String getBid_grp() {
		return bid_grp;
	}
	public String getOffer_grp() {
		return offer_grp;
	}
	public double getEntrust_rate() {
		return entrust_rate;
	}
	public double getEntrust_diff() {
		return entrust_diff;
	}
	public double getW52_low_px() {
		return w52_low_px;
	}
	public double getW52_high_px() {
		return w52_high_px;
	}
	public double getPx_change() {
		return px_change;
	}
	public double getPx_change_rate() {
		return px_change_rate;
	}
	public double getAmplitude() {
		return amplitude;
	}
	public double getPopc_px() {
		return popc_px;
	}
	public double getTrade_section() {
		return trade_section;
	}
	public double getCirculate_amount() {
		return circulate_amount;
	}
	public double getTotal_shares() {
		return total_shares;
	}
	public double getMarket_value() {
		return market_value;
	}
	public double getCirculation_value() {
		return circulation_value;
	}
	public double getVol_ratio() {
		return vol_ratio;
	}
	public double getTurnover_ratio() {
		return turnover_ratio;
	}
	public double getAmount() {
		return amount;
	}
	public double getPrev_amount() {
		return prev_amount;
	}
	public double getAmount_delta() {
		return amount_delta;
	}
	public double getPrev_settlement() {
		return prev_settlement;
	}
	public double getSettlement() {
		return settlement;
	}
	public double getIopv() {
		return iopv;
	}
	public double getDebt_fund_value() {
		return debt_fund_value;
	}
	public double getEps() {
		return eps;
	}
	public double getBps() {
		return bps;
	}
	public double getDyn_pb_rate() {
		return dyn_pb_rate;
	}
	public double getPe_rate() {
		return pe_rate;
	}
	public double getFin_quarter() {
		return fin_quarter;
	}
	public double getFin_end_date() {
		return fin_end_date;
	}
	public double getShares_per_hand() {
		return shares_per_hand;
	}
	public double getRise_count() {
		return rise_count;
	}
	public double getFall_count() {
		return fall_count;
	}
	public String getMember_count() {
		return member_count;
	}
	public String getSpecial_marker() {
		return special_marker;
	}
	public String getExercise_date() {
		return exercise_date;
	}
	public String getExercise_price() {
		return exercise_price;
	}
	public String getContract_unit() {
		return contract_unit;
	}
	public String getContract_code() {
		return contract_code;
	}
	public String getCall_put() {
		return call_put;
	}
	public String getExercise_type() {
		return exercise_type;
	}
	public String getStart_trade_date() {
		return start_trade_date;
	}
	public String getEnd_trade_date() {
		return end_trade_date;
	}
	public String getExpire_date() {
		return expire_date;
	}
	public String getSecurity_status_flag() {
		return security_status_flag;
	}
	public String getTick_size() {
		return tick_size;
	}
	public String getTrading_phase_code() {
		return trading_phase_code;
	}
	public String getAuction_price() {
		return auction_price;
	}
	public String getMatched_qty() {
		return matched_qty;
	}
	public String getMin5_chgpct() {
		return min5_chgpct;
	}
	public String getIssue_date() {
		return issue_date;
	}
	public String getIpo_price() {
		return ipo_price;
	}
	public String getChi_spelling_grp() {
		return chi_spelling_grp;
	}
	public void setProd_code(String prod_code) {
		this.prod_code = prod_code;
	}
	public void setHq_type_code(String hq_type_code) {
		this.hq_type_code = hq_type_code;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public void setData_timestamp(String data_timestamp) {
		this.data_timestamp = data_timestamp;
	}
	public void setTrade_mins(String trade_mins) {
		this.trade_mins = trade_mins;
	}
	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}
	public void setPreclose_px(double preclose_px) {
		this.preclose_px = preclose_px;
	}
	public void setOpen_px(double open_px) {
		this.open_px = open_px;
	}
	public void setLast_px(double last_px) {
		this.last_px = last_px;
	}
	public void setHigh_px(double high_px) {
		this.high_px = high_px;
	}
	public void setLow_px(double low_px) {
		this.low_px = low_px;
	}
	public void setClose_px(double close_px) {
		this.close_px = close_px;
	}
	public void setAvg_px(double avg_px) {
		this.avg_px = avg_px;
	}
	public void setWavg_px(double wavg_px) {
		this.wavg_px = wavg_px;
	}
	public void setBusiness_count(double business_count) {
		this.business_count = business_count;
	}
	public void setBusiness_balance(double business_balance) {
		this.business_balance = business_balance;
	}
	public void setBusiness_amount(double business_amount) {
		this.business_amount = business_amount;
	}
	public void setUp_px(double up_px) {
		this.up_px = up_px;
	}
	public void setDown_px(double down_px) {
		this.down_px = down_px;
	}
	public void setCurrent_amount(double current_amount) {
		this.current_amount = current_amount;
	}
	public void setBusiness_amount_in(double business_amount_in) {
		this.business_amount_in = business_amount_in;
	}
	public void setBusiness_amount_out(double business_amount_out) {
		this.business_amount_out = business_amount_out;
	}
	public void setBid_grp(String bid_grp) {
		this.bid_grp = bid_grp;
	}
	public void setOffer_grp(String offer_grp) {
		this.offer_grp = offer_grp;
	}
	public void setEntrust_rate(double entrust_rate) {
		this.entrust_rate = entrust_rate;
	}
	public void setEntrust_diff(double entrust_diff) {
		this.entrust_diff = entrust_diff;
	}
	public void setW52_low_px(double w52_low_px) {
		this.w52_low_px = w52_low_px;
	}
	public void setW52_high_px(double w52_high_px) {
		this.w52_high_px = w52_high_px;
	}
	public void setPx_change(double px_change) {
		this.px_change = px_change;
	}
	public void setPx_change_rate(double px_change_rate) {
		this.px_change_rate = px_change_rate;
	}
	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}
	public void setPopc_px(double popc_px) {
		this.popc_px = popc_px;
	}
	public void setTrade_section(double trade_section) {
		this.trade_section = trade_section;
	}
	public void setCirculate_amount(double circulate_amount) {
		this.circulate_amount = circulate_amount;
	}
	public void setTotal_shares(double total_shares) {
		this.total_shares = total_shares;
	}
	public void setMarket_value(double market_value) {
		this.market_value = market_value;
	}
	public void setCirculation_value(double circulation_value) {
		this.circulation_value = circulation_value;
	}
	public void setVol_ratio(double vol_ratio) {
		this.vol_ratio = vol_ratio;
	}
	public void setTurnover_ratio(double turnover_ratio) {
		this.turnover_ratio = turnover_ratio;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public void setPrev_amount(double prev_amount) {
		this.prev_amount = prev_amount;
	}
	public void setAmount_delta(double amount_delta) {
		this.amount_delta = amount_delta;
	}
	public void setPrev_settlement(double prev_settlement) {
		this.prev_settlement = prev_settlement;
	}
	public void setSettlement(double settlement) {
		this.settlement = settlement;
	}
	public void setIopv(double iopv) {
		this.iopv = iopv;
	}
	public void setDebt_fund_value(double debt_fund_value) {
		this.debt_fund_value = debt_fund_value;
	}
	public void setEps(double eps) {
		this.eps = eps;
	}
	public void setBps(double bps) {
		this.bps = bps;
	}
	public void setDyn_pb_rate(double dyn_pb_rate) {
		this.dyn_pb_rate = dyn_pb_rate;
	}
	public void setPe_rate(double pe_rate) {
		this.pe_rate = pe_rate;
	}
	public void setFin_quarter(double fin_quarter) {
		this.fin_quarter = fin_quarter;
	}
	public void setFin_end_date(double fin_end_date) {
		this.fin_end_date = fin_end_date;
	}
	public void setShares_per_hand(double shares_per_hand) {
		this.shares_per_hand = shares_per_hand;
	}
	public void setRise_count(double rise_count) {
		this.rise_count = rise_count;
	}
	public void setFall_count(double fall_count) {
		this.fall_count = fall_count;
	}
	public void setMember_count(String member_count) {
		this.member_count = member_count;
	}
	public void setSpecial_marker(String special_marker) {
		this.special_marker = special_marker;
	}
	public void setExercise_date(String exercise_date) {
		this.exercise_date = exercise_date;
	}
	public void setExercise_price(String exercise_price) {
		this.exercise_price = exercise_price;
	}
	public void setContract_unit(String contract_unit) {
		this.contract_unit = contract_unit;
	}
	public void setContract_code(String contract_code) {
		this.contract_code = contract_code;
	}
	public void setCall_put(String call_put) {
		this.call_put = call_put;
	}
	public void setExercise_type(String exercise_type) {
		this.exercise_type = exercise_type;
	}
	public void setStart_trade_date(String start_trade_date) {
		this.start_trade_date = start_trade_date;
	}
	public void setEnd_trade_date(String end_trade_date) {
		this.end_trade_date = end_trade_date;
	}
	public void setExpire_date(String expire_date) {
		this.expire_date = expire_date;
	}
	public void setSecurity_status_flag(String security_status_flag) {
		this.security_status_flag = security_status_flag;
	}
	public void setTick_size(String tick_size) {
		this.tick_size = tick_size;
	}
	public void setTrading_phase_code(String trading_phase_code) {
		this.trading_phase_code = trading_phase_code;
	}
	public void setAuction_price(String auction_price) {
		this.auction_price = auction_price;
	}
	public void setMatched_qty(String matched_qty) {
		this.matched_qty = matched_qty;
	}
	public void setMin5_chgpct(String min5_chgpct) {
		this.min5_chgpct = min5_chgpct;
	}
	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
	}
	public void setIpo_price(String ipo_price) {
		this.ipo_price = ipo_price;
	}
	public void setChi_spelling_grp(String chi_spelling_grp) {
		this.chi_spelling_grp = chi_spelling_grp;
	}
	private double shares_per_hand;//每手股数
	private double rise_count;//上涨家数
	private double fall_count;//下跌家数
	private String member_count;//成员个数
	private String special_marker;//特殊标志
	private String exercise_date;//行权日期
	private String exercise_price;//行权价格
	private String contract_unit;//合约乘数
	private String contract_code;//合约代码
	private String call_put;//C：认购；P：认沽
	private String exercise_type;//行权方式：若为欧式期权，则本字段为“E”；若为美式期权，则本字段为“A”
	private String start_trade_date;;//首个交易日
	private String end_trade_date;//最后交易日
	private String expire_date;//到期日
	private String security_status_flag;//期权合约状态信息
	private String tick_size;//最小报价单位
	private String trading_phase_code;//产品实施标志
	private String auction_price;//动态参考价格
	private String matched_qty;//虚拟匹配数量
	private String min5_chgpct;//五分钟涨跌幅
	private String issue_date;//上市日期
	private String ipo_price;//IPO价格
	private String chi_spelling_grp;//当该股票有多个拼音简称时，返回结果中，以英文半角逗号分隔，形如“XXXX,YYYY,ZZZZ”
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
