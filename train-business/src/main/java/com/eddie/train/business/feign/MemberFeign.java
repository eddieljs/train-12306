package com.eddie.train.business.feign;

import com.eddie.train.common.req.MemberTicketReq;
import com.eddie.train.common.resp.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
@FeignClient(name = "member", url = "127.0.0.1:8081")
public interface MemberFeign {
    @GetMapping("/member/feign/ticket/save")
    Result save(@RequestBody MemberTicketReq req);
}

