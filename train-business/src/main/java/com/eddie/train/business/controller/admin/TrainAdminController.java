package com.eddie.train.business.controller.admin;

import com.eddie.train.business.service.TrainSeatService;
import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.Result;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.business.req.*;
import com.eddie.train.business.resp.TrainQueryResp;
import com.eddie.train.business.service.TrainService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/train")
public class TrainAdminController {
    @Resource
    private TrainService trainService;
    @Resource
    private TrainSeatService trainSeatService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody TrainSaveReq req) {
        trainService.save(req);
        return Result.success("新增成功！");
    }

    @GetMapping("/query-list")
    public Result<PageResp<TrainQueryResp>> queryList(@Valid TrainQueryReq req) {

        PageResp<TrainQueryResp> list = trainService.queryList(req);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        trainService.delete(id);
        return Result.success("信息删除成功!");
    }

    @GetMapping("/query-mine")
    public Result<List<TrainQueryResp>> queryMine() {
        List<TrainQueryResp> list = trainService.queryMine();
        return Result.success(list);
    }

    @GetMapping("/query-all")
    public Result<List<TrainQueryResp>> queryList() {
        List<TrainQueryResp> list = trainService.queryAll();
        return Result.success(list);
    }

    @GetMapping("/gen-seat/{trainCode}")
    public Result<Object> genSeat(@PathVariable String trainCode) {
        trainSeatService.genTrainSeat(trainCode);
        return Result.success("生成座位信息成功!");
    }


}