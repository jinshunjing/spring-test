package org.jsj.my.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jsj.my.exception.BaseException;
import org.jsj.my.model.TxDataResponse;
import org.jsj.my.model.TxidRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Tx API
 *
 * @author JSJ
 */
@RestController("TxDataCtrl")
@RequestMapping("/tx/data")
@Slf4j
@Api(description = "0.1.0 API", tags = {"TX"})
public class TxDataController {

    @PostMapping("/query")
    @ApiOperation(value = "转账交易详情",
            notes = "Server returns the tx detail")
    public TxDataResponse detail(@RequestBody TxidRequest req,
                                 HttpServletRequest request) throws BaseException {
        Long uid = (Long) request.getAttribute("uid");
        log.info("[query tx] entrance: {}, {}", req, uid);

        // TODO
        TxDataResponse res = null;

        log.info("[query tx] exit: {}, {}", JSON.toJSONString(res), uid);
        return res;
    }

}
