package com.caimao.jserver.server.core;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.caimao.jserver.hq.njs.NJSHQPlugin;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class HQServer{


	private static List<HQPlugin> plugins = new ArrayList<HQPlugin>();
	private static Logger logger = Logger.getLogger(HQServer.class);
	static {
		init();
	}

	/**
	 * 初始化,会从classpath下加载HQServer.xml
	 */
	private static void init() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = HQServer.class.getResourceAsStream("/META-INF/socket/HQServer.xml");
			Document doc = db.parse(new InputSource(is));
			NodeList list=doc.getElementsByTagName("server");
			if(list==null){
				return;
			}
			for (int i = 0; i < list.getLength(); i++) {
				Element el=(Element) list.item(i);
				String valid=el.getAttribute("valid");
				if("false".equals(valid)){
					continue;
				}
				String cls=el.getAttribute("class");
				HQPlugin plugin=(HQPlugin) Class.forName(cls).newInstance();
				plugin.init(el);
				plugins.add(plugin);
			}

		} catch (Exception ex){
			logger.error("初始化连接失败:"+ex.getMessage());
		}
	}

	/**
	 * HQ服务启动
	 */
	public static void start() {
		logger.error("行情插件启动:");
		for (HQPlugin plugin : plugins) {
			plugin.init(plugin.getEl());
			plugin.start();
			logger.error("行情插件启动:" + plugin.getName());
		}
	}


	/**
	 * HQ服务启动
	 */
	public static void start(String serverName) {
		logger.error("行情插件启动:" + serverName);
		for (HQPlugin plugin : plugins) {
			if(null!=plugin&&plugin.getName().equalsIgnoreCase(serverName)){
				plugin.init(plugin.getEl());
				plugin.start();

			}
		}
		logger.error("行情插件结束:" + serverName);
	}

	/**
	 * HQ服务关闭
	 */
	public static void stop() {
		for (HQPlugin plugin : plugins) {
			plugin.stop();
			logger.error("行情插件关闭:" + plugin.getName());
		}
	}

	public static void main(String[] args) {
		HQServer.start();
	}
}
