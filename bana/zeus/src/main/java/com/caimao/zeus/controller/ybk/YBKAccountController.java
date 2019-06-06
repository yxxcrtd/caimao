package com.caimao.zeus.controller.ybk;

import com.caimao.bana.api.entity.req.ybk.FYBKQueryAccountListReq;
import com.caimao.bana.api.entity.req.ybk.FYbkExchangeQueryListReq;
import com.caimao.bana.api.entity.ybk.YBKAccountEntity;
import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;
import com.caimao.bana.api.enums.ybk.ECardType;
import com.caimao.bana.api.enums.ybk.EYbkApplyStatus;
import com.caimao.bana.api.service.IUserBankCardService;
import com.caimao.bana.api.service.ybk.IYBKAccountService;
import com.caimao.bana.api.service.ybk.IYBKService;
import com.caimao.zeus.admin.controller.BaseController;
import com.caimao.zeus.util.PageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * 邮币卡交易所相关操作控制器
 * Created by Administrator on 2015/9/7.
 */
@Controller
@RequestMapping(value = "/ybk/account")
public class YBKAccountController extends BaseController {
    @Resource
    private IYBKService ybkService;

    @Resource
    public IUserBankCardService userBankCardService;

    @Resource
    public IYBKAccountService ybkAccountService;

    private static ResourceBundle appBundle = PropertyResourceBundle.getBundle("conf/application");

    //private static final String brokerId = "1011"; // 办理开户业务的经纪会员id

