package com;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 2019/7/14 16:56
 *
 * @author owen pan
 */
@FeignClient(name = "server-module")
public interface ServerFeign {
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String hi( @RequestParam("serviceId") String serviceId);
}
