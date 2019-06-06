package com.caimao.bana.server.service.impl;

import com.caimao.bana.api.entity.CouponReceiveEntity;
import com.caimao.bana.api.entity.CouponsEntity;
import com.caimao.bana.api.enums.CouponReceiveStatus;
import com.caimao.bana.api.enums.CouponStatus;
import com.caimao.bana.server.dao.FreeCouponsDao;
import com.caimao.bana.api.entity.CouponChannelEntity;
import com.caimao.bana.api.enums.CouponChannelType;
import com.caimao.bana.api.service.FreeCouponsService;
import com.caimao.bana.server.utils.ExceptionUtils;
import com.huobi.commons.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service("freeCouponsService")
public class FreeCouponsServiceImpl implements FreeCouponsService {

    private static final Logger logger = LoggerFactory.getLogger(FreeCouponsServiceImpl.class);

    @Resource
    private ExceptionUtils exceptionUtils;

    private String getPackageData(BigDecimal money, Integer num) throws Exception {
        List<BigDecimal> data = new ArrayList<BigDecimal>();
        BigDecimal minBegin = new BigDecimal(10);
        for (int i = 0; i <= num; i++) {
            if (i == num) {
                data.add(money);
            } else {
                BigDecimal maxEnd = money.divide(new BigDecimal(num + 1 - i), 0, BigDecimal.ROUND_DOWN);
                maxEnd = maxEnd.multiply(new BigDecimal(2)).setScale(0, BigDecimal.ROUND_DOWN);

                // 如果最大值为小于等于 0 的，默认使用最小值
                if (maxEnd.compareTo(new BigDecimal(10)) < 0) maxEnd = minBegin;
                Random rand = new Random();
                int randMoneyTmp = rand.nextInt(maxEnd.subtract(minBegin).setScale(0, BigDecimal.ROUND_DOWN).intValue() + 1) + minBegin.intValue();
                BigDecimal randMoney = new BigDecimal(randMoneyTmp);
                // 如果取到最大边界值的，默认使用最小值
                if (randMoney.compareTo(maxEnd) >= 0) {
                    randMoney = maxEnd;
                }
                data.add(randMoney);
                money = money.subtract(randMoney).setScale(0, BigDecimal.ROUND_DOWN);
            }
        }
        Collections.sort(data, new Comparator<BigDecimal>() {
            @Override
            public int compare(BigDecimal o1, BigDecimal o2) {
                return o1.compareTo(o2);
            }
        });
        return JsonUtil.toJson(data);
    }

    @Resource
    private FreeCouponsDao freeCouponsDao;

    @Override
    public void insertCouponChannel(CouponChannelType couponChannelType, BigDecimal money, BigDecimal scale, Integer amount, Byte status) throws Exception {
        CouponChannelEntity couponChannel = new CouponChannelEntity();
        couponChannel.setChannelType(couponChannelType.getValue());
        couponChannel.setMoney(money);
        couponChannel.setScale(scale);
        couponChannel.setAmount(amount);
        couponChannel.setStatus(status);
        freeCouponsDao.insertCouponsChannel(couponChannel);
    }

    @Override
    public void updateCouponChannel(Long id, Map<String, Object> updateInfo) throws Exception {
        freeCouponsDao.updateCouponsChannel(id, updateInfo);
    }

    @Override
    public CouponChannelEntity queryCouponChannel(Long id) throws Exception {
        return freeCouponsDao.getCouponChannel(id);
    }

    @Override
    public CouponChannelEntity lotteryCouponChannel(CouponChannelType couponChannelType, BigDecimal money) throws Exception{
        return freeCouponsDao.lotteryCouponChannel(couponChannelType.getValue(), money);
    }

    @Override
    public List<CouponChannelEntity> queryCouponChannelList(Byte status, Integer limitStart, Integer limitEnd) throws Exception {
        return freeCouponsDao.queryCouponChannelList(status, limitStart, limitEnd);
    }

