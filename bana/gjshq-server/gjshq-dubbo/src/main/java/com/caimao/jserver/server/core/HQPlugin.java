package com.caimao.jserver.server.core;

import org.w3c.dom.Element;

/**
 * 行情服务插件
 * @author lvqi
 *
 */
public interface HQPlugin {
	/**
	 * @return
	 */
	public String getName();
	public void init(Element el);
	/**
	 * 启动连接
	 */
	public void start();
	/**
	 * 关闭
	 */
	public void stop();


	public  Element getEl();
}
