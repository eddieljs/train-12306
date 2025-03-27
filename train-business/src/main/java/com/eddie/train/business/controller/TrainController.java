package com.eddie.train.business.controller;

import com.eddie.train.business.resp.TrainQueryResp;
import com.eddie.train.business.service.TrainSeatService;
import com.eddie.train.business.service.TrainService;
import com.eddie.train.common.resp.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/train")
public class TrainController {
    @Resource
    private TrainService trainService;
    @Resource
    private TrainSeatService trainSeatService;

    @GetMapping("/query-all")
    public Result<List<TrainQueryResp>> queryList() {
        List<TrainQueryResp> list = trainService.queryAll();
        return Result.success(list);
    }
}