package com.fmall.bana.utils.crypto;

import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.common.api.utils.crypto.Base64Utils;
import com.caimao.bana.common.api.utils.crypto.RSAUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RSA
 * Created by Administrator on 2016/1/26.
 */
public class RSA {

    /**
     * 将加密的密文转换成明文（密文是BASE64编码的或16进制字符串）
     * 兼容没有使用编码的字符串，直接返回（TODO 之后需要去掉）
     * @param mi    BASE64编码的密文
     * @return
     * @throws Exception
     */
    public static String decodeByPwd(String mi) throws Exception {
        try {
            if (mi == null) return null;
            if (mi.length() <= 30) return mi;
            // 检验是否是16进制字符串
            Pattern pattern = Pattern.compile("^[0-9a-f]{256}$");
            Matcher matcher = pattern.matcher(mi);
            if (! matcher.matches()) {
                byte[] encodedDataBytes = Base64Utils.decode(mi);
                mi = RSAUtils.bytes2Hex(encodedDataBytes);
            }
            return RSAUtils.decryptByPrivateKey(mi);
        } catch (Exception e) {
            throw new CustomerException("解密密码错误", 888888);
        }
    }
}
