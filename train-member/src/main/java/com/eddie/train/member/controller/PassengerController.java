package com.eddie.train.member.controller;

import com.eddie.train.common.resp.Result;
import com.eddie.train.member.req.PassengerSavaReq;
import com.eddie.train.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
