package com;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.environment.NativeEnvironmentProperties;

/**
 * 2017/10/19 14:53
 * @author owen pan
 *  server starter
 */

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
@EnableConfigurationProperties(value = NativeEnvironmentProperties.class)
public class ConfigServerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ConfigServerApplication.class).run(args);
    }
}
