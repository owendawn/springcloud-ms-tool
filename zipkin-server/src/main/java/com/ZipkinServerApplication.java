package com;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import zipkin2.server.internal.EnableZipkinServer;

import javax.servlet.http.HttpServletRequest;

/**
 * 2017/10/19 14:53
 *
 * @author owen pan
 * server starter
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableZipkinServer
@Controller
public class ZipkinServerApplication {

    @GetMapping("/")
    public String redirect(HttpServletRequest request) {
        return "redirect:http://" + request.getRemoteHost() + ":8080";
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ZipkinServerApplication.class).run(args);
    }
}
