package com.wangyz.weather.util;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author wangyz
 * SignUtil
 */
public class SignUtil {

    /**
     * 获取Sign
     *
     * @param params
     * @param secret
     * @return
     * @throws IOException
     */
    @TargetApi(Build.VERSION_CODES.O)
    public static String getSignature(HashMap params, String secret) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
        Map sortedParams = new TreeMap(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder baseString = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
            //sign参数 和 空值参数 不加入算法
            if (param.getValue() != null && !"".equals(param.getKey().trim()) && !"sign".equals(param.getKey().trim()) && !"key".equals(param.getKey().trim()) && !"".equals(param.getValue().trim())) {
                baseString.append(param.getKey().trim()).append("=").append(param.getValue().trim()).append("&");
            }
        }
        if (baseString.length() > 0) {
            baseString.deleteCharAt(baseString.length() - 1).append(secret);
        }

        // 使用MD5对待签名串求签
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(baseString.toString().getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (GeneralSecurityException ex) {
            throw new IOException(ex);
        }
    }
}
