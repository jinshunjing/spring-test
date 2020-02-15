package org.jsj.my.gov;

import lombok.extern.slf4j.Slf4j;
import org.jsj.my.dingtalk.DingTalkNotify;
import org.jsj.my.redis.RedisLock;
import org.jsj.my.spring.AbstractEventHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 治理的父类
 *
 * @author The flow developers
 */
@Slf4j
public abstract class AbstractGovernor extends AbstractEventHandler {

    @Autowired
    protected DingTalkNotify dingTalkNotify;

    @Autowired
    protected RedisLock redisLock;

}
