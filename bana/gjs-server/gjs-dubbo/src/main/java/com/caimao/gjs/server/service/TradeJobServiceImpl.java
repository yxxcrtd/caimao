package com.caimao.gjs.server.service;

import com.caimao.bana.common.api.enums.gjs.EGJSExchange;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.gjs.api.entity.GJSAccountEntity;
import com.caimao.gjs.api.entity.GJSProductEntity;
import com.caimao.gjs.api.entity.history.*;
import com.caimao.gjs.api.service.ITradeJobService;
import com.caimao.gjs.api.utils.DateUtil;
import com.caimao.gjs.server.dao.*;
import com.caimao.gjs.server.trade.protocol.sjs.entity.SJSQueryEntrustHistoryEntity;
import com.caimao.gjs.server.trade.protocol.sjs.entity.SJSQueryMatchHistoryEntity;
import com.caimao.gjs.server.trade.protocol.sjs.entity.SJSQueryTransferHistoryEntity;
import com.caimao.gjs.server.trade.protocol.sjs.entity.res.FSJSQueryEntrustHistoryRes;
import com.caimao.gjs.server.trade.protocol.sjs.entity.res.FSJSQueryMatchHistoryRes;
import com.caimao.gjs.server.trade.protocol.sjs.entity.res.FSJSQueryTransferHistoryRes;
import com.caimao.gjs.server.utils.FTP4jUtils;
import com.caimao.gjs.server.utils.FTPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 交易服务
 */
@Service("tradeJobService")
public class TradeJobServiceImpl implements ITradeJobService {
    private static final Logger logger = LoggerFactory.getLogger(TradeJobServiceImpl.class);

    @Resource
    private TradeServiceCCHelper tradeServiceCCHelper;
    @Resource
    private TradeServiceHelper tradeServiceHelper;

    @Resource
    private GJSProductDao gjsProductDao;
    @Resource
    private GJSAccountDao gjsAccountDao;
    @Resource
    private GJSNJSHistoryEntrustDao gjsnjsHistoryEntrustDao;
    @Resource
    private GJSSJSHistoryEntrustDao gjssjsHistoryEntrustDao;
    @Resource
    private GJSNJSHistoryTradeDao gjsnjsHistoryTradeDao;
    @Resource
    private GJSSJSHistoryTradeDao gjssjsHistoryTradeDao;
    @Resource
    private GJSNJSHistoryTransferDao gjsnjsHistoryTransferDao;
    @Resource
    private GJSSJSHistoryTransferDao gjssjsHistoryTransferDao;

    /** FTP的IP地址配置 */
    @Value("${njs_ftp_ip}")
    protected String NJS_FTP_IP;

    /** FTP的账户配置 */
    @Value("${njs_ftp_username}")
    protected String NJS_FTP_USERNAME;

    /** FTP的密码配置 */
    @Value("${njs_ftp_password}")
    protected String NJS_FTP_PASSWORD;

    /** 下载路径配置 */
    @Value("${njs_download_path}")
    protected String NJS_DOWNLOAD_PATH;


    /**
     * 更新商品列表
     * @throws Exception
     */
    @Override
    public void updateGoods() throws Exception {
        try{
            //更新南交所商品
            List<GJSProductEntity> NJSTradeProductList = gjsProductDao.queryExchangeGoods(EGJSExchange.NJS.getCode());
            tradeServiceHelper.setGoodsCache("njs_goods", NJSTradeProductList);

            //更新上金所商品
            List<GJSProductEntity> SJSTradeProductList = gjsProductDao.queryExchangeGoods(EGJSExchange.SJS.getCode());
            tradeServiceHelper.setGoodsCache("sjs_goods", SJSTradeProductList);

            //更新伦敦交易所商品
            List<GJSProductEntity> LIFEETradeProductList = gjsProductDao.queryExchangeGoods(EGJSExchange.LIFFE.getCode());
            tradeServiceHelper.setGoodsCache("liffe_goods", LIFEETradeProductList);

            logger.info("更新商品列表完成！");
        }catch(Exception e){
            logger.error("商品列表更新失败！", e);
            throw new Exception("商品列表更新失败");
        }
    }

    /**
     * 更新用户交易员编号关系
     * @throws Exception
     */
    @Override
    public void updateTraderId() throws Exception {
        List<GJSAccountEntity> accountList = gjsAccountDao.queryAccountAllList();
        if(accountList != null && accountList.size() > 0){
            for (GJSAccountEntity gjsAccountEntity:accountList){
                tradeServiceHelper.setTraderId(gjsAccountEntity.getExchange(), gjsAccountEntity.getUserId(), gjsAccountEntity.getTraderId());
                tradeServiceHelper.setUserId(gjsAccountEntity.getExchange(), gjsAccountEntity.getTraderId(), gjsAccountEntity.getUserId());
            }
        }
    }

