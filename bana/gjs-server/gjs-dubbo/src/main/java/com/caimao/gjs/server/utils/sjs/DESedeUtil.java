package com.caimao.gjs.server.utils.sjs;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class DESedeUtil {
    public static final String KEY_ALGORITHM = "DESede";

    public static final String CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";

    private static Key toKey(byte[] key) throws Exception {
        DESedeKeySpec dks = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generateSecret(dks);
    }

    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        Key k = toKey(key);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        Key k = toKey(key);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    public static void main(String[] args) throws Exception {
        String sKey = "ABCDEF0123456789abcdef11";
        String sSrc = "<?xml version=\"1.0\" encoding=\"GBK\"?><request><head><h_exch_code>208801</h_exch_code><h_bank_no>0020</h_bank_no><h_user_id>1120888233</h_user_id><h_branch_id>B00008211</h_branch_id><h_fact_date></h_fact_date>2013-03-01<h_fact_time></h_fact_time>15:58:39<h_exch_date></h_exch_date><h_serial_no>12345678</h_serial_no><h_rsp_code></h_rsp_code><h_rsp_msg></h_rsp_msg></head><body><record></record></body></request>";
        byte[] encode = DESedeUtil.encrypt(sSrc.getBytes(), sKey.getBytes());
        String sBase64 = Base64.encode(encode);
        System.out.println(sBase64);
//		String sEncode = MsgUtil.ByteToString(encode);
//		System.out.println(sEncode);
//		byte[] decode = DESedeUtil.decrypt(sEncode.getBytes(), sKey.getBytes());
//		String sDecode = MsgUtil.ByteToString(decode);
//		System.out.println(sDecode);
    }
}
