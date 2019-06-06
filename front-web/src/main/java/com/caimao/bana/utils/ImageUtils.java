package com.caimao.bana.utils;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.caimao.bana.common.api.exception.CustomerException;

/**
 * 图片上传的东东
 * Created by WangXu on 2015/6/1.
 */
public class ImageUtils {
    public ImageUtils() {
    }

    // 上传用户头像
    public static String getPreFilePathForAvatar(Long userId, MultipartHttpServletRequest multipartRequest, String tempPath) throws Exception {
        String fileReName = null;
        MultipartFile file = multipartRequest.getFile("file");
        if(file == null) {
            throw new CustomerException("上传头像失败", 888888);
        } else {
            String fileName = file.getOriginalFilename();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            ext = ext.toLowerCase();
            List fileType = ImageUtils.uploandFileType();
            if(!fileType.contains(ext)) {
                throw new CustomerException("上传的图片格式不正确", 888888);
            } else {
                fileReName = userId + "." + ext;
                File imageFile = new File(tempPath, fileReName);
                if(imageFile.exists()) {
                    imageFile.delete();
                }
                if(!imageFile.getParentFile().exists()) {
                    imageFile.getParentFile().mkdirs();
                }
                if(!imageFile.exists()) {
                    try {
                        imageFile.createNewFile();
                    } catch (IOException var3) {
                        var3.printStackTrace();
                    }
                }
                try {
                    FileOutputStream fops = new FileOutputStream(imageFile);
                    fops.write(file.getBytes());
                    fops.flush();
                    fops.close();
                } catch (IOException var11) {
                    var11.printStackTrace();
                }

                return fileReName;
            }
        }
    }

    private static List<String> uploandFileType() {
        ArrayList fileTypes = new ArrayList();
        fileTypes.add("jpg");
        fileTypes.add("jpeg");
        fileTypes.add("bmp");
        fileTypes.add("gif");
        fileTypes.add("png");
        return fileTypes;
    }

    public static void cutCenterImage(String src, String dest, int w, int h) throws IOException {
        Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
        ImageReader reader = (ImageReader)iterator.next();
        FileInputStream in = new FileInputStream(src);
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        byte imageIndex = 0;
        Rectangle rect = new Rectangle((reader.getWidth(imageIndex) - w) / 2, (reader.getHeight(imageIndex) - h) / 2, w, h);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        ImageIO.write(bi, "jpg", new File(dest));
    }

    public static void cutHalfImage(String src, String dest) throws IOException {
        Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
        ImageReader reader = (ImageReader)iterator.next();
        FileInputStream in = new FileInputStream(src);
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        byte imageIndex = 0;
        int width = reader.getWidth(imageIndex) / 2;
        int height = reader.getHeight(imageIndex) / 2;
        Rectangle rect = new Rectangle(width / 2, height / 2, width, height);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        ImageIO.write(bi, "jpg", new File(dest));
    }

    public static void cutImage(String src, String dest, int x, int y, int w, int h) throws Exception {
        String imageType = src.substring(src.lastIndexOf(".") + 1, src.length());
        Iterator iterator = ImageIO.getImageReadersByFormatName(imageType);
        ImageReader reader = (ImageReader)iterator.next();
        FileInputStream in = new FileInputStream(src);
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(x, y, w, h);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        File destFile = new File(dest);
        if(destFile.exists()) {
            destFile.delete();
        }
        if(!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        if(!destFile.exists()) {
            try {
                destFile.createNewFile();
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }
        ImageIO.write(bi, imageType, destFile);
    }

    public static void zoomImage(String src, String dest, int w, int h) throws Exception {
        double wr = 0.0D;
        double hr = 0.0D;
        File srcFile = new File(src);
        File destFile = new File(dest);
        BufferedImage bufImg = ImageIO.read(srcFile);
        bufImg.getScaledInstance(w, h, 4);
        wr = (double)w * 1.0D / (double)bufImg.getWidth();
        hr = (double)h * 1.0D / (double)bufImg.getHeight();
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), (RenderingHints)null);
        BufferedImage Itemp = ato.filter(bufImg, (BufferedImage)null);

        try {
            ImageIO.write((BufferedImage)Itemp, dest.substring(dest.lastIndexOf(".") + 1), destFile);
        } catch (Exception var14) {
            var14.printStackTrace();
        }

    }

    public static String compress(InputStream input, int width, int height) throws IOException {
        if(input == null) {
            throw new IOException("输入流为空");
        } else {
            BufferedImage img = ImageIO.read(input);
            if(img == null) {
                return null;
            } else if(img.getWidth((ImageObserver)null) == -1) {
                throw new IOException("无法从输入流中解析");
            } else {
                double imgwidth = (double)img.getWidth((ImageObserver)null);
                double imgheigh = (double)img.getHeight((ImageObserver)null);
                double widthrate = imgwidth / (double)width + 0.1D;
                double heightrate = imgheigh / (double)height + 0.1D;
                double allrate = widthrate > heightrate?widthrate:heightrate;
                int newWidth = (int)(imgwidth / allrate);
                int newHeight = (int)(imgheigh / allrate);
                BufferedImage tag = new BufferedImage(newWidth, newHeight, 1);
                tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, 4), 0, 0, (ImageObserver)null);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
                //encoder.encode(tag);
                String newImageBase = Base64.encodeBase64String(outputStream.toByteArray());
                return newImageBase;
            }
        }
    }

    public static String compress(byte[] input, int width, int height) throws IOException {
        if(input == null) {
            throw new IOException("输入流为空");
        } else {
            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(input);
            return compress((InputStream)arrayInputStream, width, height);
        }
    }

    public static byte[] decompression(String date) {
        return Base64.decodeBase64(date);
    }

    public static String encodeImage(byte[] image) {
        return Base64.encodeBase64String(image);
    }

    public static void main(String[] arg) throws IOException {
        int count = 0;

        for(int i = 0; i < 10; ++i) {
            FileInputStream fileInputStream = new FileInputStream("e:\\avatars\\123.jpg");
            int start = (int)System.currentTimeMillis();
            FileOutputStream fileOutputStream = new FileOutputStream("e:\\avatars\\" + i + ".bmp");
            String newImageBase = compress((InputStream)fileInputStream, 1024, 1024);
            fileOutputStream.write(Base64.decodeBase64(newImageBase));
            fileOutputStream.close();
            fileOutputStream.close();
            fileInputStream.close();
            int end = (int)System.currentTimeMillis();
            int re = end - start;
            count += re;
            System.out.println("第" + (i + 1) + "时间" + re + "毫秒");
        }

        System.out.println("总计：" + count + "毫秒");
    }
}
