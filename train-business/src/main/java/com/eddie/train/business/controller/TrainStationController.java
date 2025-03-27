package com.eddie.train.business.controller;

import com.eddie.train.business.req.TrainStationQueryReq;
import com.eddie.train.business.req.TrainStationSaveReq;
import com.eddie.train.business.resp.StationQueryResp;
import com.eddie.train.business.resp.TrainStationQueryResp;
import com.eddie.train.business.service.TrainStationService;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.resp.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/station")
public class TrainStationController {
    @Resource
    private TrainStationService trainStationService;


    @GetMapping("/query-all")
    public Result<List<StationQueryResp>> queryList() {
        List<StationQueryResp> list = trainStationService.queryAll();
        return Result.success(list);
    }

}