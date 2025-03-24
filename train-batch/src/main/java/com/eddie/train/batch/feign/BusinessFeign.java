package com.eddie.train.batch.feign;

import com.eddie.train.common.resp.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@FeignClient(name = "business", url = "127.0.0.1:8080/business")
//@FeignClient("business")
public interface BusinessFeign {

    @GetMapping("/hello")
    String hello();

    @GetMapping("/admin/dailyTrain/gen-daily/{date}")
    Result genDaily(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date);

}
