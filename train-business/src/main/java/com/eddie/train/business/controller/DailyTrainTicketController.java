package com.eddie.train.business.controller;

import com.eddie.train.business.req.DailyTrainTicketQueryReq;
import com.eddie.train.business.req.DailyTrainTicketSaveReq;
import com.eddie.train.business.resp.DailyTrainTicketQueryResp;
import com.eddie.train.business.service.DailyTrainTicketService;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.resp.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dailyTrainTicket")
public class DailyTrainTicketController {
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody DailyTrainTicketSaveReq req) {
        dailyTrainTicketService.save(req);
        return Result.success("新增成功！");
    }

    @GetMapping("/query-list")
    public Result<PageResp<DailyTrainTicketQueryResp>> queryList(@Valid DailyTrainTicketQueryReq req) {

        PageResp<DailyTrainTicketQueryResp> list = dailyTrainTicketService.queryList(req);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        dailyTrainTicketService.delete(id);
        return Result.success("信息删除成功!");
    }

    @GetMapping("/query-mine")
    public Result<List<DailyTrainTicketQueryResp>> queryMine() {
        List<DailyTrainTicketQueryResp> list = dailyTrainTicketService.queryMine();
        return Result.success(list);
    }




}