    /**
     * 邮币卡用户列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "exchangeIdApply", required = false, defaultValue = "0") Integer exchangeIdApply,
                             @RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception {
        ModelAndView mav = new ModelAndView("ybk/account/list");
        FYBKQueryAccountListReq req = new FYBKQueryAccountListReq();
        Integer limit = 10;
        req.setStart((page - 1) * limit);
        req.setLimit(limit);
        if (exchangeIdApply > 0) {
            req.setExchangeIdApply(exchangeIdApply);
        }
        if (status > 0) {
            req.setStatus(status);
        }
        FYBKQueryAccountListReq list = ybkAccountService.queryAccountWithPage(req);
        if (list.getItems() != null) {
            PageUtils pageUtils = new PageUtils(page, limit, list.getTotalCount(), String.format("/ybk/account/list?exchangeIdApply=%s&status=%s&page=", exchangeIdApply, status));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }
        // 获取交易所列表
        FYbkExchangeQueryListReq exchangeReq = new FYbkExchangeQueryListReq();
        //exchangeReq.setStatus(EYbkExchangeStatus.NORMAL.getCode());
        List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(exchangeReq);
        HashMap<Integer, YbkExchangeEntity> exchangeEntityHashMap = new HashMap<>();
        for (YbkExchangeEntity entity : exchangeEntityList) {
            exchangeEntityHashMap.put(entity.getId(), entity);
        }
        HashMap<String, String> cardMap = new HashMap<String, String>();
        for (ECardType type : ECardType.values()) {
            cardMap.put(type.getCode(), type.getValue());
        }

        HashMap<Integer, String> statusMap = new HashMap<Integer, String>();
        for (EYbkApplyStatus type : EYbkApplyStatus.values()) {
            statusMap.put(type.getCode(), type.getValue());
        }
        mav.addObject("exchangeList", exchangeEntityList);
        mav.addObject("exchangeMap", exchangeEntityHashMap);
        mav.addObject("cardMap", cardMap);
        mav.addObject("statusMap", statusMap);
        mav.addObject("accountList", list);
        mav.addObject("exchangeIdApply", exchangeIdApply);
        mav.addObject("status", status);
        return mav;
    }

    /**
     * 检查是否有新的邮币卡申请的东东
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/check_new_apply")
    public FYBKQueryAccountListReq queryNewAccountApply() throws Exception {
        FYBKQueryAccountListReq req = new FYBKQueryAccountListReq();
        req.setLimit(10);
        req.setStatus(EYbkApplyStatus.INIT.getCode());
        return this.ybkAccountService.queryAccountWithPage(req);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam(value = "id") Long id) throws Exception {
        ModelAndView mav = new ModelAndView("ybk/account/edit");
        YBKAccountEntity account = ybkAccountService.queryById(id);
        HashMap<String, String> cardMap = new HashMap<String, String>();
        for (ECardType type : ECardType.values()) {
            cardMap.put(type.getCode(), type.getValue());
        }
        mav.addObject("cardMap", cardMap);
        HashMap<Integer, String> statusMap = new HashMap<Integer, String>();
        for (EYbkApplyStatus type : EYbkApplyStatus.values()) {
            statusMap.put(type.getCode(), type.getValue());
        }
        mav.addObject("statusMap", statusMap);
        mav.addObject("account", account);
        mav.addObject("bankTypeInfo", userBankCardService.getBankInfoById(account.getBankCode(), 3L));
        mav.addObject("exchange", ybkService.getExchangeById(account.getExchangeIdApply()));

        mav.addObject("ybkUrl", appBundle.getString("ybkUrl"));

        System.out.println(appBundle.getString("ybkUrl"));

        return mav;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request, YBKAccountEntity entity) throws Exception {
        ybkAccountService.update(entity);
        return jumpForSuccess(request, "保存成功", "/ybk/account/list");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(HttpServletRequest request, @RequestParam(value = "id") Integer id) throws Exception {
        ybkAccountService.delete(id);
        return jumpForSuccess(request, "删除成功", "/ybk/account/list");
    }

//    /**
//     * 获得开户验证码
//     * @param request
//     * @param response
//     * @throws Exception
//     */
//    @RequestMapping(value = "/getYZM", method = RequestMethod.GET)
//    public void getYZM(HttpServletRequest request,HttpServletResponse response) throws Exception {
//        String cookie = getCookie("http://180.97.2.74:16908/SelfOpenAccount/image.jsp", response);
//        System.out.println("cookie=" + cookie);
//        request.getSession().setAttribute("accountCookie", cookie);
//    }
//
//    /**
//     * 开户
//     * @param request
//     * @param response
//     * @throws Exception
//     */
//    @RequestMapping(value = "/openAccount", method = RequestMethod.GET)
//    public void openAccount(HttpServletRequest request,HttpServletResponse response) throws Exception {
//        String yzm = ServletRequestUtils.getStringParameter(request, "yzm", "");
//        if (yzm.isEmpty()){
//            sendMessage("验证码为空", response);
//            return;
//        }
//        String cookie = (String)request.getSession().getAttribute("accountCookie");
//        if (cookie==null||cookie.isEmpty()){
//            sendMessage("请重新获取验证码",response);
//            return;
//        }
//        long id = ServletRequestUtils.getLongParameter(request, "id", 0);
//        if (id==0){
//            return;
//        }
//        YBKAccountEntity entity = ybkAccountService.queryById(id);
//        if (entity==null){
//            return;
//        }
//        String url = "http://180.97.2.74:16908/SelfOpenAccount/firmController.fir?funcflg=eidtFirm";
//        String result = postFile(url, cookie, makeParamsMap(entity, yzm));
//        System.out.println(result);
//        parseHtmlStr(result);
//        sendMessage("success", response);
//    }
//
//    public Map<String,String> makeParamsMap(YBKAccountEntity entity, String yzm){
//        Map<String,String> map = new HashMap<String,String>();
//        map.put("name", entity.getUserName());
//        map.put("registeredPhoneNo", String.valueOf(entity.getPhoneNo()));
//        map.put("cardType", String.valueOf(entity.getCardType()));
//        map.put("cardNumber", entity.getCardNumber());
//        map.put("attach",entity.getCardPath());
//        map.put("recommendBankCode", String.valueOf(entity.getBankCode()));
//        map.put("bankAccount", entity.getBankNum());
//        map.put("selectp", entity.getProvince());
//        map.put("address1", entity.getCity());
//        map.put("address", entity.getStreet());
//        map.put("sex", String.valueOf(entity.getSex()));
//        map.put("contactMan", entity.getContactMan());
//        map.put("ContacterPhoneNo",  String.valueOf(entity.getContacterPhoneNo()));
//        map.put("yanzhengma", yzm);
//        map.put("brokerId", brokerId);// 办理开户业务的经纪会员id
//        map.put("type", "3");// 投资者类型:3自然人投资者，1机构投资者
////        map.put("ck", "true");
//        return map;
//    }
//
//    /**
//     * 获得cookie，并将验证码显示在后台
//     * @param url
//     * @param response
//     * @return
//     */
//    public String getCookie(String url, HttpServletResponse response){
//        String result = "";
//        HttpClient httpClient = new HttpClient();
//        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
//
//        GetMethod getMethod = new GetMethod(url);
//        getMethod.getParams().setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
//        try {
//            httpClient.executeMethod(getMethod);
//            // 整理cookie
//            Cookie[] cookies = httpClient.getState().getCookies();
//            StringBuffer tmpcookies = new StringBuffer();
//            for (Cookie c : cookies) {
//                tmpcookies.append(c.toString() + ";");
//            }
//            result = tmpcookies.toString();
//            // 返回图片
//            getYZM(getMethod.getResponseBodyAsStream(),response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            getMethod.releaseConnection();
//        }
//        return result;
//    }
//
//    /**
//     * 向页面输出图片
//     * @param inputStream
//     * @param response
//     * @throws Exception
//     */
//    public void getYZM(InputStream inputStream, HttpServletResponse response) throws Exception{
//        response.setContentType("multipart/form-data");
//        ServletOutputStream outputStream = response.getOutputStream();
//        int data = inputStream.read();
//        while(data!=-1){
//            outputStream.write(data);
//            data = inputStream.read();
//        }
//        outputStream.flush();
//        inputStream.close();
//        outputStream.close();
//    }
//
//    /**
//     * 上传开户信息，包括文件的上传
//     * @param url
//     * @param cookie
//     * @param params
//     * @return
//     */
//    public String postFile(String url, String cookie, Map<String,String> params){
//        String result = "";
//        HttpClient httpClient = new HttpClient();
//        PostMethod postMethod = new PostMethod(url);
//        postMethod.setRequestHeader("cookie", cookie);
//        try{
//            MultipartRequestEntity entity = new MultipartRequestEntity(createParts(params), postMethod.getParams());
//            postMethod.setRequestEntity(entity);
//            int status = httpClient.executeMethod(postMethod);
//            if (status == HttpStatus.SC_OK){
//                result = postMethod.getResponseBodyAsString();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            postMethod.releaseConnection();
//        }
//        return result;
//    }
//
//    /**
//     * 创建参数数组
//     * @param params
//     * @return
//     * @throws Exception
//     */
//    public Part[] createParts(Map<String,String> params)throws Exception{
//        Part[] parts = new Part[params.size()];
//        int count = 0;
//        for(Map.Entry<String,String> entry : params.entrySet()){
//            String key = entry.getKey();
//            if(key.indexOf("/")>-1 || key.indexOf("\\")>-1){// 文件参数
//                parts[count] = new FilePart(key, new File(entry.getValue()));
//            }else{
//                parts[count] = new StringPart(key, entry.getValue());
//            }
//            count++;
//        }
//        return parts;
//    }
//
//    public String parseHtmlStr(String dataStr)throws Exception{
//        Parser parser = new Parser(dataStr);
//        TagNameFilter scriptFilter = new TagNameFilter("script");// script过滤器
//        NodeList scriptList = parser.extractAllNodesThatMatch(scriptFilter);
//        System.out.println("tableList.size()=="+scriptList.size());
//        SimpleNodeIterator trIterator = scriptList.elements();
//        while(trIterator.hasMoreNodes()) {
//            String text = trIterator.nextNode().getChildren().toHtml().trim();
//            if (text.startsWith("alert")){
//                return text.replaceAll("alert\\('","").replaceAll("'\\)","");
//            }
//        }
//        return "";
//    }
//
//    public void sendMessage(String message, HttpServletResponse response)throws Exception{
//        PrintWriter writer = response.getWriter();
//        writer.print(message);
//        writer.close();
//    }
}
