package com.fmall.bana.interceptor;

import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 邀请链接的那个 i 记录到session中的拦截器
 * Created by Administrator on 2015/10/12.
 */
public class InviteSession extends HandlerInterceptorAdapter {

    @Resource
    private IUserService userService;

    // 检查是否有 i 参数，如果有进行记录
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //邀请用户记录
        String i = request.getParameter("i");
        if (StringUtils.isNotBlank(i)) {
            try {
                Long li = Long.parseLong(i);
                TpzUserEntity tpzUserEntity = userService.getByCaimaoId(li);
                if (tpzUserEntity != null)
                    request.getSession().setAttribute("u", String.valueOf(tpzUserEntity.getUserId()));
            } catch (Exception ignored) {
            }
        }
        return true;
    }


}
