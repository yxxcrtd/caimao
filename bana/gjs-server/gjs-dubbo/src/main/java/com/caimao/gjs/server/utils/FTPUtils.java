package com.caimao.gjs.server.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * FTP 工具类
 *
 * Created by yangxinxin@huo.com on 2015/11/13
 */
public class FTPUtils {

    /** 日志*/
    private static final Logger logger = LoggerFactory.getLogger(FTPUtils.class);

    /** FTPClient 对象*/
    private static FTPClient ftpClient;

    /**
     * 连接FTP
     *
     * @param ip
     * @param username
     * @param password
     */
    public static void connect(String ip, String username, String password, String localPath, String fileName) {
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ip); // 连接FTP
            ftpClient.login(username, password); // 登录FTP
            FTPClientConfig config = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
            ftpClient.configure(config);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setBufferSize(1024 * 2);
            ftpClient.setDataTimeout(10 * 60 * 1000); // 10分钟超时
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.error("FTP连接失败，响应Code：" + reply);
            } else {
                logger.info("FTP连接成功！");

                // 下载文件
                if (getFile(new File(fileName), localPath + File.separator + fileName)) {
                    logger.info("文件下载成功！");
                } else {
                    logger.error("文件下载失败！");
                }
                // 关闭FTP
                close();
            }
        } catch (IOException e) {
            logger.error("FTP连接出错信息：", e);
        }
    }

    /**
     * 从ftp服务器下载文件
     *
     * @param f 要获取的ftp服务器上的文件
     * @param localPath 本地存放的路径
     *
     * @return boolean 文件下载是否成功
     * */
    private static boolean getFile(File f, String localPath) {
        OutputStream outStream = null;
        boolean result = false;
        try{
            try{
                outStream = new BufferedOutputStream(new FileOutputStream(new File(localPath)));
                FTPFile[] fs = ftpClient.listFiles();
                for (FTPFile f1 : fs) {
                    if (f1.getName().equals(f.getName())) {
                        ftpClient.enterLocalPassiveMode(); // 设置为被动模式，否则会一直超时
                        result = ftpClient.retrieveFile(f1.getName(), outStream); // 文件下载
                    }
                }
            } finally {
                if (null != outStream) {
                    outStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 关闭FTP
     */
    private static void close() {
        if (null != ftpClient && ftpClient.isConnected()) {
            try {
                boolean result = ftpClient.logout(); // 退出FTP
                if (result) {
                    logger.info("FTP退出成功！");
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("退出FTP出现异常：" + e.getMessage());
            } finally {
                try {
                    ftpClient.disconnect(); // 关闭FTP连接
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("关闭FTP出现异常：" + e.getMessage());
                }
            }
        }
    }

}
