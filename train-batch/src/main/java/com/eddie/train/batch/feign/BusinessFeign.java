package com.eddie.train.batch.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "business", url = "127.0.0.1:8080/business")
public interface BusinessFeign {

    @GetMapping("/hello")
    String hello();
}
