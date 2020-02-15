package org.jsj.my;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * MicroService
 *
 * @author JSJ
 */
@SpringBootApplication(scanBasePackages = {"org.jsj.my"})
@EnableDiscoveryClient
@EnableScheduling
public class MicroService {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MicroService.class, args);
    }
}
