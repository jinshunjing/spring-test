package org.jsj.my.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsj.my.annotation.HttpRetryer;
import org.jsj.my.model.TxData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 微服务的客户端
 *
 * @author JSJ
 */
@Slf4j
public class TxClient {
    public static String SERVICE_URL = "http://my-service/api/v1";

    @Autowired
    private RestTemplate restTemplate;

    public TxData queryAccountByAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            return null;
        }
        TxData bean = restTemplate.getForObject(SERVICE_URL + "/tx/{1}", TxData.class, address);
        return bean;
    }

    public List<TxData> listAccountByAddress(List<String> addrs) {
        if (CollectionUtils.isEmpty(addrs)) {
            return Collections.emptyList();
        }
        TxData[] results = restTemplate.postForObject(SERVICE_URL + "/tx/list", addrs, TxData[].class);
        if (null == results) {
            results = new TxData[0];
        }
        return Arrays.asList(results);
    }

    @HttpRetryer
    public int updateAccountByAddress(TxData bean) {
        if (Objects.isNull(bean)) {
            return 0;
        }
        Integer rows = restTemplate.postForObject(SERVICE_URL + "/tx/update", bean, Integer.class);
        return rows == null ? 0 : rows;
    }

}
