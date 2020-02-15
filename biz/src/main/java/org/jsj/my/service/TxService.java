package org.jsj.my.service;

import lombok.extern.slf4j.Slf4j;
import org.jsj.my.event.TxEvent;
import org.jsj.my.ops.TxOps;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 异步业务逻辑
 *
 * @author JSJ
 */
@Slf4j
public class TxService {
    @Autowired
    private TxOps txOps;

    public void process(TxEvent event) {
        // TODO 业务逻辑

        // 执行数据库事务
        try {
            txOps.executeDbTx();
        } catch (Exception e) {

        }
    }
}
