package com.caimao.gjs.server.trade.mina;

import org.w3c.dom.Element;

public interface MinaPlugin{
    public String getName();

    public void init(Element el);

    public void start();

    public void stop();

    public void restart();

    public Object sendAndRev(String adapter, Object message);

    public void send(String adapter, String message);
}
