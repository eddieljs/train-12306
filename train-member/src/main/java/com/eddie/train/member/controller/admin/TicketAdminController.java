package com.eddie.train.member.controller.admin;

import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.Result;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.member.req.*;
import com.eddie.train.member.resp.TicketQueryResp;
import com.eddie.train.member.service.TicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/ticket")
public class TicketAdminController {
    @Resource
    private TicketService ticketService;



    @GetMapping("/query-list")
    public Result<PageResp<TicketQueryResp>> queryList(@Valid TicketQueryReq req) {

        PageResp<TicketQueryResp> list = ticketService.queryList(req);
        return Result.success(list);
    }


    @GetMapping("/query-mine")
    public Result<List<TicketQueryResp>> queryMine() {
        List<TicketQueryResp> list = ticketService.queryMine();
        return Result.success(list);
    }




}