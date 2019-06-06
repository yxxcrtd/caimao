package com.caimao.hq.api.base.page;

/**
 * @author scott
 * 2013年12月19日21:37:44
 * desc：分页对象
 */
public class Page {
	// 分页查询开始记录位置
	private int begin;
	// 分页查看结束位置
	private int end;
	// 每页显示记录数
	private int length;
	// 查询结果总记录数
	private int count;
	// 当前页码
	private int current;
	// 总共页数
	private int total;

	public Page() {
		this.begin = 0;
		this.length = 10;
		this.end = 10;
		this.current = 0;
	}

	/**
	 * 构造函数
	 * 
	 * @param begin
	 * @param length
	 */
	public Page(int begin, int length) {
		this.begin = begin;
		this.length = length;
		this.end = this.begin + this.length;
		this.current = (int) Math.floor((this.begin * 1.0d) / this.length);
	}

	/**
	 * @param begin
	 * @param length
	 * @param count
	 */
	public Page(int begin, int length, int count) {
		this(begin, length);
		this.count = count;
	}

	/**
	 * @return the begin
	 */
	public int getBegin() {
		return begin;
	}

	/**
	 * @return the end
	 */
	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public void setBegin(int begin) {
		this.begin = begin;
		if (this.length != 0) {
			this.current = (int) Math.floor((this.begin * 1.0d) / this.length);
		}
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(int length) {
		this.length = length;
		if (this.begin != 0) {
			this.current = (int) Math.floor((this.begin * 1.0d) / this.length);
		}
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
		this.total = (int) Math.floor((this.count * 1.0d) / this.length);
		if (this.count % this.length != 0) {
			this.total++;
		}
	}

	/**
	 * @return the current
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * @param current
	 *            the current to set
	 */
	public int getTotal() {
		if (total == 0) {
			return 1;
		}
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}
}
