package com.furong.admin.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

import java.util.UUID;

/**
 * @author Origami
 * @date 2025/9/10
 */
public class DigesterUtils {

    /**
     * 生成盐
     * @return
     */
    public static String createSalt(){
        String uuid = UUID.randomUUID().toString();
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String salt = md5.digestHex(uuid + System.currentTimeMillis());
        return salt;
    }

    /**
     * 密码加密
     * @param password
     * @param salt
     * @return
     */
    public static String md5(String password, String salt){
        //1.md5加密
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String md5Password = md5.digestHex(password);

        //2.SHA-256加密
        Digester sha256 = new Digester(DigestAlgorithm.SHA256);
        return sha256.digestHex(md5Password + salt);
    }

}
