package com.caimao.account.server.service.user;


import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.account.api.entity.user.TpzUserEntity;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.account.api.service.IUserService;
import com.caimao.account.server.dao.userDao.TpzUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private TpzUserDao userDao;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 根据手机号，查找用户的user_id
     * @param phone 手机号
     * @return
     * @throws Exception
     */
    @Override
    public Long getUserIdByPhone(String phone) throws Exception {
        if (StringUtils.isBlank(phone)) {
            throw new CustomerException("手机号不能为空", 888888);
        }
        TpzUserEntity user = this.userDao.queryUserByPhone(phone);
        if (user == null) {
            return null;
        }
        return user.getUserId();
    }

}
