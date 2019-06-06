package com.caimao.zeus.admin.controller;

import com.caimao.zeus.admin.entity.Rule;
import com.caimao.zeus.admin.entity.User;
import com.caimao.zeus.admin.service.AdminService;
import com.caimao.zeus.util.AdminMenuUtils;
import com.caimao.zeus.util.Constants;
import com.caimao.zeus.util.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 管理员控制器
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    /**
     * 管理员服务
     */
    @Resource
    private AdminService adminService;

    /**
     * 跳转登陆页
     *
     * @return 登陆模板位置
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() throws Exception {
        return new ModelAndView("admin/login");
    }

    /**
     * 登陆处理。
     *
     * @param account 账号名
     * @param pwd     账号密码
     * @return ajax响应
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam String account, @RequestParam String pwd) {
        try {
            HttpSession session = SessionAware.getSession();
            session.removeAttribute(Constants.SESSION_USER_KEY);

            User user = adminService.getLogin(account, pwd);
            if (user == null) {
                return jumpForFail(request, "请确认账号密码是否正确", "/login");
            } else {
                session.setAttribute(Constants.SESSION_USER_KEY, user);
                response.sendRedirect("/");
                return null;
            }
        } catch (Exception e) {
            return new ModelAndView("admin/login");
        }
    }

    /**
     * 变更密码
     *
     * @return
     */
    @RequestMapping(value = "/userPassword", method = RequestMethod.GET)
    public ModelAndView adminUserPassword() throws Exception {
        return new ModelAndView("admin/userPassword");
    }

    /**
     * 变更密码动作
     *
     * @return
     */
    @RequestMapping(value = "/userPassword", method = RequestMethod.POST)
    public ModelAndView doAdminUserPassword(HttpServletRequest request) throws Exception {
        String passwordOld = request.getParameter("passwordOld");
        String passwordNew = request.getParameter("passwordNew");
        String passwordNewConfirm = request.getParameter("passwordNewConfirm");

        User user = (User) request.getAttribute("adminUser");
        adminService.changeUserPassword(user.getId(), passwordOld, passwordNew, passwordNewConfirm);
        return jumpForSuccess(request, "修改成功", "/admin/userPassword");
    }

    /**
     * 退出系统
     *
     * @param request http请求
     * @return 登陆页的url
     */
    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        // 会话失效
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // 返回到登陆页
        return "redirect:/login";
    }

    /**
     * 管理员列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/userList")
    public ModelAndView adminUserList() throws Exception {
        SessionAware.getRequest().setAttribute("userList", adminService.getUserList());
        return new ModelAndView("admin/userList");
    }

    /**
     * 管理员编辑
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/userSave", method = RequestMethod.GET)
    public ModelAndView adminUserSave(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        if (id != null && !"".equals(id)) {
            User user = adminService.getUserById(Long.parseLong(id));
            if (user == null) {
                return jumpForFail(request, "管理员不存在", "/admin/userList");
            } else {
                request.setAttribute("user", user);
            }
        }
        return new ModelAndView("admin/userSave");
    }

    /**
     * 管理员保存
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/userSave", method = RequestMethod.POST)
    public ModelAndView doAdminUserSave(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        String account = request.getParameter("account");
        String pwd = request.getParameter("pwd");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String status = request.getParameter("status");
        if (("".equals(id) && "".equals("pwd")) || ("".equals(id) && "".equals("account"))) {
            return jumpForFail(request, "创建管理员时账号、密码不能为空", "/admin/userList");
        }
        Long userId = (id == null || "".equals(id)) ? null : Long.parseLong(id);
        Integer isSuccess = adminService.saveUser(account, pwd, name, phone, email, new Byte(status), userId);
        if (isSuccess == 1) {
            return jumpForSuccess(request, "管理员编辑成功", "/admin/userList");
        } else {
            return jumpForFail(request, "管理员编辑失败", "/admin/userList");
        }
    }

    /**
     * 管理员状态
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/userStatus", method = RequestMethod.GET)
    public void doAdminUserStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        String status = request.getParameter("status");
        if (id != null && !"".equals(id)) {
            adminService.userStatus(Long.parseLong(id), new Byte(status));
        }
        response.sendRedirect("/admin/userList");
    }

    /**
     * 管理员删除
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/userDel", method = RequestMethod.GET)
    public void doAdminUserDel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        if (id != null && !"".equals(id)) {
            adminService.userDel(Long.parseLong(id));
        }
        response.sendRedirect("/admin/userList");
    }

    /**
     * 管理员权限分配
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/userRule", method = RequestMethod.GET)
    public ModelAndView adminUserRule(@RequestParam(value = "id") Long userId) throws Exception {
        String ruleData = adminService.getUserRuleTree(userId);
        SessionAware.getRequest().setAttribute("ruleData", ruleData);
        return new ModelAndView("admin/userRule");
    }

    /**
     * 管理员权限分配动作
     *
     * @param userId
     * @param rules
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/userRule", method = RequestMethod.POST)
    public ModelAndView doAdminUserRule(@RequestParam(value = "id") Long userId, @RequestParam(value = "rule") String[] rules, HttpServletRequest request) throws Exception {
        adminService.userRuleUpdate(userId, rules);
        return jumpForSuccess(request, "分配成功", "/admin/userRule?id=" + userId);
    }

    /**
     * 权限列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ruleList", method = RequestMethod.GET)
    public ModelAndView adminRuleList(HttpServletRequest request) throws Exception {
        AdminMenuUtils adminMenuUtils = new AdminMenuUtils();
        request.setAttribute("ruleListTree", adminMenuUtils.ruleListTree(adminService.getAllRule()));
        return new ModelAndView("admin/ruleList");
    }

    /**
     * 权限编辑
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ruleSave", method = RequestMethod.GET)
    public ModelAndView adminRuleSave(HttpServletRequest request) throws Exception {
        //输出select
        AdminMenuUtils adminMenuUtils = new AdminMenuUtils();
        request.setAttribute("ruleListTree", adminMenuUtils.ruleListTreeOption(adminService.getAllRule()));
        //处理是否有pid
        String pid = request.getParameter("pid");
        request.setAttribute("pid", pid);
        //处理是否是编辑
        String id = request.getParameter("id");
        if (id != null && !"".equals(id)) {
            Rule rule = adminService.getRuleById(Long.parseLong(id));
            if (rule == null) {
                return jumpForFail(request, "权限不存在", "/admin/ruleList");
            } else {
                request.setAttribute("rule", rule);
            }
        }
        return new ModelAndView("admin/ruleSave");
    }

    /**
     * 权限编辑动作
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ruleSave", method = RequestMethod.POST)
    public ModelAndView doAdminRuleSave(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        String pid = request.getParameter("pid");
        String ruleName = request.getParameter("rule_name");
        String rule = request.getParameter("rule");
        String sort = request.getParameter("sort");
        String ruleType = request.getParameter("rule_type");
        String status = request.getParameter("status");
        String isPublic = request.getParameter("is_public");

        if (("".equals(rule) && !ruleType.equals("2"))) {
            return jumpForFail(request, "如果权限不为导航类型，请填写权限URL", "/admin/ruleList");
        }
        Long ruleId = (id == null || "".equals(id)) ? null : Long.parseLong(id);
        Long sortL = (id == null || "".equals(id)) ? 0 : Long.parseLong(sort);
        Integer isSuccess = adminService.saveRule(Long.parseLong(pid), ruleName, rule, sortL, new Byte(ruleType), new Byte(status), new Byte(isPublic), ruleId);
        if (isSuccess == 1) {
            return jumpForSuccess(request, "权限编辑成功", "/admin/ruleList");
        } else {
            return jumpForFail(request, "权限编辑失败", "/admin/ruleList");
        }
    }

    /**
     * 权限删除
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ruleDel", method = RequestMethod.GET)
    public void doAdminRuleDel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        if (id != null && !"".equals(id)) {
            //获取所有字权限
            AdminMenuUtils adminMenuUtils = new AdminMenuUtils();
            String ids = adminMenuUtils.ruleListTreeIds(adminService.getAllRule(), Long.parseLong(id));
            if ("".equals(ids)) {
                ids += id;
            } else {
                ids += "," + id;
            }
            adminService.ruleDel(ids);
        }
        response.sendRedirect("/admin/ruleList");
    }

    /**
     * 权限排序修改
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/ruleSort", method = RequestMethod.POST)
    public ModelAndView doAdminRuleSort(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        String sort = request.getParameter("sort");
        if (id != null && !"".equals(id) && sort != null && !"".equals(sort)) {
            adminService.ruleSort(Long.parseLong(id), Long.parseLong(sort));
        }
        return jumpForSuccess(request, "修改排序成功", "/admin/ruleList");
    }

    @RequestMapping(value = "/ruleHave", method = {RequestMethod.POST, RequestMethod.GET}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String doAdminRuleUser(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        if (id != null && !"".equals(id)) {
            return adminService.ruleHave(Long.parseLong(id));
        }
        return null;
    }
}