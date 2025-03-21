package com.eddie.train.business.controller.admin;

import com.eddie.train.business.resp.StationQueryResp;
import com.eddie.train.business.resp.TrainQueryResp;
import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.Result;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.business.req.*;
import com.eddie.train.business.resp.TrainStationQueryResp;
import com.eddie.train.business.service.TrainStationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/trainStation")
public class TrainStationAdminController {
    @Resource
    private TrainStationService trainStationService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody TrainStationSaveReq req) {
        trainStationService.save(req);
        return Result.success("新增成功！");
    }

    @GetMapping("/query-list")
    public Result<PageResp<TrainStationQueryResp>> queryList(@Valid TrainStationQueryReq req) {

        PageResp<TrainStationQueryResp> list = trainStationService.queryList(req);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        trainStationService.delete(id);
        return Result.success("信息删除成功!");
    }

    @GetMapping("/query-mine")
    public Result<List<TrainStationQueryResp>> queryMine() {
        List<TrainStationQueryResp> list = trainStationService.queryMine();
        return Result.success(list);
    }






}