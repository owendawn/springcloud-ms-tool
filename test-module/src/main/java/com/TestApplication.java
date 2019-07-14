package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 2017/10/19 14:53
 * @author owen pan
 *  server starter
 */

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableHystrix
@EnableFeignClients
@EnableCircuitBreaker
public class TestApplication {

    @Autowired
    private ServerFeign serverFeign;

    @GetMapping("/http")
    public String simpleHttp() {
        Map<String, Object> params = new HashMap<>();
        params.put("a", "Test-Module");
        return new RestTemplate().getForObject("http://192.168.119.136:12352/hi?serviceId={a}", String.class,params);
    }

    @GetMapping("/feign")
    public String feign() {
        return serverFeign.hi("Test-Module");
    }


    public static void main(String[] args) {
        new SpringApplicationBuilder(TestApplication.class).run(args);
    }
}
