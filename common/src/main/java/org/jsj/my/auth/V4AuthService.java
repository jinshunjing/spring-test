package org.jsj.my.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsj.my.constant.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * Session token
 *
 * @author JSJ
 */
@Slf4j
public class V4AuthService {
    @Autowired
    private RedisTemplate<String, String> stringTemplate;

    /**
     * 查询用户是否已经有token
     *
     * @param uid
     * @param addr
     * @param app
     * @return
     */
    public String queryToken(String uid, String addr, String app) {
        ValueOperations<String, String> stringOps = stringTemplate.opsForValue();
        String uidKey = RedisKey.UID_TOKEN + uid + "@" + app;
        String addrKey = RedisKey.ADDR_TOKEN + addr + "@" + app;

        // TODO 先根据地址查询
        String token = stringOps.get(addrKey);
        if (StringUtils.isEmpty(token)) {
            // TODO 再根据uid查询
            token = stringOps.get(uidKey);
            // TODO 同步地址对应的token
            if (StringUtils.isNotEmpty(token)) {
                stringOps.set(addrKey, token);
                stringOps.set(RedisKey.TOKEN_ADDR + token, addr + "@" + app);
            }
        }
        return token;
    }

    /**
     * 创建新的token
     *
     * @param uid
     * @param addr
     * @param app
     * @return
     */
    public String renewToken(String uid, String addr, String app) {
        return generateToken(uid + "@" + app, addr + "@" + app);
    }

    /**
     * 设置token
     *
     * @param uid
     * @param addr
     * @param app
     * @param token
     */
    public void assignToken(String uid, String addr, String app, String token) {
        setToken(uid + "@" + app, addr + "@" + app, token);
    }

    /**
     * 查询用户的标识信息
     *
     * @param token
     * @return
     */
    public String getUid(String token) {
        String tokenKey = RedisKey.TOKEN_UID + token;
        return stringTemplate.opsForValue().get(tokenKey);
    }
    public String getAddr(String token) {
        String tokenKey = RedisKey.TOKEN_ADDR + token;
        return stringTemplate.opsForValue().get(tokenKey);
    }

    /**
     * 创建新的token，并且删除旧的token
     *
     * @param uid
     * @param addr
     * @return
     */
    private String generateToken(String uid, String addr) {
        // 添加新的Token
        String token = DigestUtils.md5Hex(
                RandomStringUtils.randomAlphanumeric(32) + System.currentTimeMillis());

        setToken(uid, addr, token);
        return token;
    }

    /**
     * 更新token，踢掉旧的token
     *
     * @param uid
     * @param addr
     * @param token
     */
    private void setToken(String uid, String addr, String token) {
        // 添加新的Token
        ValueOperations<String, String> stringOps = stringTemplate.opsForValue();
        stringOps.set(RedisKey.TOKEN_UID + token, uid);
        stringOps.set(RedisKey.TOKEN_ADDR + token, addr);

        // 踢掉旧的Token
        // TODO 先保留30天
        String uidKey = RedisKey.UID_TOKEN + uid;
        String oldToken = stringOps.get(uidKey);
        if(!StringUtils.isEmpty(oldToken) && !token.equalsIgnoreCase(oldToken)) {
            /// stringTemplate.delete(RedisKey.TOKEN_UID + oldToken);
            stringTemplate.expire(RedisKey.TOKEN_UID + oldToken, 30, TimeUnit.DAYS);
        }
        String addrKey = RedisKey.ADDR_TOKEN + addr;
        oldToken = stringOps.get(addrKey);
        if(!StringUtils.isEmpty(oldToken) && !token.equalsIgnoreCase(oldToken)) {
            /// stringTemplate.delete(RedisKey.TOKEN_ADDR + oldToken);
            stringTemplate.expire(RedisKey.TOKEN_ADDR + oldToken, 30, TimeUnit.DAYS);
        }

        // 注册新的Token
        stringOps.set(uidKey, token);
        stringOps.set(addrKey, token);
    }
}