    /**
     * 检测用户风险率并发送
     * @throws Exception
     */
    @Override
    public void sendRiskUser(Integer tplNo) throws Exception {
        tradeServiceHelper.queryNJSRisk(tplNo);
    }

    /**
     * 解析南交所历史数据
     * @param dataType 数据类型
     * @throws Exception
     */
    public void parseNJSHistory(String dataType, String date) throws Exception{
        List<String> dataTypeList = new ArrayList<>();
        dataTypeList.add("wt");
        dataTypeList.add("cj");
        dataTypeList.add("cr");
        if(!dataTypeList.contains(dataType)) throw new CustomerException("需要解析的类型不正确", 999999);
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT_STRING);
        //获取当前日期的前一天
        String d = DateUtil.addDays(df.format(new Date()), DateUtil.DATE_FORMAT_STRING, -1);
        if(!"".equals(date)){
            d = date;
        }
        String fileName = dataType + d + ".DEL";

        try{
            //连接ftp并下载文件
            //FTPUtils.connect(NJS_FTP_IP, NJS_FTP_USERNAME, NJS_FTP_PASSWORD, NJS_DOWNLOAD_PATH, fileName);
            FTP4jUtils.download(NJS_FTP_IP, NJS_FTP_USERNAME, NJS_FTP_PASSWORD, NJS_DOWNLOAD_PATH, fileName);
            //解析文件
            File file = new File(NJS_DOWNLOAD_PATH + File.separator + fileName);
            if (file.exists() && file.isFile()) {
                logger.info("开始解析" + fileName + "文件");
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "GBK");
                BufferedReader br = new BufferedReader(reader);
                String txt, s;
                while (null != (txt = br.readLine())) {
                    s = txt.replaceAll("\"", "").trim();
                    try{
                        switch (dataType) {
                            case "wt":
                                this.insertNJSEntrustHistory(s);
                                break;
                            case "cj":
                                this.insertNJSMatchHistory(s);
                                break;
                            case "cr":
                                this.insertNJSTransferHistory(s);
                                break;
                        }
                    }catch(Exception e){
                        logger.error("解析南交所历史数据出错," + s);
                    }
                }
                br.close();
                reader.close();
                logger.info("结束解析" + fileName + "文件");
            }else{
                logger.error(fileName + "文件不存在");
            }
        }catch(Exception e){
            logger.error("解析" + fileName + "文件错误");
        }
    }


    /**
     * 插入NJS历史委托
     * @param s 数据
     * @throws Exception
     */
    private void insertNJSEntrustHistory(String s) throws Exception {
        try{
            String[] array = s.split(",");
            GjsNJSHistoryEntrustEntity entity = new GjsNJSHistoryEntrustEntity();
            entity.setDate(array[1]); // 委托日期
            entity.setSerialNo(array[2]); // 委托单号
            entity.setTraderId(array[0]); // 交易员编号
            entity.setTime(array[3]); // 时间
            entity.setBuyOrSal(array[4]); // 买卖方向
            entity.setPrice(formatMoney(array[5])); // 委托价格
            entity.setNum(array[6]); // 委托数量
            entity.setContNum(array[7]); // 成交数量
            entity.setcStatus(array[8].equals("A")?"D":array[8]); // 状态
            entity.setSoIpAddress(""); // 给的数据没有
            entity.setsCancleTime(array[10]); // 撤单时间
            entity.setWareId(array[11]); // 委托品种
            entity.setcGenerateFlag(array[12]); // 下单类型（B止损止盈   A正常委托  C条件单
            entity.setOrderSty(array[9]);
            this.gjsnjsHistoryEntrustDao.insertNJSHistoryEntrust(entity);
            logger.info("成功！ NJS历史委托：" + s);
        }catch(Exception e){
            logger.info("失败！ NJS历史委托：" + s);
        }
    }

    /**
     * 插入NJS历史成交
     * @param s 数据
     * @throws Exception
     */
    private void insertNJSMatchHistory(String s) throws Exception {
        try{
            String[] array = s.split(",");
            GjsNJSHistoryTradeEntity entity = new GjsNJSHistoryTradeEntity();
            entity.setFirmId(array[0]); // 交易商编号
            entity.setDate(array[1]); // 成交日期
            entity.setContNo(array[2]); // 成交编号
            entity.setSerialNo(array[3]); // 委托编号
            entity.setWareId(array[4]); // 商品代码
            entity.setfTime(array[5]); // 成交时间
            entity.setBuyOrSal(array[6]); // 买卖标记，B买入;S卖出
            entity.setConPrice(formatMoney(array[7])); // 成交价格，货币型（12,2）
            entity.setContNum(array[8]); // 成交数量
            entity.setTmpMoney(formatMoney(array[9])); // 手续费
            entity.setContQty(new BigDecimal(formatMoney(array[7])).multiply(new BigDecimal(array[8])).setScale(2, BigDecimal.ROUND_DOWN).toString()); // 成交额
            entity.setOrderSty(array[11]); // 委托标志（101市价单；111正常单；151强制单
            entity.setcGenerateFlag(""); // 下单类型，A，正常的委托单；B，止盈止损单触发的委托单；C，条件单触发的委托单。
            this.gjsnjsHistoryTradeDao.insertNJSHistoryTrade(entity);
            logger.info("成功！ NJS历史成交：" + s);
        }catch(Exception e){
            logger.info("失败！ NJS历史成交：" + s);
        }
    }

    /**
     * 插入NJS历史出入金
     * @param s 数据
     * @throws Exception
     */
    private void insertNJSTransferHistory(String s) throws Exception {
        try{
            String[] array = s.split(",");
            if(!array[6].equals("1102")) return;
            GjsNJSHistoryTransferEntity entity = new GjsNJSHistoryTransferEntity();
            entity.setFirm_id(array[0]); // 交易商号
            entity.setDeal_date(array[1]); // 日期
            entity.setDeal_time(array[2]); // 时间
            entity.setBank_water_id(array[4]); // 银行流水
            entity.setChange_type(array[7]); // 出入金类型
            entity.setChange_money(formatMoney(array[8])); // 出入金金额

            entity.setMoney_type("");
            entity.setFlag("");
            entity.setStyle("");
            entity.setUser_id("");
            this.gjsnjsHistoryTransferDao.insertNJSHistoryTransfer(entity);
            logger.info("成功！ NJS历史出入金：" + s);
        }catch(Exception e){
            logger.info("成功！ NJS历史出入金：" + s);
        }
    }


    /**
     * 解析上金所历史数据
     * @param dataType 数据类型
     * @throws Exception
     */
    public void parseSJSHistory(String dataType) throws Exception{
        List<String> dataTypeList = new ArrayList<>();
        dataTypeList.add("wt");
        dataTypeList.add("cj");
        dataTypeList.add("cr");
        if(!dataTypeList.contains(dataType)) throw new CustomerException("需要解析的类型不正确", 999999);
        // 获取所有用户
        List<GJSAccountEntity> userList = gjsAccountDao.queryAccountListByExchange(EGJSExchange.SJS.getCode());
        logger.info("用户总数：" + userList.size());
        if(userList.size() > 0){
            for (GJSAccountEntity gjsAccountEntity:userList){
                try{
                    switch (dataType) {
                        case "wt":
                            this.insertSJSEntrustHistory(gjsAccountEntity.getTraderId());
                            break;
                        case "cj":
                            this.insertSJSMatchHistory(gjsAccountEntity.getTraderId());
                            break;
                        case "cr":
                            this.insertSJSTransferHistory(gjsAccountEntity.getTraderId());
                            break;
                    }
                }catch(Exception e){
                    logger.error("解析上金所历史数据出错");
                }
            }
        }
    }

    /**
     * 插入上金所历史委托数据
     * @param traderId 交易员编号
     * @throws Exception
     */
    private void insertSJSEntrustHistory(String traderId) throws Exception{
        try{
            FSJSQueryEntrustHistoryRes res = tradeServiceCCHelper.querySJSEntrustHistoryCC(traderId);
            if (res.getRecord() != null && res.getRecord().size() > 0) {
                for (SJSQueryEntrustHistoryEntity entity : res.getRecord()) {
                    try{
                        GjsSJSHistoryEntrustEntity e = new GjsSJSHistoryEntrustEntity();
                        e.setTrader_id(traderId);
                        e.setEntr_date(entity.getEntr_date());
                        e.setOrder_no(entity.getOrder_no());
                        e.setMarket_id(entity.getMarket_id());
                        e.setExch_type(entity.getExch_type());
                        e.setProd_code(entity.getProd_code());
                        e.setEntr_price(Float.parseFloat(entity.getEntr_price()));
                        e.setEntr_amount(Integer.parseInt(entity.getEntr_amount()));
                        e.setRemain_amount(Integer.parseInt(entity.getRemain_amount()));
                        e.setOffset_flag(entity.getOffset_flag());
                        e.setDeli_flag(entity.getDeli_flag());
                        e.setBs(entity.getBs());
                        e.setEntr_stat(entity.getEntr_stat());
                        e.setCancel_flag(entity.getCancel_flag());
                        e.setAccept_time(entity.getAccept_time());
                        e.setE_term_type(entity.getE_term_type());
                        e.setE_exch_time(entity.getE_exch_time());
                        e.setC_term_type(entity.getC_term_type());
                        e.setC_exch_time(entity.getC_exch_time());
                        e.setLocal_order_no(entity.getLocal_order_no());
                        this.gjssjsHistoryEntrustDao.insertSJSHistoryEntrust(e);
                    }catch(Exception e){
                        logger.error("插入上金所历史委托数据失败, ", e);
                    }
                }
            }
        }catch(Exception e){
            logger.error("上金所历史委托数据失败, ", e);
        }
    }

    /**
     * 插入上金所历史成交数据
     * @param traderId 交易员编号
     * @throws Exception
     */
    private void insertSJSMatchHistory(String traderId) throws Exception{
        try{
            FSJSQueryMatchHistoryRes res = tradeServiceCCHelper.querySJSMatchHistoryCC(traderId);
            if (res.getRecord() != null && res.getRecord().size() > 0) {
                for (SJSQueryMatchHistoryEntity entity : res.getRecord()) {
                    try{
                        GjsSJSHistoryTradeEntity e = new GjsSJSHistoryTradeEntity();
                        e.setTrader_id(traderId);
                        e.setExch_date(entity.getExch_date());
                        e.setExch_time(entity.getExch_time());
                        e.setMatch_no(entity.getMatch_no());
                        e.setOrder_no(entity.getOrder_no());
                        e.setMarket_id(entity.getMarket_id());
                        e.setProd_code(entity.getProd_code());
                        e.setExch_type(entity.getExch_type());
                        e.setMatch_price(Float.parseFloat(entity.getMatch_price()));
                        e.setMatch_amount(Integer.parseInt(entity.getMatch_amount()));
                        e.setBs(entity.getBs());
                        e.setOffset_flag(entity.getOffset_flag());
                        e.setDeli_flag(entity.getDeli_flag());
                        e.setExch_bal(Float.parseFloat(entity.getExch_bal()));
                        e.setExch_fare(Float.parseFloat(entity.getExch_fare()));
                        e.setMargin(Float.parseFloat(entity.getMargin()));
                        e.setTerm_type(entity.getTerm_type());
                        e.setLocal_order_no(entity.getLocal_order_no());
                        this.gjssjsHistoryTradeDao.insertSJSHistoryTrade(e);
                    }catch(Exception e){
                        logger.error("插入上金所历史成交数据失败, ", e);
                    }
                }
            }
        }catch(Exception e){
            logger.error("上金所历史成交数据失败, ", e);
        }
    }

    /**
     * 插入上金所历史出入金数据
     * @param traderId 交易员编号
     * @throws Exception
     */
    private void insertSJSTransferHistory(String traderId) throws Exception{
        try{
            FSJSQueryTransferHistoryRes res = tradeServiceCCHelper.querySJSTransferHistoryCC(traderId);
            if (res.getRecord() != null && res.getRecord().size() > 0) {
                for (SJSQueryTransferHistoryEntity entity : res.getRecord()) {
                    try{
                        GjsSJSHistoryTransferEntity e = new GjsSJSHistoryTransferEntity();
                        e.setTrader_id(traderId);
                        e.setExch_date(e.getExch_date());
                        e.setSerial_no(entity.getSerial_no());
                        e.setAccess_way(entity.getAccess_way());
                        e.setExch_bal(Float.parseFloat(entity.getExch_bal()));
                        e.setO_term_type(entity.getO_term_type());
                        e.setBk_plat_date(entity.getBk_plat_date());
                        e.setIn_account_flag(entity.getIn_account_flag());
                        e.setCheck_stat1(entity.getCheck_stat1());
                        e.setCheck_stat2(entity.getCheck_stat2());
                        e.setBk_rsp_code(entity.getBk_rsp_code());
                        e.setBk_rsp_msg(entity.getBk_rsp_msg());
                        e.setO_date(entity.getO_date());
                        this.gjssjsHistoryTransferDao.insertSJSHistoryTransfer(e);
                    }catch(Exception e){
                        logger.error("插入上金所历史出入金数据失败, ", e);
                    }
                }
            }
        }catch(Exception e){
            logger.error("上金所历史出入金数据失败, ", e);
        }
    }

    /**
     * 将FTP上：“+00000000205.5700”类型的数据格式化为：“205.57”
     * @param money 金额
     * @return String
     * @throws Exception
     */
    private static String formatMoney(String money) throws Exception {
        DecimalFormat fmt = new DecimalFormat("#############0.00");
        double d = Double.parseDouble(money);
        return fmt.format(d);
    }
}
