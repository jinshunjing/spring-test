package org.jsj.my.push;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;

/**
 * JPush
 *
 * @author JSJ
 */
@Slf4j
public class JPushStub {
    private static final String PROD = "prod";

    private String appKey;
    private String secret;

    private String profile;

    private JPushClient pushClient;

    public void serviceConfig(String profile, String appKey, String masterSecret) {
        this.profile = profile;
        pushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
    }

    private void refresh() {
        if (null != pushClient) {
            return;
        }
        pushClient = new JPushClient(secret, appKey, null, ClientConfig.getInstance());
    }

    public JPushClient getPushClient() {
        return pushClient;
    }

    /**
     * 发送push给某个用户，jpush文档： https://docs.jiguang.cn/jpush/server/push/rest_api_v3_push/
     *
     * @param address Asimov地址
     * @param content 内容
     * @param extras  额外数据
     * @return 消息id
     */
    public Optional<String> pushToUser(String address,
                                       String title,
                                       String content,
                                       Map<String, String> extras) {
        if (StringUtils.isEmpty(address) || StringUtils.isEmpty(content)) {
            log.error("push error: {}, {}", address, content);
            return Optional.empty();
        }
        IosAlert iosAlert = IosAlert.newBuilder().setTitleAndBody(title, "", content).build();

        Notification notification = Notification.newBuilder()
                .addPlatformNotification(
                        AndroidNotification.newBuilder()
                                .setAlert(content)
//                                .setAlertType(1)
                                .setTitle(title)
                                .addExtras(extras)
                                .build()
                )
                .addPlatformNotification(
                        IosNotification.newBuilder()
                                .autoBadge()
                                .setAlert(iosAlert)
                                .setSound("default")
                                .addExtras(extras)
                                .build()
                ).build();

        // FIXME: PROD
        boolean isProd = false;
        if (PROD.equalsIgnoreCase(profile)) {
            isProd = true;
        }
        Options options = Options.newBuilder()
                .setApnsProduction(isProd)
                .build();

        PushPayload payload = PushPayload.newBuilder()
                .setAudience(Audience.alias(address))
                .setNotification(notification)
                .setPlatform(Platform.all())
                .setOptions(options)
                .build();

        Optional<String> result = this.push(payload);

        // TODO: IOS分别推送生产环境和测试环境
        if (!isProd) {
            Notification notification1 = Notification.newBuilder()
                    .addPlatformNotification(
                            IosNotification.newBuilder()
                                    .autoBadge()
                                    .setAlert(iosAlert)
                                    .setSound("default")
                                    .addExtras(extras)
                                    .build()
                    ).build();
            Options options1 = Options.newBuilder()
                    .setApnsProduction(true)
                    .build();
            PushPayload payload1 = PushPayload.newBuilder()
                    .setAudience(Audience.alias(address))
                    .setNotification(notification1)
                    .setPlatform(Platform.ios())
                    .setOptions(options1)
                    .build();
            this.push(payload1);
        }

        return result;
    }

    /**
     * 发送
     *
     * @param payload 消息内容
     * @return 消息id
     */
    private Optional<String> push(PushPayload payload) {
        try {
            refresh();
            PushResult result = pushClient.sendPush(payload);
            log.info("push end: {}", result.toString());
            if (!result.isResultOK()) {
                log.error("push error: {}", result);
                return Optional.empty();
            }

            return Optional.of(String.valueOf(result.msg_id));
        } catch (APIConnectionException e) {
            log.error("Connection error, should retry later", e);
        } catch (APIRequestException e) {
            log.error("HTTP Status: {}, Error Code: {}, Error Message:{} ",
                    e.getStatus(),
                    e.getErrorCode(),
                    e.getErrorMessage());
        }
        return Optional.empty();
    }

}
