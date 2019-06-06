package demo;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class TestFileChange {
    private void changeFile(String name, String filePath) throws Exception{
        String txtStr = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            if (line.indexOf("dubbo.group=") == 0) {
                txtStr += "dubbo.group=test_" + name + "\r\n";
            }else if (line.indexOf("group=") == 0) {
                txtStr += "group=test_" + name + "\r\n";
            } else {
                txtStr += line + "\r\n";
            }
        }
        FileWriter fw = new FileWriter(filePath);
        fw.write(txtStr);
        fw.close();
        System.out.println("文件 " + filePath + " 修改完毕");
    }

    @Test
    public void testChangeFile() throws Exception {
        String name = "nh";
        String folder = "D:\\java_work\\caimao\\";
        List<String> fileList = new ArrayList<>();
        fileList.add("bana\\bana-server\\bana-dubbo\\src\\main\\resources\\META-INF\\conf\\dubbo.properties");
        fileList.add("bana\\gjs-server\\gjs-dubbo\\src\\main\\resources\\META-INF\\conf\\dubbo.properties");
        fileList.add("bana\\gjshq-server\\gjshq-dubbo\\src\\main\\resources\\META-INF\\conf\\dubbo.properties");
        fileList.add("bana\\account-server\\account-dubbo\\src\\main\\resources\\META-INF\\conf\\dubbo.properties");
        fileList.add("fmall-web\\src\\main\\resources\\dubbo-conf.properties");
        fileList.add("front-web\\src\\main\\resources\\dubbo-conf.properties");
        fileList.add("bana\\zeus\\src\\main\\resources\\conf\\dubbo-conf.properties");
        fileList.add("bana\\bana-jobs\\src\\main\\resources\\conf\\dubbo-conf.properties");
        for (String filePath:fileList) this.changeFile(name, folder + filePath);
    }
}