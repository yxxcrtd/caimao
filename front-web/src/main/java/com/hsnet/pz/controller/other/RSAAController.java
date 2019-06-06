package com.hsnet.pz.controller.other;

import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Hex;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import com.hsnet.pz.ao.util.RSAUtils;

/**
 * RSA 服务Action
 * 
 * @author: xuebj07252
 * @since: 2013-10-15 下午5:29:11
 * @history:
 */
@RestController
@RequestMapping(value = "/sec")
public class RSAAController
{
    
    /**
     * 获取RSA公钥系数和指数
     * 
     * @return
     * @create: 2012-5-30 上午10:42:51 xuwf
     * @history:
     */
    @RequestMapping(value = "/rsa",method = RequestMethod.GET)
    public Map<String, String> getModulusExponent()
    {
        
        RSAPublicKey publicKey = RSAUtils.getDefaultPublicKey();
        // 系数
        String modulus = new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
        // 指数
        String exponent = new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));
        Map<String, String> map = new HashMap<String, String>();
        map.put("modulus", modulus);
        map.put("exponent", exponent);
       return map;
    }
}
