/**
 * 
 */
package com.caimao.j2cache;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import net.oschina.j2cache.J2Cache;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 缓存测试入口
 * @author Winter Lau
 */
public class CacheTester {

	public static void main(String[] args) {
		CacheChannel cache = J2Cache.getChannel();
		cache.set("cache1","key1","OSChina.net");
		cache.get("cache1","key1");
		System.setProperty("java.net.preferIPv4Stack", "true"); //Disable IPv6 in JVM

	}
	
	private static void printHelp() {
		System.out.println("Usage: [cmd] region key [value]");
		System.out.println("cmd: get/set/evict/quit/exit/help");
	}

}
