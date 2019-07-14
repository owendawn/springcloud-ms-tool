package com;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 2017/10/19 14:53
 * @author owen pan
 *  server starter
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class ZuulServerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ZuulServerApplication.class).run(args);
    }
}
