package org.jsj.my.service;

import org.apache.commons.lang3.StringUtils;
import org.jsj.my.model.MessageAcceptor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * webSocket缓存管理
 *
 * @author JSJ
 */
public class MessageAcceptorCache {

    public static int ANONYMOUS_CAPCITY = 10000;

    /**
     * uid -> MessageAcceptor
     */
    public static ConcurrentHashMap<String, MessageAcceptor> params = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, MessageAcceptor> anonymous = new ConcurrentHashMap<>();

    /**
     * sessionId -> uid
     */
    public static ConcurrentHashMap<String, String> sessionIdUidMap = new ConcurrentHashMap<>();

    /**
     * 存放sessionId与uid的关联关系
     *
     * @param sessionId
     * @param uid
     */
    public static void putSessionId(String sessionId, String uid) {
        if (StringUtils.isEmpty(sessionId) || StringUtils.isEmpty(uid)) {
            return;
        }

        sessionIdUidMap.put(sessionId, uid);
    }

    /**
     * 根据sessionId获取uid
     *
     * @param sessionId
     * @return
     */
    public static String getUidBySessionId(String sessionId) {
        if (StringUtils.isEmpty(sessionId)) {
            return null;
        }
        return sessionIdUidMap.get(sessionId);
    }

    /**
     * 根据sessionId去除uid
     *
     * @param key
     */
    public static void removeUid(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        sessionIdUidMap.remove(key);
    }

    /**
     * 根据uid去除MessageAcceptor
     *
     * @param key
     */
    public static void removeAcceptor(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        params.remove(key);
        anonymous.remove(key);
    }

    /**
     * 根据uid更新MessageAcceptor
     *
     * @param key
     * @param acceptor
     */
    public static boolean update(String key, MessageAcceptor acceptor) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }

        acceptor.setAnonymous(false);
        params.put(key, acceptor);
        return true;
    }
    public static boolean updateAnonymous(String key, MessageAcceptor acceptor) {
        // FIXME: 控制匿名用户的总数，防止DOS攻击
        if (ANONYMOUS_CAPCITY <= anonymous.size()) {
            return false;
        }

        if (StringUtils.isEmpty(key)) {
            return false;
        }
        acceptor.setAnonymous(true);
        anonymous.put(key, acceptor);
        return true;
    }

    /**
     * 返回所有键值对
     *
     * @return
     */
    public static ConcurrentHashMap<String, MessageAcceptor> getParams() {
        return new ConcurrentHashMap<>(params);
    }
    public static ConcurrentHashMap<String, MessageAcceptor> getAnonymous() {
        return new ConcurrentHashMap<>(anonymous);
    }

}
