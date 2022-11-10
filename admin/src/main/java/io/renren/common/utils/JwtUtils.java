package io.renren.common.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    /**
     * jwt 登录密钥
     */
    private static final String LOGIN_SECRET = "ELsFI#pY!yDQW6E4dmZioONpntc!MZxN";

    /**
     * jwt 支付密钥
     */
    private static final String PAY_SECRET = "HRZZQKhw1jw0Hv5Mmnovk7vc#*I%c*K7";
    private static final String DF_SECRET = "Q5$FV9K*M^BP@*R!Y^InoGsaIbp57*mA";

    /**
     * 登录token 默认一天过期
     */
    public static String getToken(Map<String, String> map) {
        return getToken(map, LOGIN_SECRET, DateUtil.offsetDay(new Date(), 1));
    }

    /**
     * 支付token
     */
    public static String getPayToken(Map<String, String> map, Date date) {
        return getToken(map, PAY_SECRET, date);
    }

    public static String getDFToken(Map<String, String> map, Date date) {
        return getToken(map, DF_SECRET, date);
    }

    /**
     * 验证登录token
     */
    public static DecodedJWT verify(String token) {
        return verify(token, LOGIN_SECRET);
    }

    /**
     * 验证支付token
     */
    public static DecodedJWT verifyPay(String token) {
        return verify(token, PAY_SECRET);
    }
    public static DecodedJWT verifyDF(String token) {
        return verify(token, DF_SECRET);
    }


    /**
     * 生产token
     */
    public static String getToken(Map<String, String> map, String secret, Date date) {
        JWTCreator.Builder builder = JWT.create();

        //payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        //指定令牌的过期时间
        builder.withExpiresAt(date);
        return builder.sign(Algorithm.HMAC256(secret));
    }
    
    /**
     * 验证token
     */
    public static DecodedJWT verify(String token, String secret) {
        //如果有任何验证异常，此处都会抛出异常
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
            return decodedJWT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("orderNo", "tx1443597949912354816");
        map.put("merCode", "qepay");
        map.put("userPhone", "1122334455");
        map.put("bankCode", "IDPT0001");
        map.put("receiveName", "jack");
        map.put("receiveAccount", "11233434");
//        map.put("receivePhone", "1111112345");
        map.put("province", "11110111111");
//        map.put("paramJson", "{\"countryCode\":\"123\",\"ccy_no\":\"321\"}");
        String payToken = getDFToken(map, DateUtil.offsetDay(new Date(), 1));
        System.out.println(payToken);

    }
}
