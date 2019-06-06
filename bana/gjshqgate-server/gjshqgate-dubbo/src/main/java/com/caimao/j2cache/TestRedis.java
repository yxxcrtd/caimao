package com.caimao.j2cache;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.J2Cache;

/**
 * Created by yzc on 2015/12/22.
 */
public class TestRedis {

    public static void main(String args[]){


        CacheChannel cache = J2Cache.getChannel();
        cache.set("cache1","key1","OSChina.net");
        cache.get("cache1","key1");
    }
}
