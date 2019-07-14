package com;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import util.DomXmlUtils;

import java.io.File;
import java.io.IOException;

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
        DomXmlUtils domXmlUtils = new DomXmlUtils(new File(modulePath + "/routes.xml"));
        domXmlUtils.parseXml(new DomXmlUtils.BaseDomXmlParser<Object>() {
            @Override
            protected Object parse(Document doc) {
                Element root = doc.getDocumentElement();
                NodeList routes = root.getElementsByTagName("route");
                for (int i = 0; i < routes.getLength(); i++) {
                    Element route = (Element) routes.item(i);
                    builder.route(r->
                            r.path(route.getAttribute("path"))
                                    .uri(route.getAttribute("uri")
                                    ).id(route.getAttribute("path"))
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
