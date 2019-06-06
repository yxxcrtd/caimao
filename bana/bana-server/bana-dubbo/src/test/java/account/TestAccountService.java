package account;

import com.caimao.bana.common.api.utils.crypto.Base64Utils;
import com.caimao.bana.common.api.utils.crypto.RSAUtils;
import com.caimao.bana.server.utils.HttpClientUtils;
import com.caimao.bana.server.utils.yeepay.RSA;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 账户相关的测试用例
 * Created by WangXu on 2015/5/26.
 */
public class TestAccountService  {

//    @Autowired
//    private AccountServiceImpl accountService;
//    @Autowired
//    private AccountManager accountManager;

    private static final Logger logger = LoggerFactory.getLogger(TestAccountService.class);

//    @Autowired
//    private TpzSmsOutDao smsOutDao;

    @Test
    public void testHtml() throws Exception {
        String url = "http://www.epianhong.com/web/gagmi?dzpid=23";
        //String url = "http://121.40.125.254:18080/tradeweb/refreshHQ";
        String str = HttpClientUtils.getString(url);

        System.out.println(new String(str.getBytes("ISO-8859-1"), "UTF-8"));
    }

    private long numberFormat(String num){
        num = num.trim();
        if ("&mdash;".equals(num) || "-".equals(num)){
            return 0;
        }
        return (long)(Double.parseDouble(num)*100);
    }

