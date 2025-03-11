package com.eddie.train.member.controller;

import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.resp.Result;
import com.eddie.train.member.req.PassengerQueryReq;
import com.eddie.train.member.req.PassengerSavaReq;
import com.eddie.train.member.resp.PassengerQueryResp;
import com.eddie.train.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Resource
    private PassengerService passengerService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody PassengerSavaReq req) {
        passengerService.save(req);
        return Result.success("新增乘客成功！");
    }

    @GetMapping("/query-list")
    public Result<PageResp<PassengerQueryResp>> queryList(@Valid PassengerQueryReq req) {
        //req.setMemberId(LoginMemberContext.getId());
        PageResp<PassengerQueryResp> passengerList = passengerService.queryList(req);
        return Result.success(passengerList);
    }
}
