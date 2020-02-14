package org.jsj.my.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author JSJ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TxData {
    private Integer id;

    private String txid;

    private Integer vout;

    private String address;

    private BigDecimal amount;

    private Integer state;

    private Long createTime;

    private Long modifyTime;
}
