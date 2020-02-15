package org.jsj.my.consumer;

import org.jsj.my.rocket.RocketMessageConsumer;

/**
 * Rocket MQ Consumer
 *
 * @author JSJ
 */
public class TxConsumer extends RocketMessageConsumer {

    public TxConsumer(String profile, String nameServerAddr, String group) {
        super(profile, nameServerAddr, group + "_tx", "tx");
    }

}
