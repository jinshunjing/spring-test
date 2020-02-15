package org.jsj.my.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 转账交易ID
 *
 * @author JSJ
 */
@Data
public class TxidRequest {
    @ApiModelProperty(value = "交易ID")
    private String txid;
}
