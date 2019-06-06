package com.caimao.bana.server.dao.userDao;

import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.req.FUserListReq;
import com.caimao.bana.api.entity.res.FUserListRes;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理操作
 * Created by WangXu on 2015/4/22.
 */
@Repository
public class TpzUserDao extends SqlSessionDaoSupport {


    /**
     * 根据用户ID获取用户信息 (FOR UPDATE)
     *
     * @param userId
     * @return
     */
    public TpzUserEntity getUserById(Long userId) {
        return getSqlSession().selectOne("TpzUser.getUserById", userId);
    }

    public TpzUserEntity getByIdForUpdate(Long userId) {
        return getSqlSession().selectOne("TpzUser.getByIdForUpdate", userId);
    }

    /**
     * @param user
     * @return
     */
    public TpzUserEntity queryUserByNameOrMobile(TpzUserEntity user) {
        return getSqlSession().selectOne("TpzUser.queryUserByNameOrMobile", user);
    }

    /**
     * @param user
     */
    public void updateLoginPwd(TpzUserEntity user) {
        getSqlSession().update("TpzUser.updateLoginPwd", user);
    }

    /**
     * 根据手机号码获取用户信息
     *
     * @param phone
     * @return
     */
    public TpzUserEntity queryUserByPhone(String phone) {
        return getSqlSession().selectOne("TpzUser.queryUserByPhone", phone);
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param userName 用户名
     * @return
     */
    public TpzUserEntity isUserNameExist(String userName) {
        return getSqlSession().selectOne("TpzUser.isUserNameExist", userName);
    }

    /**
     * @param userId
     * @return
     */
    public TpzUserEntity getById(Long userId) {
        return getSqlSession().selectOne("TpzUser.getById", userId);
    }

    /**
     * 根据财猫的ID获取用户信息
     *
     * @param caimaoId 自定义的财猫ID
     * @return
     */
    public TpzUserEntity getUserByCaimaoId(Long caimaoId) {
        return getSqlSession().selectOne("TpzUser.getByCaimaoId", caimaoId);
    }

    /**
     * 根据火币UID获取用户信息
     *
     * @param huobiUid huobiUID
     * @return
     */
    public TpzUserEntity getUserByHuobiUid(Long huobiUid) {
        return getSqlSession().selectOne("TpzUser.getByHuobiUid", huobiUid);
    }


    /**
     * 保存用户信息
     *
     * @param userEntity 用户信息
     * @return 返回值
     */
    public int save(TpzUserEntity userEntity) {
        return getSqlSession().insert("TpzUser.save", userEntity);
    }

    /**
     * 根据用户名与密码，查询用户信息
     *
     * @param userEntity 用户信息
     * @return 用户信息
     */
    public TpzUserEntity queryUserBynameAndPwd(TpzUserEntity userEntity) {
        return getSqlSession().selectOne("TpzUser.queryUserBynameAndPwd", userEntity);
    }

    /**
     * 根据手机号与密码，查询用户信息
     *
     * @param userEntity 用户信息
     * @return 用户信息
     */
    public TpzUserEntity queryUserBymobileAndPwd(TpzUserEntity userEntity) {
        return getSqlSession().selectOne("TpzUser.queryUserBymobileAndPwd", userEntity);
    }

    /**
     * 根据传递对象中的值，查询用户列表信息
     *
     * @param userEntity 用户信息
     * @return 用户信息列表
     */
    public List<TpzUserEntity> queryUser(TpzUserEntity userEntity) {
        return getSqlSession().selectList("TpzUser.queryUser", userEntity);
    }

    /**
     * 用户登陆成功后，更新用户表中的相关信息
     *
     * @param userEntity 用户信息
     * @return 返回值
     */
    public int updateLoginSuccess(TpzUserEntity userEntity) {
        return getSqlSession().update("TpzUser.updateLoginSuccess", userEntity);
    }

    /**
     * 用户登陆失败后，更新用户表中的相关信息
     *
     * @param userId 用户ID
     * @return 返回值
     */
    public int updateLoginFail(Long userId) {
        return getSqlSession().update("TpzUser.updateLoginFail", userId);
    }

    /**
     * 更新用户积分
     *
     * @param userEntity
     * @return
     */
    public int updateScore(TpzUserEntity userEntity) {
        return getSqlSession().update("TpzUser.updateScore", userEntity);
    }

    /**
     * 更新用户表中的信息
     *
     * @param userEntity 用户信息
     * @return 返回值
     */
    public int update(TpzUserEntity userEntity) {
        return getSqlSession().update("TpzUser.update", userEntity);
    }

    /**
     * 更新用户邮箱
     * @param userEntity
     * @return
     */
    public int updateEmail(TpzUserEntity userEntity) {
        return getSqlSession().update("TpzUser.updateEmail", userEntity);
    }

    public Integer doChangeUserRefUserId(Long userId, Long refUserId) throws Exception{
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("userId", userId);
        updateInfo.put("refUserId", refUserId);
        return getSqlSession().update("TpzUser.doChangeUserRefUserId", updateInfo);
    }

    /**
     * 获取指定用户邀请的用户，在指定的时间之后
     * @param userId
     * @param date
     * @return
     */
    public List<TpzUserEntity> getUserRefList(Long userId, Date date) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("date", date);
        return this.getSqlSession().selectList("TpzUser.getUserRefList", paramsMap);
    }

    public List<FUserListRes> queryUserListWithPage(FUserListReq req){
        return this.getSqlSession().selectList("TpzUser.queryUserListWithPage", req);
    }

    public int queryUserCount() throws Exception {
        return this.getSqlSession().selectOne("TpzUser.queryUserCount");
    }

    public int queryRegisterCount(String startDate, String endDate) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return this.getSqlSession().selectOne("TpzUser.queryRegisterCount", params);
    }

    public List<FUserListRes> queryGjsUserStatisticsListWithPage(FUserListReq req){
        return this.getSqlSession().selectList("TpzUser.queryGjsUserStatisticsListWithPage", req);
    }

    public List<FUserListRes> queryGjsUserOpenAccountStatisticsListWithPage(FUserListReq req){
        return this.getSqlSession().selectList("TpzUser.queryGjsUserOpenAccountStatisticsListWithPage", req);
    }

    public List<FUserListRes> queryGjsUserTradeStatisticsListWithPage(FUserListReq req){
        return this.getSqlSession().selectList("TpzUser.queryGjsUserTradeStatisticsListWithPage", req);
    }

    public List<FUserListRes> queryGjsUserMoneyStatisticsListWithPage(FUserListReq req){
        return this.getSqlSession().selectList("TpzUser.queryGjsUserMoneyStatisticsListWithPage", req);
    }

    public List<FUserListRes> queryGjsUserTransformStatisticsListWithPage(FUserListReq req){
        return this.getSqlSession().selectList("TpzUser.queryGjsUserTransformStatisticsListWithPage", req);
    }

}
