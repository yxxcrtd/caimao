/*
*MemoryDbidGenerator.java
*Created on 2015/4/23 15:57
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server.utils;

/**
 * @author Administrator
 * @version 1.0.1
 */
public class MemoryDbidGenerator {
    private long nextId = 0L;
    private long serverId = 1L;
    private long preCalculateTime = 0L;

    public synchronized Long getNextId() {
        if (System.currentTimeMillis() - this.preCalculateTime > 60000L) {
            this.preCalculateTime = (System.currentTimeMillis() / 60000L);
            this.nextId = (this.preCalculateTime << (int) (4L + this.serverId) << 20);
            this.preCalculateTime *= 60000L;
        }
        return Long.valueOf(++this.nextId);
    }

    public void setServerId(long serverId) {
        if ((serverId < 1L) || (serverId > 16L)) {
            throw new IllegalArgumentException("The serverId of MemoryDbidGenerator must more 0 and less 16,  but it is " + serverId);
        }
        this.serverId = serverId;
    }
}
