package org.jsj.my.dingtalk;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * DingTalk
 *
 * @author JSJ
 */
@Slf4j
public class DingTalkNotify {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String env;

    private String app;

    private String hook;

    private boolean active;

    private OkHttpClient httpClient;

    public void serverConfig(String hook, String app, String env) {
        this.hook = hook;
        this.app = app;
        this.env = env;

        active = true;
        httpClient = new OkHttpClient();
    }

    public void notify(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        this.notify(stringWriter.toString());
    }

    public void notify(String content) {
        this.push("Exception", content);
    }

    public void info(String content) {
        this.push("Information", content);
    }

    private void push(String title, String content) {
        if (active) {
            String stringBuilder = "【" + title + "】" + " <-- " + "【" + env + "." + app + "】" + ":\n" + content;
            TextMessage textMessage = TextMessage.builder()
                    .text(stringBuilder)
                    .isAtAll(false).build();
            post(hook, textMessage.toJsonString());
        }
    }

    /**
     * 通用的通知
     *
     * @param app
     * @param title
     * @param content
     */
    public void push(String app, String title, String content) {
        String url = hook;
        if (null == url) {
            return;
        }

        String text = "【" + title + "】" + ":\n" + content;

        Map<String, Object> items = new HashMap<>();
        items.put("msgtype", "text");
        Map<String, String> textContent = new HashMap<>();
        textContent.put("content", text);
        items.put("text", textContent);

        post(url, com.alibaba.fastjson.JSON.toJSONString(items));
    }

    private void post(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            httpClient.newCall(request).execute().close();
        } catch (IOException e) {
            log.error("Send to ding talk failed.", e);
        }
    }

}
