package org.jsj.my.ops;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * 异步的数据库事务
 *
 * @author JSJ
 */
@Slf4j
public class TxOps {

    /**
     * 执行数据库事务
     *
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void executeDbTx() throws Exception {

    }

}
