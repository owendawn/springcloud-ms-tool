package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 2017/10/19 14:53
 * @author owen pan
 *  server starter
 */

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ServerApplication {


    @GetMapping("/hi")
    public String hi(@RequestParam(defaultValue = "Anonymous-Service") String serviceId) {
        return String.format("hi %s, from Server-Module",serviceId);
    }


    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerApplication.class).run(args);
    }
}