    @Override
    public Long insertCoupon(Long userId, Long channelId, BigDecimal money, Integer amount, Long extId) throws Exception {
        //查询是否发放过券包
        boolean couponsSend = freeCouponsDao.queryCouponsSend(userId, channelId, extId);
        if (couponsSend) {
            throw exceptionUtils.getCustomerException(2);
        } else {
            CouponsEntity coupons = new CouponsEntity();
            coupons.setUserId(userId);
            coupons.setChannelId(channelId);
            coupons.setMoney(money);
            coupons.setAmount(amount);
            coupons.setPackageData(getPackageData(money, amount));
            coupons.setExtId(extId);
            coupons.setEndTime((int) System.currentTimeMillis() / 1000);
            freeCouponsDao.insertCoupons(coupons);
            return coupons.getId();
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateCoupon(Long id, Map<String, Object> updateInfo) throws Exception {
        CouponsEntity coupons = freeCouponsDao.queryCouponsForUpdate(id);
        //TODO 目前这里有问题 需要计算相应字段的数据
        int affectRow = freeCouponsDao.updateCoupons(coupons.getId(), updateInfo);
        if (affectRow != 1) throw exceptionUtils.getCustomerException(1);
    }

    @Override
    public CouponsEntity queryCoupons(Long id) throws Exception {
        return freeCouponsDao.queryCoupons(id);
    }

    @Override
    public List<CouponsEntity> queryCouponsList(Long userId, Byte status, Integer limitStart, Integer limitEnd) throws Exception {
        return freeCouponsDao.queryCouponsList(userId, status, limitStart, limitEnd);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void insertCouponReceive(Long couponId, Byte receiveType, Long phone) throws Exception {
        //检测是否领取过券
        boolean couponsReceiveSend = freeCouponsDao.queryCouponsReceiveSend(couponId, receiveType, phone);
        if (couponsReceiveSend) {
            throw exceptionUtils.getCustomerException(3);
        } else {
            //获得券包中的一个券
            CouponsEntity coupons = freeCouponsDao.queryCouponsForUpdate(couponId);
            if (coupons == null) throw exceptionUtils.getCustomerException(4);
            if (coupons.getStatus().getValue() != 0 || coupons.getAmount().equals(coupons.getAmountUsed())) {
                throw exceptionUtils.getCustomerException(5);
            }

            String packageDataStr = coupons.getPackageData();
            List packageData = JsonUtil.toObject(packageDataStr, List.class);
            BigDecimal money = new BigDecimal(packageData.get(0).toString());
            if (money.compareTo(new BigDecimal(10)) < 0) {
                throw exceptionUtils.getCustomerException(6);
            }
            packageData.remove(0);
            Map<String, Object> updateInfo = new HashMap<>();
            updateInfo.put("moneyUsed", coupons.getMoneyUsed().add(money));
            updateInfo.put("amountUsed", coupons.getAmountUsed() + 1);
            updateInfo.put("packageData", JsonUtil.toJson(packageData));
            if (coupons.getAmount() == (coupons.getAmountUsed() + 1)) {
                updateInfo.put("status", CouponStatus.STATUS_EXHAUST.getValue());
            }
            Integer affectRow = freeCouponsDao.updateCoupons(couponId, updateInfo);
            if (affectRow != 1) {
                throw exceptionUtils.getCustomerException(7);
            }

            //TODO 根据手机查找user_id
            //插入券
            CouponReceiveEntity couponReceive = new CouponReceiveEntity();
            couponReceive.setCouponId(couponId);
            couponReceive.setUserId((long) 1);
            couponReceive.setMoney(money);
            couponReceive.setReceiveType(receiveType);
            couponReceive.setPhone(phone);
            freeCouponsDao.insertCouponReceive(couponReceive);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateCouponReceive(Long id, Map<String, Object> updateInfo) throws Exception {
        freeCouponsDao.queryCouponReceiveForUpdate(id);
        freeCouponsDao.updateCouponReceive(id, updateInfo);
    }

    @Override
    public CouponReceiveEntity queryCouponReceive(Long id) throws Exception {
        return freeCouponsDao.queryCouponReceive(id);
    }

    @Override
    public List<CouponReceiveEntity> queryCouponReceiveList(Long couponId, Long userId, Byte receiveType, Long phone, CouponReceiveStatus statusValue, Integer limitStart, Integer limitEnd) throws Exception {
        return freeCouponsDao.queryCouponReceiveList(couponId, userId, receiveType, phone, statusValue, limitStart, limitEnd);
    }
}