    @Test
    public void TestQueryAccountJour() throws Exception {

//        Map<String, Object> keyMap = RSAUtils.genKeyPair();
//        String publicKey = RSAUtils.getPublicKey(keyMap);
//        String privateKey = RSAUtils.getPrivateKey(keyMap);


        System.err.println("公钥加密——私钥解密");
        String source = "阿萨德，。JLOASJf923r";
        System.out.println("\r加密前文字：\r\n" + source);
        String encodedData = RSAUtils.encryptByPublicKey(source);
        System.out.println("加密后文字：\r\n" + encodedData);
        encodedData = "0530a9ccb2465b4a2546fd536e61db9ac8fbbe3f62cf67001d6389e8afc04b1ef42f1a02242abaaa2a12c2860981801686a16a8f010f499288574401d13514c3265e185935f95e751193c415e6ed2d5d067fbc39d8318da5cdf3185f3d7d044e2894f9f1b5a1e68336f7f028237a23b717c6fb307a3cc5336c9c3a4457ba345e";
        //byte[] encodedDataBytes = Base64Utils.decode(encodedData);
        //encodedData = RSAUtils.bytes2Hex(encodedDataBytes);
        String decodedData = RSAUtils.decryptByPrivateKey(encodedData);
        System.out.println("解密后文字: \r\n" + decodedData);



//        String filePathName = "/www/o_a2wwzm76qHTj8L2id4NbtkHh-8.jpg";
//        InputStream inputStream = null;
//        String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=p0KWhY_TgFy0iE2bP6LO2O-hnpppsScjfmV2d3vMEqf3BS1VQ1-8VGmXYuBAQph63H09JBcykmFmO259vo8TcRVPEIfUJIQq7oDJfZVwx2MWEEgACABJJ&media_id=efZQ1MkDLLmB6uIBH_G0WuUBWHgma0DFluvgpzGxzI-KejJjoqf9nNLqcl2U2q15";
//        try {
//            URL urlGet = new URL(url);
//            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
//            http.setRequestMethod("GET"); // 必须是get方式请求
//            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            http.setDoOutput(true);
//            http.setDoInput(true);
//            http.connect();
//            // 获取文件转化为byte流
//            inputStream = http.getInputStream();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (inputStream == null) return;
//
//        byte[] data = new byte[1024];
//        int len = 0;
//        FileOutputStream fileOutputStream = null;
//        try {
//            fileOutputStream = new FileOutputStream(filePathName);
//            while ((len = inputStream.read(data)) != -1) {
//                fileOutputStream.write(data, 0, len);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (fileOutputStream != null) {
//                try {
//                    fileOutputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

//        String str = "、恒宝股份、大北农、罗普斯金";
//
//        str = str.substring(1, str.length());
//        System.out.println(str);

//        String str = "上市 公 司公 告 点评 买 入 维持 \n" +
//                "证券研究报告 化学合成多肽药物 \n" +
//                "翰宇药业（300199） \n" +
//                "2015 年 01 月 20 日 ";
//
//        Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
//        Matcher m = CRLF.matcher(str);
//        if (m.find()) {
//            str = m.replaceAll("<br>");
//        }
//
//        System.out.println(str);


//        Pattern pricePattern = Pattern.compile("^[0-9]+[.]?[0-9]{0,2}$");
//        Matcher priceMatcher = pricePattern.matcher("12331");
//
//        System.out.println(priceMatcher.matches());

//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        Date date = df.parse("2016-01-12 11:18:00");
//        Date nowDate = df.parse("2016-01-12 11:18:00");
//
//        Long diff = nowDate.getTime() - date.getTime();
//
//        System.out.println("Date : " + date.getTime());
//        System.out.println("nowDate : " + nowDate.getTime());
//        System.out.println("diff : " + diff);

        //String url = "http://www.localcaimao.com:8082/weixin/guji/analystIndex.html?code=031926fa7e530b29036e524b134d1cfl&state=";



//        String openPrice = "140.8";
//        String nowPrice = "130";
//
//        long CurrentGains = numberFormat(String.valueOf((Double.valueOf(nowPrice) - Double.valueOf(openPrice)) / Double.valueOf(openPrice) * 100));
//
//        System.out.println(CurrentGains);

//        Map<String, Object> resMap = new HashMap<>();
//        resMap.put("success", true);
//        resMap.put("msg", "/asdfasdfasdf");
//
//        System.out.println(com.alibaba.dubbo.common.json.JSON.json(resMap));

//        // 获取urls
//        String[] urls = "百度,http://www.baidu.com,网易,http://www.163.com".split(",");
//        List<Map<String, String>> tmpList = new ArrayList<>();
//        Map<String, String> tmpMap = new HashMap<>();
//        for (String u : urls) {
//            if (tmpMap.size() == 2) {
//                Map<String, String> newMap = new HashMap<>();
//                newMap.putAll(tmpMap);
//                tmpList.add(newMap);
//                tmpMap.clear();
//            }
//            if (tmpMap.size() == 0) {
//                tmpMap.put("name", u);
//            } else if (tmpMap.size() == 1) {
//                tmpMap.put("url", u);
//            }
//            System.out.println(tmpMap);
//        }
//        if (tmpMap.size() == 2) {
//            Map<String, String> newMap = new HashMap<>();
//            newMap.putAll(tmpMap);
//            tmpList.add(newMap);
//            tmpMap.clear();
//        }
//
//        System.out.println("List map: ");
//        for (Map<String, String> map : tmpList) {
//            System.out.println(map);
//        }


//        String str = String.format("%s产品，价格%.2f %%", "yin", new BigDecimal("21"));
//
//        System.out.println(str);

//        logger.info("info");
//        logger.warn("warn");
//        logger.error("error");
//
//        logger.info("test {}", 1);

//        List<Map<String, String>> list = new ArrayList<>();
//        Map<String, String> map = new HashMap<>();
//        map.put("condition", "1");
//        map.put("price", "123");
//        map.put("on", "1");
//        list.add(map);
//
//        Map<String, String> map2 = new HashMap<>();
//        map2.put("condition", "2");
//        map2.put("price", "321");
//        map2.put("on", "0");
//        list.add(map2);
//
//        String json = JSON.toJSONString(list);
//
//        System.out.println(json);

//        String str = "select orderr by  a desc and b desc";
//        Integer one = str.indexOf("desc");
//        Integer last = str.lastIndexOf("desc");


//        String url = "https://ybk.caimao.com/ybk/article/1.html";
//        String serverName = "ybk.caimao.com";
//        String reqPath = "favicon.ico";
//        String app = serverName.substring(0, serverName.indexOf("."));
//
//        List<String> domainList = new ArrayList<>();
//        domainList.add("ybk");
//        domainList.add("gjs");
//        domainList.add("api");
//
//        System.out.println("app : " + app);
//        System.out.println(domainList.contains(app));
//        System.out.println("2 index " + reqPath.indexOf("/", 2));
//        String reqApp = reqPath.substring(reqPath.indexOf("/") + 1, reqPath.indexOf("/", 2));
//        System.out.println("req app : " + reqApp);
//        System.out.println(app.equals(reqApp));

//        for (Integer i = 0; i <= 5; i++) {
//            new Thread(){
//                @Override
//                public void run() {
//                    System.out.println("终端打印的 ");
//                    logger.error("日志打印的");
//                }
//            }.start();
//        }
//
//        try {
//            Thread.sleep(3000);
//        } catch (Exception e) {}

//        String pushType = "1,2,1,1,2";
//        if (pushType != null) {
//            List<String> typeList = new ArrayList<>();
//            String[] pushStrArr = pushType.split(",");
//            for (String p : pushStrArr) {
//                typeList.add(p);
//            }
//            //req.setPushTypes(typeList);
//            System.out.println(ToStringBuilder.reflectionToString(typeList));
//        }


//        String avatarPic = "/upload/799867328790529.jpg";
//        if (!avatarPic.contains("upload")) {
//            avatarPic = "/avatars/" + avatarPic;
//        }
//
//        System.out.println(avatarPic);
//
//        Integer cnt = this.smsOutDao.getSmsCount("18612839215", "1", new Date(new Date().getTime() - (60 * 60 * 1000)));
//        System.out.println(cnt);

//        String req_url = "http://172.32.1.218:8097/api/gjs_trade/queryEntrust?token=7ojlr5rp6av0elfzyduw&exchange=NJS";
//        HttpClient client = new HttpClient();
//        // 设置代理服务器地址和端口
//
//        //client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
//        // 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
//        HttpMethod method = new GetMethod(req_url);
//        method.setRequestHeader("accept", "application/json");
//        //使用POST方法
//        //HttpMethod method = new PostMethod("http://java.sun.com");
//        client.executeMethod(method);
//
//        //打印服务器返回的状态
//        System.out.println(method.getStatusLine());
//        //打印返回的信息
//        System.out.println(method.getResponseBodyAsString());
//        //释放连接
//        method.releaseConnection();


//        StringBuffer buffer = new StringBuffer();
//        try {
//            URL url = new URL(req_url);
//            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
//
//            httpUrlConn.setDoOutput(false);
//            httpUrlConn.setDoInput(true);
//            httpUrlConn.setUseCaches(false);
//
//            httpUrlConn.setRequestMethod("GET");
//            httpUrlConn.connect();
//
//            // 将返回的输入流转换成字符串
//            InputStream inputStream = httpUrlConn.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//            String str = null;
//            while ((str = bufferedReader.readLine()) != null) {
//                buffer.append(str);
//            }
//            bufferedReader.close();
//            inputStreamReader.close();
//            // 释放资源
//            inputStream.close();
//            inputStream = null;
//            httpUrlConn.disconnect();
//        } catch (Exception e) {
//            System.out.println(e.getStackTrace());
//        }
//
//        System.out.println(buffer.toString());

//        String str = "/home/www/html/update/12/1442820308236.png";
//        System.out.println(str.substring(str.indexOf("update")));

//        // 将支持的银行分开
//        HashMap<String, String> supportBankMap = new HashMap<>();
//        String[] supportBankList = "001,002,003".split(",");
//        for (String s : supportBankList) {
//            System.out.println(s);
//            supportBankMap.put(s, s);
//        }


//        Long userId = 799869644046338L;
//        String bizType = "00";
//        String startDate = "2014-11-26 00:00:00";
//        String endDate = "2015-05-26 23:59:59";
//        int start = 5;
//        int limit = 15;
//
//        FAccountQueryAccountJourReq accountQueryAccountJourReq = new FAccountQueryAccountJourReq();
//        accountQueryAccountJourReq.setUserId(userId);
//        accountQueryAccountJourReq.setBizType(bizType);
//        accountQueryAccountJourReq.setStartDate(startDate);
//        accountQueryAccountJourReq.setEndDate(endDate);
//        accountQueryAccountJourReq.setStart(start);
//        accountQueryAccountJourReq.setLimit(limit);
//        accountQueryAccountJourReq = this.accountService.queryAccountJour(accountQueryAccountJourReq);
//        System.out.println("查询条数 ： " + accountQueryAccountJourReq.getItems().size());
//        for (int i = 0; i < accountQueryAccountJourReq.getItems().size(); i++) {
//            FAccountQueryAccountJourRes a = accountQueryAccountJourReq.getItems().get(i);
//            System.out.println(i + " " + ToStringBuilder.reflectionToString(a));
//        }
    }
//
//    @Test
//    public void TestQueryAccountFrozenJour() throws Exception {
//        Long userId = 799869644046338L;
//        String bizType = "00";
//        String startDate = "2014-11-26 00:00:00";
//        String endDate = "2015-05-26 23:59:59";
//        int start = 5;
//        int limit = 15;
//
//        FAccountQueryAccountFrozenJourReq req = new FAccountQueryAccountFrozenJourReq();
//        req.setUserId(userId);
//        req.setBizType(bizType);
//        req.setStartDate(startDate);
//        req.setEndDate(endDate);
//        req.setStart(start);
//        req.setLimit(limit);
//        req = this.accountService.queryAccountFrozenJour(req);
//        System.out.println("查询条数 ： " + req.getItems().size());
//        for (int i = 0; i < req.getItems().size(); i++) {
//            FAccountQueryAccountFrozenJourRes a = req.getItems().get(i);
//            System.out.println(i + " " + ToStringBuilder.reflectionToString(a));
//        }
//
//        System.out.println(EAccountBizType.findByCode("00").getValue());
//    }
//
//    @Test
//    public void TestQueryWithdrawOrder() throws Exception {
//        FAccountQueryWithdrawOrderReq accountQueryWithdrawOrderReq = new FAccountQueryWithdrawOrderReq();
//        accountQueryWithdrawOrderReq.setUserId(799869644046338L);
//        accountQueryWithdrawOrderReq.setOrderStatus("");
//        accountQueryWithdrawOrderReq.setStart(1);
//        accountQueryWithdrawOrderReq.setLimit(10);
//
//        accountQueryWithdrawOrderReq = this.accountService.queryWithdrawOrder(accountQueryWithdrawOrderReq);
//        System.out.println("查询条数 ： " + accountQueryWithdrawOrderReq.getItems().size());
//        for (int i = 0; i < accountQueryWithdrawOrderReq.getItems().size(); i++) {
//            FAccountQueryWithdrawOrderRes a = accountQueryWithdrawOrderReq.getItems().get(i);
//            System.out.println(i + " " + ToStringBuilder.reflectionToString(a));
//        }
//    }
//
//    @Test
//    public void TestQueryChargeOrder() throws Exception {
//        FAccountQueryChargeOrderReq accountQueryChargeOrderReq = new FAccountQueryChargeOrderReq();
//        accountQueryChargeOrderReq.setUserId(799869644046338L);
//        accountQueryChargeOrderReq.setStartDate("2015-06-05 00:00:00");
//        accountQueryChargeOrderReq.setEndDate("2015-06-05 23:59:59");
//        //accountQueryChargeOrderReq.setOrderStatus("02");
//        //accountQueryChargeOrderReq.setPages(1);
//        accountQueryChargeOrderReq.setStart(0);
//        accountQueryChargeOrderReq.setLimit(5);
//
//        accountQueryChargeOrderReq = this.accountService.queryChargeOrder(accountQueryChargeOrderReq);
//        System.out.println("查询条数 ： " + accountQueryChargeOrderReq.getItems().size());
//        for (int i = 0; i < accountQueryChargeOrderReq.getItems().size(); i++) {
//            FAccountQueryChargeOrderRes a = accountQueryChargeOrderReq.getItems().get(i);
//            System.out.println(i + " " + ToStringBuilder.reflectionToString(a));
//        }
//
//        System.out.println("总的信息：" + ToStringBuilder.reflectionToString(accountQueryChargeOrderReq));
//    }
//
//    @Test
//    public void TestDoApplyWithdraw() throws Exception {
//        Long userId = 802184463646721L;
//        String tradePwd = "wangxu123,";
//        Long orderAmoutn = 10000L;
//        String orderName = "测试用例提现";
//        String orderAbstract = "测试用例提现说明";
//
//        FAccountApplyWithdrawReq accountApplyWithdrawReq = new FAccountApplyWithdrawReq();
//        accountApplyWithdrawReq.setUserId(userId);
//        accountApplyWithdrawReq.setUserTradePwd(tradePwd);
//        accountApplyWithdrawReq.setOrderAmount(orderAmoutn);
//        accountApplyWithdrawReq.setOrderName(orderName);
//        accountApplyWithdrawReq.setOrderAbstract(orderAbstract);
//        Long res = this.accountService.doApplyWithdraw(accountApplyWithdrawReq);
//
//        System.out.println("提现返回的订单ID ; " + res);
//    }
//
//    @Test
//    //@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
//    public void testUpdateAccount() throws Exception {
//        this.accountManager.doUpdateAvaiAmount("123", 803991202037762L, 100L, EAccountBizType.CHARGE.getCode(), ESeqFlag.COME.getCode());
//    }
//
//    @Test
//    public void testDate(){
//        Calendar c1 = Calendar.getInstance();
//        System.out.println(c1.get(Calendar.DAY_OF_WEEK));
//    }
}
