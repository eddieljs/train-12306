package com.eddie.train.business.controller.admin;

import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.Result;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.business.req.*;
import com.eddie.train.business.resp.DailyTrainTicketQueryResp;
import com.eddie.train.business.service.DailyTrainTicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dailyTrainTicket")
public class DailyTrainTicketAdminController {
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