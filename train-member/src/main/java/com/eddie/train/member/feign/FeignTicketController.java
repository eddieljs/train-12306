package com.eddie.train.member.feign;

import com.eddie.train.common.req.MemberTicketReq;
import com.eddie.train.common.resp.Result;
import com.eddie.train.member.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign/ticket")
public class FeignTicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/save")
    public Result save(@Valid @RequestBody MemberTicketReq req) {
        ticketService.save(req);
        return Result.success("买票信息保存成功!");
    }
}
