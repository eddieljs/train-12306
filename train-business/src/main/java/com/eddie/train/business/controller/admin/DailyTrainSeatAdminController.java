package com.eddie.train.business.controller.admin;

import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.Result;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.business.req.*;
import com.eddie.train.business.resp.DailyTrainSeatQueryResp;
import com.eddie.train.business.service.DailyTrainSeatService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dailyTrainSeat")
public class DailyTrainSeatAdminController {
    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody DailyTrainSeatSaveReq req) {
        dailyTrainSeatService.save(req);
        return Result.success("新增成功！");
    }

    @GetMapping("/query-list")
    public Result<PageResp<DailyTrainSeatQueryResp>> queryList(@Valid DailyTrainSeatQueryReq req) {

        PageResp<DailyTrainSeatQueryResp> list = dailyTrainSeatService.queryList(req);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        dailyTrainSeatService.delete(id);
        return Result.success("信息删除成功!");
    }

    @GetMapping("/query-mine")
    public Result<List<DailyTrainSeatQueryResp>> queryMine() {
        List<DailyTrainSeatQueryResp> list = dailyTrainSeatService.queryMine();
        return Result.success(list);
    }




}