package org.jsj.my.config;

import org.jsj.my.auth.V4AuthService;
import org.jsj.my.dingtalk.DingTalkNotify;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Biz configurations
 *
 * @author JSJ
 */
@Configuration
public class BizConfig {

    @Value("${spring.profiles.active:Prod}")
    private String env;

    @Value("${spring.application.name:Asimov}")
    private String app;

    @Value("${dingtalk.hook:https://oapi.dingtalk.com/robot/send?access_token=8217628dbdb966e9eb5e558682c92922d2caf969edcdc7782d1986c6faa7f62a}")
    private String hook;

    @Bean
    public DingTalkNotify dingTalkNotify() {
        DingTalkNotify dingTalkNotify = new DingTalkNotify();
        dingTalkNotify.serverConfig(hook, app, env);
        return dingTalkNotify;
    }

    @Bean
    public V4AuthService v4AuthService() {
        return new V4AuthService();
    }
}
