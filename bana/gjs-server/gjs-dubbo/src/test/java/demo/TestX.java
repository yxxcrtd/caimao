package demo;

import com.alibaba.fastjson.JSON;
import com.caimao.gjs.server.trade.protocol.njs.entity.NJSQueryFundsEntity;
import com.caimao.gjs.server.trade.protocol.njs.entity.res.FNJSQueryFundsRes;
import com.caimao.gjs.server.trade.protocol.njs.entity.res.FNJSQueryWareAllRes;
import com.caimao.gjs.server.trade.protocol.sjs.entity.req.FSJSDoBankTransferReq;
import com.caimao.gjs.server.trade.protocol.sjs.entity.res.FSJSQueryFundsRes;
import com.caimao.gjs.server.utils.IdCardUtils;
import com.caimao.gjs.server.utils.XMLForBean;
import com.caimao.gjs.server.utils.CommonUtils;
import com.google.gson.Gson;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.pojoxml.core.PojoXml;
import org.pojoxml.core.PojoXmlFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestX {
    @Test
    public void testFor() throws Exception{
        System.out.println(Boolean.parseBoolean("true"));
        /*int s = 1;
        for (int i = 0; i<10000000; i++){
            s+=1;
        }
        System.out.println(s);*/
    }

    @Test
    public void testWeek() throws Exception{
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        System.out.println("今天星期：" + w);
        System.out.println("现在是几点：" + cal.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public  void testLogin() throws Exception{
        System.out.print(String.format("%-8s", "huobi"));
    }

    @Test
    public void testIndex() throws Exception{
        String aaa = "{\"ADAPTER\":\"TDEOP1012\",\"STATE\":\"6\",\"MSG\":\"您尚未登陆、请您先登陆！\"}{\"ADAPTER\":\"TDEOP1012\",\"STATE\":\"13\",\"MSG\":\"请您重新输入正确的交易商密码!\"}";
        System.out.println(aaa.substring(0, aaa.indexOf("}{") + 1));
    }

    @Test
    public void testFormatDate() throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String validDate = df.format(new Date(System.currentTimeMillis() + 5 * 86400 * 1000));
        System.out.println(validDate);
    }

    @Test
    public void testRemove() throws Exception{
        Map<String, String> testMap = new HashMap<>();
        testMap.put("2", "2");
        testMap.put("3", "3");
        testMap.put("4", "4");
        System.out.println(testMap.remove("2"));

    }

    @Test
    public void testXXC() throws Exception{
        String xx = "00483A1448259292993[{\"BAILMONEY\":\"0.00\",\"BUYORSAL\":\"S\",\"CCANCLETYPE";
        System.out.println(xx.substring(19));
    }

    @Test
    public void testW() throws Exception{
        String numStr = "123!@#$%^&&中号**";
        System.out.println(Pattern.compile("\\w+\\W*\\S+").matcher(numStr).matches());
    }

    @Test
    public void testUrlParams() throws Exception{
        Map<String, String> dataMap = new HashMap<>();
        String aa = "#MsgType=2#CheckFlag=0##ApiName=onRecvDeferQuotation#DataType=TDeferQuotation#Flag=L#NodeID=6031#NodeType=6#Posi=184962#RootID=#RspCode=RSP000000#RspMsg=交易成功#Ts_NodeID=1003#ask1=223.72000000#ask2=223.73000000#ask3=223.75000000#ask4=223.76000000#ask5=223.77000000#askLot1=11#askLot2=2#askLot3=81#askLot4=3#askLot5=2#average=223.64000000#bid1=223.70000000#bid2=223.69000000#bid3=223.67000000#bid4=223.66000000#bid5=223.65000000#bidLot1=39#bidLot2=2#bidLot3=5#bidLot4=2#bidLot5=2#close=0.00000000#high=223.92000000#highLimit=237.49000000#instID=Au(T+D)#last=223.70000000#lastClose=224.24000000#lastSettle=224.05000000#low=223.08000000#lowLimit=210.60000000#name=黄金延期#open=223.61000000#quoteDate=20151112#quoteTime=11:00:08#sequenceNo=47162#settle=0.00000000#turnOver=4967608520.00000000#upDown=-0.35000000#upDownRate=0.00156215#volume=22212#weight=22212.00000000#ApdSckCode=1254968013#ApdRecvTime=1447296784027#";

        for (String subData:aa.split("#")){
            String[] params = subData.split("=");
            if(params.length == 2){
                dataMap.put(params[0], params[1]);
            }
        }
        System.out.println(dataMap);
    }

    @Test
    public void testByte() throws Exception{
        String a = "00483A14470358122";

        System.out.println(a.getBytes().length);
    }

    @Test
    public void testIdCard() throws Exception{
        String asd = "123123";
        System.out.println(asd.substring(0, 0));

        //System.out.println(Pattern.compile("[0-9]*").matcher("").matches());

        //String idCard = "142322198906193011";
        //System.out.println(IdCardUtils.IDCardValidate(idCard));
    }

    @Test
    public void testList() throws Exception{
        List<String> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            list.add("xx" + i);
        }
        System.out.println(list);
        Collections.reverse(list);
        System.out.println(list);
    }

    @Test
    public void test() throws Exception {
        String testString = "{\"ADAPTER\":\"011009\",\"STATE\":\"0\",\"MSG\":28,\"DATAS\":[{\"WAREID\":\"AG\",\"WARENAME\":\"白银\",\"SWAREKIND\":\"D1\",\"SKINDNAME\":\"白银\",\"WPOINT\":\"1.00\"},{\"WAREID\":\"BI\",\"WARENAME\":\"铋\",\"SWAREKIND\":\"D3\",\"SKINDNAME\":\"铋\",\"WPOINT\":\"1.00\"}]}";
        FNJSQueryWareAllRes content = JSON.parseObject(testString, FNJSQueryWareAllRes.class);


        System.out.println(ToStringBuilder.reflectionToString(content));


        String headBytes = "016005000030NN0000000021";
        Integer dataLen = Integer.parseInt(new String(headBytes.getBytes(), 6, 6));
        System.out.println(dataLen);

    }

    @Test
    public void testSub() throws Exception{
        String sss = "$('#exch_pwd').val('CmJvxgPEwAWf'); $('#fund_pwd').val('izl6v386k459');";

        Matcher m = null;

        m = Pattern.compile("\\$\\(\'#exch_pwd\'\\)\\.val\\(\'(.*?)\'\\);").matcher(sss);
        while (m.find()) {System.out.println(m.group(1));break;}

        m = Pattern.compile("\\$\\(\'#fund_pwd\'\\)\\.val\\(\'(.*?)\'\\);").matcher(sss);
        while (m.find()) {System.out.println(m.group(1));break;}

        String traderId = "";
        m = Pattern.compile("<b style=\"color:#096;\">(.*?)</b>").matcher("<b style=\"color:#096;\">1080130700</b>");
        while (m.find()) {traderId = m.group(1);break;}
        System.out.println(traderId);



        //String sss = "http://113.106.63.156:40080/openacc_szgold/trade/identity";
        //System.out.println(sss.substring(sss.indexOf("/o")));
    }

    @Test
    public void testZero() throws Exception{
        String str = "000000030";
        String xxx = str.replaceFirst("^0*", "");
        System.out.println(xxx);
        System.out.println(Integer.valueOf(xxx));
    }

    @Test
    public void testParse() throws Exception{
        String callbackParam = "sss-ss_2222";
        callbackParam = callbackParam.replaceAll("[^0-9a-zA-Z_-]", "");

        System.out.println(callbackParam);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date formatDate = sdf.parse("20120214" + "121212");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String xxx = sdf2.format(formatDate);
        System.out.println(xxx);
    }

    @Test
    public void testBeanToXml() throws Exception {
        FSJSDoBankTransferReq req = new FSJSDoBankTransferReq();
        req.setAccess_way("11111");
        req.setExch_bal("22222");
        req.setFund_pwd("33333");

        PojoXml pojoxml = PojoXmlFactory.createPojoXml();
        pojoxml.setEncoding("GBK");
        pojoxml.addClassAlias(req.getClass(), "record");
        String xml = pojoxml.getXml(req);
        System.out.println(xml);


        FSJSDoBankTransferReq req2 = new FSJSDoBankTransferReq();
        PojoXml pojoxml2 = PojoXmlFactory.createPojoXml();
        pojoxml.addClassAlias(req.getClass(), "FSJSDoBankTransferReq");
        Object x = pojoxml2.getPojo(xml, req2.getClass());
        System.out.println(ToStringBuilder.reflectionToString(x));


    }

    @Test
    public void testXX() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<response>\n" +
                "  <head>\n" +
                "    <h_branch_id>B0077001</h_branch_id>\n" +
                "    <h_exch_date></h_exch_date>\n" +
                "    <h_fact_date>20150911</h_fact_date>\n" +
                "    <h_rsp_code>HJ0000</h_rsp_code>\n" +
                "    <h_bank_no>0077</h_bank_no>\n" +
                "    <h_exch_code>102001</h_exch_code>\n" +
                "    <h_user_id>1080040687</h_user_id>\n" +
                "    <h_serial_no>0000000003</h_serial_no>\n" +
                "    <h_fact_time></h_fact_time>\n" +
                "    <h_rsp_msg>处理成功</h_rsp_msg>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <record>\n" +
                "      <real_buy>0</real_buy>\n" +
                "      <curr_bal>0</curr_bal>\n" +
                "      <take_margin>0</take_margin>\n" +
                "      <currency_id>1</currency_id>\n" +
                "      <float_surplus>0.00</float_surplus>\n" +
                "      <curr_can_get>0</curr_can_get>\n" +
                "      <curr_can_use>0</curr_can_use>\n" +
                "      <exch_froz_bal>0</exch_froz_bal>\n" +
                "      <pt_reserve>0</pt_reserve>\n" +
                "      <posi_margin>0</posi_margin>\n" +
                "      <in_bal>0</in_bal>\n" +
                "      <user_id>1080040687</user_id>\n" +
                "      <real_sell>0</real_sell>\n" +
                "      <ag_margin>0</ag_margin>\n" +
                "      <base_margin>0</base_margin>\n" +
                "      <exch_fare>0</exch_fare>\n" +
                "      <forward_froz>0</forward_froz>\n" +
                "      <stor_margin>0</stor_margin>\n" +
                "      <out_bal>0</out_bal>\n" +
                "    </record>\n" +
                "  </body>\n" +
                "</response>";

        FSJSQueryFundsRes res = XMLForBean.toObject(xml, FSJSQueryFundsRes.class);
        System.out.println(ToStringBuilder.reflectionToString(res.getRecord().get(0)));
    }

    @Test
    public void testImg() throws Exception {
        try {
            //第一张图片
            File fileOne = new File("E:\\1.jpg");
            BufferedImage ImageOne = ImageIO.read(fileOne);
            int width = ImageOne.getWidth();
            int height = ImageOne.getHeight();
            int[] ImageArrayOne = new int[width * height];
            ImageArrayOne = ImageOne.getRGB(0, 0, width, height, ImageArrayOne, 0, width);
            //第二张图片
            File fileTwo = new File("E:\\2.jpg");
            BufferedImage ImageTwo = ImageIO.read(fileTwo);
            int[] ImageArrayTwo = new int[width * height];
            ImageArrayTwo = ImageTwo.getRGB(0, 0, width, height, ImageArrayTwo, 0, width);
            //生成新图片
            BufferedImage ImageNew = new BufferedImage(width, height * 2, BufferedImage.TYPE_INT_RGB);
            ImageNew.setRGB(0, 0, width, height, ImageArrayOne, 0, width);
            ImageNew.setRGB(0, height, width, height, ImageArrayTwo, 0, width);
            File outFile = new File("E:\\4.jpg");
            ImageIO.write(ImageNew, "jpg", outFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("合成图片错误");
        }
    }

    @Test
    public void testUpload() throws Exception {
        try{
            // stjq48b2izdtt6e7lspb

            // 换行符
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // 定义数据分隔线
            String BOUNDARY = "========" + System.currentTimeMillis();
            // 服务器的域名
            URL url = new URL("http://210.21.197.124:51008/demo/gold/fileUpload.do");
            //URL url = new URL("http://127.0.0.1:8080/api/ybk/uploadIMG");
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
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\"230902197109269617.jpg\"" + newLine);
            sb.append("Content-Type:application/octet-stream");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);
            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());
            // 数据输入流,用于读取文件数据

            InputStream IS= CommonUtils.mergerImg("E:\\1.jpg", "E:\\1.jpg");

            DataInputStream in = new DataInputStream(IS);
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("出现错误。。");
        }
    }

    @Test
    public void testF() throws Exception{
        System.setProperty("fastjson.compatibleWithJavaBean", "true");
        FNJSQueryFundsRes rexx = new FNJSQueryFundsRes();
        rexx.setADAPTER("11111");
        rexx.setMSG("222222");
        rexx.setSTATE("3333333");
        List<NJSQueryFundsEntity> xxx = new ArrayList<NJSQueryFundsEntity>();
        NJSQueryFundsEntity asd1 = new NJSQueryFundsEntity();
        asd1.setASSETNETVALUE("111");
        asd1.setASSETTOTALVALUE("222");
        asd1.setBALANCEMONEY("333");
        NJSQueryFundsEntity asd2 = new NJSQueryFundsEntity();
        asd2.setASSETNETVALUE("444");
        asd2.setASSETTOTALVALUE("555");
        asd2.setBALANCEMONEY("666");
        xxx.add(asd1);
        xxx.add(asd2);
        System.out.println(xxx);
        rexx.setDATAS(xxx);

        Gson gson = new Gson();
        System.out.println(gson.toJson(rexx));

        String xxxasd = "{\"ADAPTER\":\"11111\",\"DATAS\":[{\"ASSETNETVALUE\":\"111\",\"ASSETTOTALVALUE\":\"222\",\"BALANCEMONEY\":\"333\"},{\"ASSETNETVALUE\":\"444\",\"ASSETTOTALVALUE\":\"555\",\"BALANCEMONEY\":\"666\"}],\"MSG\":\"222222\",\"STATE\":\"3333333\"}";

        FNJSQueryFundsRes obj = gson.fromJson(xxxasd, FNJSQueryFundsRes.class);
        System.out.println(obj.getDATAS().get(0).getBALANCEMONEY());


       /* FNJSBaseRes rexxsssx = JSON.parseObject(xxxasd, new TypeReference<FNJSBaseRes<NJSQueryFundsEntity>>(){});

        FNJSQueryFundsRes rexxx = JSON.parseObject(xxxasd, new TypeReference<FNJSQueryFundsRes>(){});

        NJSQueryFundsEntity ssss = (NJSQueryFundsEntity)rexxx.getDATAS().get(0);

        System.out.println(ssss.getBALANCEMONEY());

        System.out.println("000000000000000000000");
        String asd = "{\"ADAPTER\":\"011008\",\"STATE\":\"0\",\"MSG\":2,\"DATAS\":[{\"FDATE\":\"20151021\",\"FIRMID\":\"16321756\",\"INMONEY\":\"0.00\",\"OUTMONEY\":\"0.00\",\"FINANCING\":\"0.00\",\"FREEZEMONEY\":\"0.00\",\"BALANCEMONEY\":\"41.62\",\"BAILMONEY\":\"0.00\",\"ENABLEMONEY\":\"41.62\",\"ENABLEOUTMONEY\":\"6.62\",\"ASSETTOTALVALUE\":\"41.62\",\"ASSETNETVALUE\":\"41.62\",\"PICKGOODVALUE\":\"0.00\",\"FINANMARKETVAL\":\"0.00\",\"SAFERATE\":\"0.0000\"}]}";

        FNJSQueryFundsRes resx = JsonUtil.toObject(asd, FNJSQueryFundsRes.class);
        System.out.println(ToStringBuilder.reflectionToString(resx));

        System.out.println("=====================");
        FNJSQueryWareAllRes res = JSON.parseObject(asd, FNJSQueryWareAllRes.class);
        System.out.println(ToStringBuilder.reflectionToString(res));*/
    }
}