package com.eddie.train.business.controller.admin;

import com.eddie.train.business.req.StationQueryReq;
import com.eddie.train.business.req.StationSaveReq;
import com.eddie.train.business.resp.StationQueryResp;
import com.eddie.train.business.service.StationService;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.resp.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/station")
public class AdminStationController {

    @Resource
    private StationService stationService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody StationSaveReq req) {
        stationService.save(req);
        return Result.success("新增成功！");
    }

    @GetMapping("/query-list")
    public Result<PageResp<StationQueryResp>> queryList(@Valid StationQueryReq req) {
        PageResp<StationQueryResp> stationList = stationService.queryList(req);
        return Result.success(stationList);
    }

    @DeleteMapping("/delete/{id}")
    public Result login(@Valid @PathVariable Long id) {
        stationService.deleteById(id);
        return Result.success("信息删除成功!");
    }
}
