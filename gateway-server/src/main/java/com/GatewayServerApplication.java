package com;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import util.DomXmlUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

/**
 * 2017/10/19 14:53
 *
 * @author owen pan
 * server starter
 */

@SpringBootApplication
public class GatewayServerApplication {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder builder = routeLocatorBuilder.routes();
        getRoutesFromXml(builder);
        return builder.build();
    }

    private void getRoutesFromXml(RouteLocatorBuilder.Builder builder) {
        String modulePath = null;
        try {
            modulePath = new File("").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file=new File(modulePath + "/routes.xml");
        System.out.println("默认加载routes.xml路径："+file.getAbsolutePath());
        if(!file.exists()){
            String jarPath= System.getProperty("java.class.path").replaceAll("\\\\","/");
//            System.out.println("jar路径："+jarPath);
            String jarNearPath=jarPath.substring(0,jarPath.lastIndexOf("/"))+"/routes.xml";
            System.out.println("默认routes.xml文件不存在，加载jar同路径下文件："+jarNearPath);
            file=  new File(jarNearPath);
        }
        DomXmlUtils domXmlUtils = new DomXmlUtils(file);
        domXmlUtils.parseXml(new DomXmlUtils.BaseDomXmlParser<Object>() {
            @Override
            protected Object parse(Document doc) {
                Element root = doc.getDocumentElement();
                NodeList routes = root.getElementsByTagName("route");
                for (int i = 0; i < routes.getLength(); i++) {
                    Element route = (Element) routes.item(i);
                    builder.route(r -> r.path(route.getAttribute("path"))
                                    .filters (f -> f.stripPrefix (1))
                                    .uri(route.getAttribute("uri"))
                                    .id(UUID.randomUUID().toString())
                    );
                }
                return null;
            }
        });
    }


    public static void main(String[] args) {
        new SpringApplicationBuilder(GatewayServerApplication.class).run(args);
    }
}
