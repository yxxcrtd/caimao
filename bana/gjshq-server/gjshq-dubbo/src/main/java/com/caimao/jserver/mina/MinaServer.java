package com.caimao.jserver.mina;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.*;

public class MinaServer {

    private static final Logger logger = LoggerFactory.getLogger(MinaServer.class);

    private static Map<String, MinaPlugin> plugins = new HashMap<>();
    private static Map<String, List<String>> groups = new HashMap<>();

    static {
        init();
    }

    private static void init() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputStream is = MinaServer.class.getResourceAsStream("/META-INF/socket/MinaTradeServer.xml");
            Document doc = db.parse(new InputSource(is));
            NodeList list = doc.getElementsByTagName("server");
            if (list == null) {
                return;
            }
            for (int i = 0; i < list.getLength(); i++) {
                Element el = (Element) list.item(i);
                String valid = el.getAttribute("valid");
                if ("false".equals(valid)) {
                    continue;
                }
                String cls = el.getAttribute("class");
                MinaPlugin plugin = (MinaPlugin) Class.forName(cls).newInstance();
                plugin.init(el);
                plugins.put(plugin.getName(), plugin);

                String groupName = el.getAttribute("group");
                if(groupName != null && !groupName.equals("")){
                    List<String> pluginNameList = groups.get(groupName);
                    if(pluginNameList == null){
                        pluginNameList = new ArrayList<>();
                    }
                    pluginNameList.add(plugin.getName());
                    groups.put(groupName, pluginNameList);
                }
            }
        } catch (Exception e) {
            logger.error("socket服务启动失败，失败原因{}", e);
        }
    }

    public static MinaPlugin getPluginGroup(String name){
        List<String> pluginNameList = groups.get(name);
        Random rand = new Random();
        String pluginName = pluginNameList.get(rand.nextInt(pluginNameList.size()));
        return plugins.get(pluginName);
    }

    public static MinaPlugin getPlugin(String name) {
        return plugins.get(name);
    }

    /**
     * 交易服务启动
     */
    public static void start() {
        Iterator<Map.Entry<String, MinaPlugin>> it = plugins.entrySet().iterator();
        for (; it.hasNext(); ) {
            it.next().getValue().start();
        }
    }

    /**
     * 交易服务关闭
     */
    public static void stop() {
        Iterator<Map.Entry<String, MinaPlugin>> it = plugins.entrySet().iterator();
        for (; it.hasNext(); ) {
            it.next().getValue().stop();
        }
    }
}
