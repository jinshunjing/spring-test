package org.jsj.my.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 异步事件
 *
 * @author JSJ
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TxEvent {
    private String txid;
}
