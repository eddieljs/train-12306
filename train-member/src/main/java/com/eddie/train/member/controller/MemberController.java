package com.eddie.train.member.controller;

import com.eddie.train.common.resp.Result;
import com.eddie.train.member.req.MemberRegisterReq;
import com.eddie.train.member.req.MemberSendCodeReq;
import com.eddie.train.member.service.MemberService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @GetMapping("/count")
    public Result<Integer> count() {
        return Result.success(memberService.count());
    }

    @PostMapping("/register")
    public Result<Long> register(@Valid MemberRegisterReq req) {
        return Result.success(memberService.register(req));
    }

    @PostMapping("/sendCode")
    public Result sendCode(@Valid MemberSendCodeReq req) {
        memberService.sendCode(req);
        return Result.success("验证码发送成功");
    }
}
