package com;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.netflix.turbine.TurbineProperties;


/**
 * 2017/10/19 14:53
 * @author owen pan
 *  server starter
 */
@EnableDiscoveryClient
@EnableTurbine
@SpringBootApplication
@EnableHystrixDashboard
@EnableConfigurationProperties(TurbineProperties.class)
public class DashBoardServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DashBoardServerApplication.class).web(WebApplicationType.SERVLET).run(args);
    }
}
