package com.eddie.train.business.controller;

import com.eddie.train.business.req.ConfirmOrderDoReq;
import com.eddie.train.business.req.ConfirmOrderQueryReq;
import com.eddie.train.business.resp.ConfirmOrderQueryResp;
import com.eddie.train.business.service.ConfirmOrderService;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.resp.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/confirmOrder")
public class ConfirmOrderController {
    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/do")
    public Result<Object> save(@Valid @RequestBody ConfirmOrderDoReq req) {
        confirmOrderService.doConfirm(req);
        return Result.success("新增成功！");
    }





}