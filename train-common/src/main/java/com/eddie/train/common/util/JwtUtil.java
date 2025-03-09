package com.eddie.train.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.crypto.GlobalBouncyCastleProvider;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final Logger LOG = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * 盐值很重要，不能泄漏，且每个项目都应该不一样，可以放到配置文件中
     */
    private static final String key = "Eddie12306";

    public static String createToken(Object bean) {
        Map<String, Object> beanToMap = BeanUtil.beanToMap(bean);
        LOG.info("开始生成JWT token");
        GlobalBouncyCastleProvider.setUseBouncyCastle(false);
        DateTime now = DateTime.now();
        DateTime expTime = now.offsetNew(DateField.HOUR, 24);//定义token过期时间为24小时
        Map<String, Object> payload = new HashMap<>();
        // 签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        // 过期时间
        payload.put(JWTPayload.EXPIRES_AT, expTime);
        // 生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        // 将 beanToMap 中的所有键值对添加到 payload 中
        payload.putAll(beanToMap);
        String token = JWTUtil.createToken(payload, key.getBytes());
        LOG.info("生成JWT token：{}", token);
        return token;
    }

    public static boolean validate(String token) {
        LOG.info("开始JWT token校验，token：{}", token);
        GlobalBouncyCastleProvider.setUseBouncyCastle(false);
        JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
        // validate包含了verify
        boolean validate = jwt.validate(0);
        LOG.info("JWT token校验结果：{}", validate);
        return validate;
    }

    public static JSONObject getJSONObject(String token) {
        GlobalBouncyCastleProvider.setUseBouncyCastle(false);
        JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
        JSONObject payloads = jwt.getPayloads();
        payloads.remove(JWTPayload.ISSUED_AT);
        payloads.remove(JWTPayload.EXPIRES_AT);
        payloads.remove(JWTPayload.NOT_BEFORE);
        LOG.info("根据token获取原始内容：{}", payloads);
        return payloads;
    }

    public static void main(String[] args) {
        // 示例 Bean 类
        class User {
            private Long id;
            private String mobile;
            private String username;

            public User(Long id, String mobile, String username) {
                this.id = id;
                this.mobile = mobile;
                this.username = username;
            }
        }

        User user = new User(1L, "1234567890", "testUser");
        String token = createToken(user);

//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
//                "eyJuYmYiOjE3MjEwMDI3NjUsIm1vYmlsZSI6IjEyMyIsImlkIjoxLCJleHAiOjE3MjEwODkxNjUsImlhdCI6MTcyMTAwMjc2NX0." +
//                "EVFUiVnnUGYxAqasIFtq_LycjtPwIDjyjOZTniNdeFk";
        validate(token);

        getJSONObject(token);
    }
}
