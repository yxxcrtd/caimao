package com.caimao.gjs.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class CommonUtils {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * 合并图片
     * @param fileOnePath 图片1
     * @param fileTwoPath 图片2
     * @return 输入流
     * @throws Exception
     */
    public static InputStream mergerImg(String fileOnePath, String fileTwoPath) throws Exception {
        try {
            //第一张图片
            File fileOne = new File(fileOnePath);
            BufferedImage ImageOne = ImageIO.read(fileOne);
            int width = ImageOne.getWidth();
            int height = ImageOne.getHeight();
            int[] ImageArrayOne = new int[width * height];
            ImageArrayOne = ImageOne.getRGB(0, 0, width, height, ImageArrayOne, 0, width);
            //第二张图片
            File fileTwo = new File(fileTwoPath);
            BufferedImage ImageTwo = ImageIO.read(fileTwo);
            int[] ImageArrayTwo = new int[width * height];
            ImageArrayTwo = ImageTwo.getRGB(0, 0, width, height, ImageArrayTwo, 0, width);
            //生成新图片
            BufferedImage ImageNew = new BufferedImage(width, height * 2, BufferedImage.TYPE_INT_RGB);
            ImageNew.setRGB(0, 0, width, height, ImageArrayOne, 0, width);
            ImageNew.setRGB(0, height, width, height, ImageArrayTwo, 0, width);

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            ImageIO.write(ImageNew, "jpg", os);

            return new ByteArrayInputStream(os.toByteArray());
        } catch (Exception e) {
            throw new Exception("合并图片错误");
        }
    }

    /**
     * post图片
     * @param is 输入流
     * @param fileName 文件名称
     * @param postUrl post地址
     * @return 响应结果
     * @throws Exception
     */
    public static String postImg(InputStream is, String fileName, String postUrl) throws Exception {
        String result = "";
        BufferedReader reader = null;

        try {
            // 换行符
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // 定义数据分隔线
            String BOUNDARY = "========" + System.currentTimeMillis();
            // 服务器的域名
            URL url = new URL(postUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());

            StringBuilder sb = new StringBuilder();
            // 上传文件
            //File file = new File("E:\\230902197109269617.jpg");
            sb.append(boundaryPrefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            // 文件参数,photo参数名可以随意修改
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\"").append(fileName).append(".jpg\"").append(newLine);
            sb.append("Content-Type:application/octet-stream");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);
            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());
            // 数据输入流,用于读取文件数据
            DataInputStream in = new DataInputStream(is);
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            // 每次读1KB数据,并且将文件数据写入到输出流中
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            // 最后添加换行
            out.write(newLine.getBytes());
            in.close();
            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine).getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("上金所文件上传失败， 失败原因{}", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 截取字符串
     * @param pString 字符串
     * @param start 前显示位数
     * @param end 后显示位数
     * @return String
     */
    public static String hideString(String pString, Integer start, Integer end, Boolean isMasks){
        if(pString != null && !pString.equals("") && pString.length() >= start + end){
            return pString.substring(0, start) + (isMasks?"********":"") + pString.substring(pString.length() - end, pString.length());
        }else{
            return null;
        }
    }

    /**
     * 获取随机数
     * @param min 最小数
     * @param max 最大数
     * @return 随机数
     */
    public static Integer getRandomNum(Integer min, Integer max){
        Random random = new Random();
        return random.nextInt(max)%(max - min + 1) + min;
    }
}
