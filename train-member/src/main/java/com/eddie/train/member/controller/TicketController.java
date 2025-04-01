package com.eddie.train.member.controller;


import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.resp.Result;
import com.eddie.train.member.req.TicketQueryReq;
import com.eddie.train.member.resp.TicketQueryResp;
import com.eddie.train.member.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/query-list")
    public Result<PageResp<TicketQueryResp>> query(@Valid TicketQueryReq req) {
        req.setMemberId(LoginMemberContext.getId());
        PageResp<TicketQueryResp> pageResp = ticketService.queryList(req);
        return Result.success(pageResp);
    }

}
