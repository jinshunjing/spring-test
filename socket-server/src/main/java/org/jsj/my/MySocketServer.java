package org.jsj.my;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Socket server
 *
 * @author JSJ
 */
@EnableScheduling
@SpringBootApplication
public class MySocketServer {

    public static void main(String[] args) {
        SpringApplication.run(MySocketServer.class, args);
    }

}
