package com.eddie.train.member.controller;

import com.eddie.train.common.resp.Result;
import com.eddie.train.member.req.MemberLoginReq;
import com.eddie.train.member.req.MemberRegisterReq;
import com.eddie.train.member.req.MemberSendCodeReq;
import com.eddie.train.member.resp.MemberLoginResp;
import com.eddie.train.member.service.MemberService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    public Result<Long> register(@Valid @RequestBody MemberRegisterReq req) {
        return Result.success(memberService.register(req));
    }

    @PostMapping("/sendCode")
    public Result sendCode(@Valid @RequestBody MemberSendCodeReq req) {
        memberService.sendCode(req);
        return Result.success("验证码发送成功");
    }

    @PostMapping("/login")
    public Result<MemberLoginResp> login(@Valid @RequestBody MemberLoginReq req) {
        MemberLoginResp resp = memberService.login(req);
        return Result.success(resp);
    }
}
