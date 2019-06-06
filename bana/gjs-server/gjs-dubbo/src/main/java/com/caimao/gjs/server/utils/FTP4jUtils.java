package com.caimao.gjs.server.utils;

import it.sauronsoftware.ftp4j.FTPClient;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class FTP4jUtils {
    private static final Logger logger = LoggerFactory.getLogger(FTPUtils.class);

    public static FTPClient connect(String ip, String username, String password) {
        FTPClient client = new FTPClient();
        try{
            client.connect(ip);
            client.login(username, password);
            client.setCharset("GBK");
            client.setType(FTPClient.TYPE_BINARY);
        }catch(Exception e){
            logger.error("FTP连接出错信息：", e);
        }
        return client;
    }

    public static boolean closeConn(FTPClient client) {
        if (client == null) return true;
        if (client.isConnected()) {
            try {
                client.disconnect(true);
                return true;
            } catch (Exception e) {
                try {
                    client.disconnect(false);
                } catch (Exception ignored) {}
            }
        }
        return true;
    }

    public static void download(String ip, String username, String password, String localFolderPath, String remoteFileName) {
        FTPClient client = connect(ip, username, password);
        try {
            List<String> filesList = new ArrayList<>();
            String[] fileNames = client.listNames();
            logger.info("文件名称列表：" + ToStringBuilder.reflectionToString(fileNames));
            if (null != fileNames) {
                Collections.addAll(filesList, fileNames);
            }
            if(filesList.contains(remoteFileName)){
                logger.error("文件 " + remoteFileName + " 存在, 开始下载");
                File localFolder = new File(localFolderPath);
                if (!localFolder.exists()) {
                    localFolder.mkdirs();
                }
                String localFilePath = localFolderPath + File.separator + remoteFileName;
                try{
                    client.download(remoteFileName, new File(localFilePath));
                    logger.info("文件下载成功！");
                }catch(Exception e){
                    logger.info("文件下载失败！", e);
                }
            }else{
                logger.error("文件 " + remoteFileName + " 不存在");
            }
        } catch (Exception e) {
            logger.error("FTP下载文件出错：", e);
        }
        closeConn(client);
    }
}
