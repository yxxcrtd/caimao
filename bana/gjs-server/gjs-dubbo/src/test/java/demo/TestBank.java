package demo;

import com.caimao.gjs.server.utils.sjs.BlockSocket;
import com.caimao.gjs.server.utils.sjs.DESedeUtil;
import com.caimao.gjs.server.utils.sjs.StringUtil;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TestBank {
    public static String SocketClient(String param) throws IOException {
        String recvMsg = null;
        BlockSocket bs = null;
        try {




            //String unicode = new String(param.getBytes(),"UTF-8");
            //param = new String(unicode.getBytes("GBK"));

            //开户：120.197.123.70:51008
            //String host_ip = "120.197.123.70"; //168.33.120.14：6087 112.95.144.26：48002
            //int port = 51008; //54077 17000，51077
            String sKey="ABCDEF0123456789abcdef11";

            String host_ip = "210.21.197.124";
            int port = 47007;


            bs = new BlockSocket();
            bs.connect(host_ip, port);
            param = StringUtil.AppendString(param); //追加并替换字符串

            System.out.println("开始打印request");
            System.out.println("请求报文：" + param);
            System.out.println("请求长度：" + param.length());


            byte[] encryptMsg = StringUtil.StringToBytes(param);
            //encryptMsg = DESedeUtil.encrypt(encryptMsg, sKey.getBytes());


            ByteBuffer byteBuffer = ByteBuffer.allocate(8 + encryptMsg.length);
            byte[] headLen = StringUtil.StringToBytes(StringUtil.FILL(Integer.toString(encryptMsg.length), '0', 8, 'L'));
            System.out.println("最终发送" + new String(encryptMsg));
            System.out.println("处理开始============="+encryptMsg.length);
            System.out.println(new String(headLen));
            byteBuffer.put(headLen);
            byteBuffer.put(encryptMsg);
            bs.directWriteMsg(byteBuffer.array());
            String sRecvHead = "";
            byte[] recvBuffHead = bs.recvMsgByLen(8, 30);
            sRecvHead = StringUtil.ByteToString(recvBuffHead);
            System.out.println("head = " + sRecvHead);
            int bodyLength = Integer.parseInt(sRecvHead);
            byte[] recvBuffBody = bs.recvMsgByLen(bodyLength, 80);
            recvMsg = StringUtil.ByteToString(recvBuffBody);
            System.out.println("body = " + recvMsg);
            return recvMsg;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bs != null) {
                bs.closeSocket();
            }
        }
        return recvMsg;
    }

    public static void main(String[] args) throws IOException {

        /**用51077
         * 查询代理机构相关
         * 代理机构信息查询 880110/客户级别信息查询880111/客户经理信息查询880112/客户分组信息查询880113
         */
        //String param="<request><head><h_exch_code>880110</h_exch_code><h_bank_no>1111</h_bank_no><h_term_type>02</h_term_type><h_branch_id>BJG082</h_branch_id><h_teller_id>P00001</h_teller_id></head><body><record><is_contain_self>1</is_contain_self></record></body></request>";
        /**
         * 查询开户信息相关880120
         */
        //String param = "<request><head><h_exch_code>880120</h_exch_code><h_bank_no>1111</h_bank_no><h_term_type>02</h_term_type><h_branch_id>B0077001</h_branch_id><h_teller_id>C09100</h_teller_id><h_teller_id_1></h_teller_id_1><h_teller_id_2></h_teller_id_2><h_bk_seq_no></h_bk_seq_no><h_work_date></h_work_date><h_exch_date></h_exch_date></head><body><record><area_code>110000</area_code><zipcode>100001</zipcode><tel>15652826296</tel><addr>深圳前海</addr><cert_type_id>s</cert_type_id><cust_name>刘琦</cust_name><mobile_phone>13500000000</mobile_phone><cert_num>522635197809122343</cert_num><email></email><exch_pwd>6ed4f54d2411e91b4bccd944fe96e598</exch_pwd><bk_acct_no>005:6217007********0011</bk_acct_no>" + "<broker_list>00771</broker_list><fund_pwd>73f56cf33a90698e0f9a397a5e4cbc7c</fund_pwd><grade_id>007701</grade_id><branch_id>B0077001</branch_id><cust_type_id>C90001</cust_type_id><sms_validatecode>737759</sms_validatecode></record></body></request>";
        /**
         * 开户=========================
         */
        //String param = "<request><head><h_bank_no>1111</h_bank_no><h_term_type>02</h_term_type><h_teller_id>C09100</h_teller_id><h_teller_id_1></h_teller_id_1><h_teller_id_2></h_teller_id_2><h_branch_id>B0077001</h_branch_id><h_work_date></h_work_date><h_exch_date></h_exch_date><h_exch_code>880120</h_exch_code><h_bk_seq_no>000001</h_bk_seq_no></head><body><record><acct_no></acct_no><bk_acct_no>6227000015910152025</bk_acct_no><cust_name>宁豪</cust_name><cert_type_id>S</cert_type_id><cert_num>142322198906193018</cert_num><branch_id>B0077001</branch_id><grade_id>007701</grade_id><exch_pwd>1c7688c393195bcb39e91cbd6fac2e12</exch_pwd><fund_pwd>e10adc3949ba59abbe56e057f20f883e</fund_pwd><area_code>110000</area_code><mobile_phone>18210046820</mobile_phone><tel>18210046820</tel><addr>北京市海淀区上地三街金隅嘉华大厦</addr><zipcode>100000</zipcode><email></email><cust_type_id></cust_type_id><broker_list>00771</broker_list><sms_validatecode>722529</sms_validatecode><score_riskevaluation>90</score_riskevaluation></record></body></request>";
        //String param = "<request><head><h_bank_no>1111</h_bank_no><h_term_type>02</h_term_type><h_teller_id>C09100</h_teller_id><h_teller_id_1></h_teller_id_1><h_teller_id_2></h_teller_id_2><h_branch_id>B0077001</h_branch_id><h_work_date></h_work_date><h_exch_date></h_exch_date><h_exch_code>880120</h_exch_code><h_bk_seq_no>000001</h_bk_seq_no></head><body><record><acct_no></acct_no><bk_acct_no>6227000015910152025</bk_acct_no><cust_name>ninghao</cust_name><cert_type_id>S</cert_type_id><cert_num>142322198906193018</cert_num><branch_id>B0077001</branch_id><grade_id>007701</grade_id><exch_pwd>1c7688c393195bcb39e91cbd6fac2e12</exch_pwd><fund_pwd>e10adc3949ba59abbe56e057f20f883e</fund_pwd><area_code>110000</area_code><mobile_phone>18210046820</mobile_phone><tel>18210046820</tel><addr>beijingshishangdijiahuadasha</addr><zipcode>100000</zipcode><email></email><cust_type_id></cust_type_id><broker_list>00771</broker_list><sms_validatecode>######</sms_validatecode><score_riskevaluation>90</score_riskevaluation></record></body></request>";
        /**
         *代理个人账户信息查询880140
         */
        //String param="<request><head><h_exch_code>880140</h_exch_code><h_bank_no>0077</h_bank_no><h_term_type>02</h_term_type><h_branch_id>B0077001</h_branch_id><h_teller_id>C09100</h_teller_id><h_teller_id_1></h_teller_id_1><h_teller_id_2></h_teller_id_2><h_bk_seq_no>bk14418904998788997113</h_bk_seq_no><h_work_date></h_work_date><h_exch_date></h_exch_date></head><body><record><acct_no>1080017715</acct_no><cert_type_id></cert_type_id><cert_num></cert_num></record></body></request>";

        /**
         *代理个人账户基本信息修改880141
         */
        //String param="<request><head><h_exch_code>880141</h_exch_code><h_bank_no>0077</h_bank_no><h_term_type>02</h_term_type><h_branch_id>B0077001</h_branch_id><h_teller_id>C09100</h_teller_id><h_teller_id_1></h_teller_id_1><h_teller_id_2></h_teller_id_2><h_bk_seq_no>bk14418904998788997113</h_bk_seq_no><h_work_date></h_work_date><h_exch_date></h_exch_date></head><body><record><acct_no>1080017715</acct_no><mobile_phone>13106930273</mobile_phone><tel>13106930273</tel><addr>深圳</addr><zipcode>518054</zipcode></record></body></request>";

        /**
         *代理个人账户状态变更880142
         */
        //String param="<request><head><h_exch_code>880141</h_exch_code><h_bank_no>0077</h_bank_no><h_term_type>02</h_term_type><h_branch_id>B0077001</h_branch_id><h_teller_id>C09100</h_teller_id><h_teller_id_1></h_teller_id_1><h_teller_id_2></h_teller_id_2><h_bk_seq_no>bk14418904998788997113</h_bk_seq_no><h_work_date></h_work_date><h_exch_date></h_exch_date></head><body><record><acct_no>1080017715</acct_no><old_acct_stat>1</old_acct_stat><new_acct_stat>2</new_acct_stat></record></body></request>";


        /**
         *代理个人账户预销户880150/代理个人账户销户880151
         */
        //String param="<request><head><h_exch_code>880150</h_exch_code><h_bank_no>0077</h_bank_no><h_term_type>02</h_term_type><h_branch_id>B0077001</h_branch_id><h_teller_id>C09100</h_teller_id><h_teller_id_1></h_teller_id_1><h_teller_id_2></h_teller_id_2><h_bk_seq_no>bk14418904998788997113</h_bk_seq_no><h_work_date></h_work_date><h_exch_date></h_exch_date></head><body><record><acct_no>1080017715</acct_no></record></body></request>";

        /**
         *客户交易密码重置880160
         */
        //String param="<request><head><h_exch_code>880160</h_exch_code><h_bank_no>0077</h_bank_no><h_term_type>02</h_term_type><h_branch_id>B0077001</h_branch_id><h_teller_id>C09100</h_teller_id><h_teller_id_1></h_teller_id_1><h_teller_id_2></h_teller_id_2><h_bk_seq_no>bk14418904998788997113</h_bk_seq_no><h_work_date></h_work_date><h_exch_date></h_exch_date></head><body><record><acct_no>1080017715</acct_no><new_exch_pwd>21218cca77804d2ba1922c33e0151105</new_exch_pwd></record></body></request>";

        /**
         *客户资金密码重置880161
         */
        //String param="<request><head><h_exch_code>880161</h_exch_code><h_bank_no>0077</h_bank_no><h_term_type>02</h_term_type><h_branch_id>B0077001</h_branch_id><h_teller_id>C09100</h_teller_id><h_teller_id_1></h_teller_id_1><h_teller_id_2></h_teller_id_2><h_bk_seq_no>bk14418904998788997113</h_bk_seq_no><h_work_date></h_work_date><h_exch_date></h_exch_date></head><body><record><acct_no>1080017715</acct_no><new_fund_pwd>21218cca77804d2ba1922c33e0151105</new_fund_pwd></record></body></request>";

        /**
         * 开通出入金880170
         */
        //String param="<request><head><h_exch_code>880170</h_exch_code><h_bank_no>1111</h_bank_no><h_term_type>02</h_term_type><h_branch_id>BJG004</h_branch_id><h_teller_id>C00004</h_teller_id></head><body><record><bk_cert_no>340223198101089041</bk_cert_no><acct_no>1080061534</acct_no><bank_no>9997</bank_no><bk_mer_code>123456789</bk_mer_code><bk_cert_type>A</bk_cert_type><bk_acct_no>002:6226096551483301</bk_acct_no></record></body></request>";


        /**用46001
         * 客户登录800101
         * 明文密码：119322
         */
        //String param="<request><head><h_exch_code>800101</h_exch_code><h_bank_no>0077</h_bank_no><h_user_id>1080017715</h_user_id><h_branch_id>B0077001</h_branch_id><h_fact_date>20150911</h_fact_date><h_fact_time></h_fact_time><h_exch_date>20150911</h_exch_date><h_serial_no>123456</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><user_pwd>f3cad05b052003e572e930a38db9458c</user_pwd><login_ip>168.33.114.248</login_ip><login_server_code>M201</login_server_code></record></body></request>";

        /**客户交易密码/资金密码修改202201
         * 客户交易/资金密码888888
         * 原交易密码119322
         */
        //String param="<request><head><h_exch_code>202201</h_exch_code><h_bank_no>0077</h_bank_no><h_user_id>1080017715</h_user_id><h_branch_id>B0077001</h_branch_id><h_fact_date>20150911</h_fact_date><h_fact_time></h_fact_time><h_exch_date>20150911</h_exch_date><h_serial_no>123456</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><exch_pwd></exch_pwd><old_exch_pwd></old_exch_pwd><fund_pwd>21218cca77804d2ba1922c33e0151105</fund_pwd><old_fund_pwd>f3cad05b052003e572e930a38db9458c</old_fund_pwd></record></body></request>";
        /**
         * 客户所属机构的机构代码查询208801/客户签约银行代码查询302401
         */
        //String param="<request><head><h_exch_code>302401</h_exch_code><h_bank_no>0077</h_bank_no><h_user_id>1080017715</h_user_id><h_branch_id>B0077001</h_branch_id><h_fact_date>20150911</h_fact_date><h_fact_time></h_fact_time><h_exch_date>20150911</h_exch_date><h_serial_no>123456</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><bank_no></bank_no></record></body></request>";
        /**
         * 银行简称查询302501
         */
        //String param="<request><head><h_exch_code>302501</h_exch_code><h_bank_no>0077</h_bank_no><h_user_id>1080017715</h_user_id><h_branch_id>B0077001</h_branch_id><h_fact_date>20150911</h_fact_date><h_fact_time></h_fact_time><h_exch_date>20150911</h_exch_date><h_serial_no>123456</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><bank_no>1111</bank_no></record></body></request>";

        /**
         * 系统参数查询903001
         */
        //String param="<request><head><h_exch_code>903001</h_exch_code><h_bank_no>0077</h_bank_no><h_user_id>1080017715</h_user_id><h_branch_id>B0077001</h_branch_id><h_fact_date>20150911</h_fact_date><h_fact_time></h_fact_time><h_exch_date>20150911</h_exch_date><h_serial_no>123456</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><para_id>VersionClientNew0043</para_id></record></body></request>";

        /**
         * 当日出入金流水查询600201
         */
        //String param="<request><head><h_exch_code>600201</h_exch_code><h_user_id>1080043433</h_user_id><h_bank_no>9997</h_bank_no><h_branch_id>B0077001</h_branch_id><h_fact_date>20150915</h_fact_date><h_fact_time>07:41:55</h_fact_time><h_exch_date>20150915</h_exch_date><h_serial_no>10015022</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><access_way>1</access_way><in_account>1</in_account></record></body></request>";

        /**
         * 历史出入金流水查询600216
         */
        //String param="<request><head><h_exch_code>600216</h_exch_code><h_user_id>1080012271</h_user_id><h_bank_no>0077</h_bank_no><h_branch_id>B00000</h_branch_id><h_fact_date>20150918</h_fact_date><h_fact_time>13:44:02</h_fact_time><h_exch_date>20150918</h_exch_date><h_serial_no>10015268</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><start_date>20150918</start_date><end_date>20150918</end_date><paginal_num>1</paginal_num><curr_page>1</curr_page></record></body></request>";

        /**
         * 当日委托单查询600203/当日成交单查询600205
         */
        //String param="<request><head><h_exch_code>600210</h_exch_code><h_user_id>1080012271</h_user_id><h_bank_no>0077</h_bank_no><h_branch_id>B00000</h_branch_id><h_fact_date>20150922</h_fact_date><h_fact_time>13:44:02</h_fact_time><h_exch_date>20150922</h_exch_date><h_serial_no>10015268</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><paginal_num>1</paginal_num><curr_page>1</curr_page></record></body></request>";

        /**
         * 当日延期持仓查询600210
         */
        //String param="<request><head><h_exch_code>600210</h_exch_code><h_user_id>1080012271</h_user_id><h_bank_no>0077</h_bank_no><h_branch_id>B00000</h_branch_id><h_fact_date>20150922</h_fact_date><h_fact_time>13:44:02</h_fact_time><h_exch_date>20150922</h_exch_date><h_serial_no>10015268</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><prod_code>Au(T+N1)</prod_code><paginal_num>1</paginal_num><curr_page>1</curr_page></record></body></request>";
        /**
         * 历史委托单查询600204/历史成交单查询600206
         */
        // String param="<request><head><h_exch_code>600207</h_exch_code><h_user_id>1006000265</h_user_id><h_bank_no>0077</h_bank_no><h_branch_id>B00000</h_branch_id><h_fact_date>20150917</h_fact_date><h_fact_time>09:28:37</h_fact_time><h_exch_date>20150917</h_exch_date><h_serial_no>10015871</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><start_date>20150917</start_date><end_date>20150917</end_date><paginal_num>1</paginal_num><curr_page>1</curr_page></record></body></request>";
        /**
         * 历史延期持仓查询600207
         */
         //String param="<request><head><h_exch_code>600207</h_exch_code><h_user_id>1080012271</h_user_id><h_bank_no>0077</h_bank_no><h_branch_id>B00000</h_branch_id><h_fact_date>20150918</h_fact_date><h_fact_time>13:44:02</h_fact_time><h_exch_date>20150918</h_exch_date><h_serial_no>10015268</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><prod_code>mAu(T+D)</prod_code><start_date>20150918</start_date><end_date>20150918</end_date><paginal_num>1</paginal_num><curr_page>1</curr_page></record></body></request>";
        /**
         * 当日延期持仓查询102003
         */
        //String param="<request><head><h_exch_code>102003</h_exch_code><h_user_id>1080012271</h_user_id><h_bank_no>0077</h_bank_no><h_branch_id>B00000</h_branch_id><h_fact_date>20150918</h_fact_date><h_fact_time>13:44:02</h_fact_time><h_exch_date>20150918</h_exch_date><h_serial_no>10015268</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record></record></body></request>";

        /**
         * 会员公告查询803101
         */
        // String param="<request><head><h_exch_code>803101</h_exch_code><h_user_id>1080012271</h_user_id><h_bank_no>0077</h_bank_no><h_branch_id>B00000</h_branch_id><h_fact_date>20150918</h_fact_date><h_fact_time>13:44:02</h_fact_time><h_exch_date>20150918</h_exch_date><h_serial_no>10015268</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><start_date>20150922</start_date><end_date>20150922</end_date></record></body></request>";

        /**
         * 委托报单400101
         */
        //String param = "<request><head><h_exch_code>400101</h_exch_code><h_bank_no>1111</h_bank_no><h_branch_id>BJG040</h_branch_id><h_user_id>1080011089</h_user_id><h_serial_no>56465456452</h_serial_no></head><body><record><prod_code>Au(T+D)</prod_code><exch_type>4041</exch_type><entr_price>250</entr_price><client_serial_no>5646549483</client_serial_no><entr_amount>1</entr_amount></record></body></request>";
        /**
         * 报单撤销406101
         */
        // String param = "<request><head><h_exch_code>406101</h_exch_code><h_bank_no>1111</h_bank_no><h_branch_id>BJG040</h_branch_id><h_user_id>1080011089</h_user_id><h_serial_no>56465456452</h_serial_no></head><body><record><order_no>02000094</order_no></record></body></request>";
        /**
         * 延期平仓试算407101
         */
        //String param="<request><head><h_exch_code>407101</h_exch_code><h_bank_no>0077</h_bank_no><h_user_id>1080012271</h_user_id><h_branch_id>B00000</h_branch_id><h_fact_date>20150918</h_fact_date><h_fact_time></h_fact_time><h_exch_date>20150918</h_exch_date><h_serial_no>10015268</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><prod_code>mAu(T+D)</prod_code><cov_bs>b</cov_bs><cov_price>1</cov_price><cov_amount>1</cov_amount></record></body></request>";
        /**
         * 出入金转账相关302101
         */
        //String param="<request><head><h_exch_code>302101</h_exch_code><h_user_id>1080062467</h_user_id><h_bank_no>9997</h_bank_no><h_branch_id>BJG004</h_branch_id><h_fact_date>20150916</h_fact_date><h_fact_time>11:02:03</h_fact_time><h_exch_date></h_exch_date><h_serial_no>14406445230918663207</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><fund_pwd>cff5268fdea23436804aa896597355a4</fund_pwd><access_way>1</access_way><exch_bal>5000</exch_bal></record></body></request>";


        /**
         * 用55077
         * 当日资金查询102001
         */
        //String param="<request><head><h_exch_code>102001</h_exch_code><h_bank_no>0077</h_bank_no><h_user_id>1080017715</h_user_id><h_branch_id>B0077001</h_branch_id><h_fact_date>20150911</h_fact_date><h_fact_time></h_fact_time><h_exch_date>20150911</h_exch_date><h_serial_no>123456</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record></record></body></request>";

        /**
         * 历史资金查询600208
         */
        //String param="<request><head><h_exch_code>600208</h_exch_code><h_user_id>1080017715</h_user_id><h_bank_no>0077</h_bank_no><h_branch_id>B0077001</h_branch_id><h_fact_date>20150916</h_fact_date><h_fact_time>07:41:55</h_fact_time><h_exch_date>20150916</h_exch_date><h_serial_no>123456</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><start_date>20150916</start_date><end_date>20150916</end_date><paginal_num>1</paginal_num><curr_page>1</curr_page></record></body></request>";

        /**
         * 用56077
         * 强平单查询306901(待测试)
         */
        //String param="<request><head><h_exch_code>306901</h_exch_code><h_user_id>1080017715</h_user_id><h_bank_no>0077</h_bank_no><h_branch_id>B0077001</h_branch_id><h_fact_date>20150916</h_fact_date><h_fact_time>07:41:55</h_fact_time><h_exch_date>20150916</h_exch_date><h_serial_no>123456</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record><start_date>20150916</start_date><end_date>20150916</end_date></record></body></request>";

        /**
         * 风险度查询309901(待测试)
         */
        String param="<request><head><h_exch_code>309901</h_exch_code><h_user_id>1002820755</h_user_id><h_bank_no>0077</h_bank_no><h_branch_id>BBK077</h_branch_id><h_fact_date>20150917</h_fact_date><h_fact_time>07:41:55</h_fact_time><h_exch_date>20150916</h_exch_date><h_serial_no>123456</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record></record></body></request>";

        /**
         * 报单回报行情
         */
        //String param="00000105term_type=05#user_key=97641239412631#user_type=3#user_id=1020782897#branch_id=BJG001#lan_ip=10.14.14.133#";


        TestBank.SocketClient(param);
    }
}
