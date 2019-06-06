package com.caimao.jserver.server.core;

import org.w3c.dom.Element;

/**
 * 交易服务插件
 * @author lvqi
 *
 */
public interface TradePlugin {
	/**
	 * 插件名称，调用者根据这个从TradeServer中拿到这个插件进行交易服务调用
	 * @return
	 */
	public String getName();
	/**
	 * 初始化
	 * @param el XML节点的信息
	 */
	public void init(Element el);
	/**
	 * 插件启动连接
	 */
	public void start();
	/**
	 * 插件停止
	 */
	public void stop();
	/**
	 * 同步调用交易服务
	 * @param service 服务名
	 * @param request 服务参数对象
	 * @return 结果对象
	 */
	public Object sendAndRev(String service, Object request);
	
	/**
	 * 同步调用交易服务
	 * @param service 服务名
	 * @return 结果对象
	 */
	public Object sendAndRev(String service);
	/**异步调用或者无返回调用
	 * @param service 服务名
	 * @param request 服务参数对象
	 */
	public void send(String service, Object request);
	
}
