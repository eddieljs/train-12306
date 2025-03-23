package com.eddie.train.business.controller.admin;

import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.Result;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.business.req.*;
import com.eddie.train.business.resp.DailyTrainStationQueryResp;
import com.eddie.train.business.service.DailyTrainStationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dailyTrainStation")
public class DailyTrainStationAdminController {
    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody DailyTrainStationSaveReq req) {
        dailyTrainStationService.save(req);
        return Result.success("新增成功！");
    }

    @GetMapping("/query-list")
    public Result<PageResp<DailyTrainStationQueryResp>> queryList(@Valid DailyTrainStationQueryReq req) {

        PageResp<DailyTrainStationQueryResp> list = dailyTrainStationService.queryList(req);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        dailyTrainStationService.delete(id);
        return Result.success("信息删除成功!");
    }

    @GetMapping("/query-mine")
    public Result<List<DailyTrainStationQueryResp>> queryMine() {
        List<DailyTrainStationQueryResp> list = dailyTrainStationService.queryMine();
        return Result.success(list);
    }




}