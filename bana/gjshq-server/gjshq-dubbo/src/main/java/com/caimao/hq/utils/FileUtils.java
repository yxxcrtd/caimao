package com.caimao.hq.utils;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FileUtils {
    static org.slf4j.Logger candle_njs_log = LoggerFactory.getLogger("candle_njs_log");
    public String[] readfile(String filepath) throws Exception {
        FileReader fr = new FileReader(filepath);
        // 将无法识别的字节赋值为'?'
        int c = 63;
        String errmessage = "文件编码不是GBK，不能解析";
        try {
            // 从文件中读取一个字符
            c = fr.read();
        } catch (Exception e) {
            try {
                fr.skip(1);
            } catch (Exception ex) {
                throw new Exception(errmessage, ex);
            }
            c = 63;
        }
        StringBuffer sb = new StringBuffer();
        List list = new ArrayList();
        while (c != -1) {
            // 遇到回车符时保存该行内容，刷新缓存
            if (c == 10) {
                list.add(sb.toString());
                sb = new StringBuffer();
                try {
                    // 从文件中继续读取数据
                    c = fr.read();
                } catch (Exception e) {
                    try {
                        fr.skip(1);
                    } catch (Exception ex) {
                        throw new Exception(errmessage, ex);
                    }
                    c = 63;
                }
                continue;
            }
            sb.append((char) c);
            try {
                // 从文件中继续读取数据
                c = fr.read();
            } catch (Exception e) {
                try {
                    fr.skip(1);
                } catch (Exception ex) {
                    throw new Exception(errmessage, ex);
                }
                c = 63;
            }
        }
        // 保存最后一行内容
        if (c == -1 && sb.length() > 0) {
            list.add(sb.toString());
        }
        fr.close();
        // 返回从文本文件中读取的内容
        Object[] obj = list.toArray();
        String[] objs = new String[obj.length];
        for (int i = 0; i < obj.length; i++) {
            objs[i] = (String) obj[i];
        }
        return objs;
    }
    private static Logger logger = Logger.getLogger(FileUtils.class);

    /**
     * @param path
     *            文件路径
     * @param suffix
     *            后缀名, 为空则表示所有文件
     * @param isdepth
     *            是否遍历子目录
     * @return list
     */
    public static List<String> getListFiles(String path, String suffix, boolean isdepth) {
        List<String> lstFileNames = new ArrayList<String>();
        File file = new File(path);
        return FileUtils.listFile(lstFileNames, file, suffix, isdepth);
    }

    private static List<String> listFile(List<String> lstFileNames, File f, String suffix, boolean isdepth) {
        // 若是目录, 采用递归的方法遍历子目录
        if (f.isDirectory()) {
            File[] t = f.listFiles();

            for (int i = 0; i < t.length; i++) {
                if (isdepth || t[i].isFile()) {
                    listFile(lstFileNames, t[i], suffix, isdepth);
                }
            }
        } else {
            String filePath = f.getAbsolutePath();
            if (!suffix.equals("")) {
                int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
                String tempsuffix = "";

                if (begIndex != -1) {
                    tempsuffix = filePath.substring(begIndex + 1, filePath.length());
                    if (tempsuffix.equals(suffix)) {
                        lstFileNames.add(filePath);
                    }
                }
            } else {
                lstFileNames.add(filePath);
            }
        }
        return lstFileNames;
    }
    public static void main(String[] args) {

        for(int i=0;i<10;i++){
            candle_njs_log.info("test candle_njs_log log");
        }

    }

    public static void appendWrite(String fileName, String content) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content+System.getProperty("line.separator"));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Boolean isWinOs(){

        Boolean isWinOs=false;
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        if(os.startsWith("win") || os.startsWith("Win")) {

            isWinOs=true;
        }else{
            isWinOs=false;
        }
        return isWinOs;
    }

    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            return true;
        } else {
            return false;
        }
    }


}