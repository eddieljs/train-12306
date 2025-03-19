package com.eddie.train.${module2}.controller.admin;

import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.Result;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.${module2}.req.*;
import com.eddie.train.${module2}.resp.${Domain}QueryResp;
import com.eddie.train.${module2}.service.${Domain}Service;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/${domain}")
public class ${Domain}AdminController {
    @Resource
    private ${Domain}Service ${domain}Service;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody ${Domain}SaveReq req) {
        ${domain}Service.save(req);
        return new Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResp<${Domain}QueryResp>> queryList(@Valid ${Domain}QueryReq req) {

        PageResp<${Domain}QueryResp> list = ${domain}Service.queryList(req);
        return new Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        ${domain}Service.delete(id);
        return new Result.success();
    }

    @GetMapping("/query-mine")
    public Result<List<${Domain}QueryResp>> queryMine() {
        List<${Domain}QueryResp> list = ${domain}Service.queryMine();
        return new Result.success(list);
    }

    @GetMapping("/init")
    public Result<Object> init() {
        ${domain}Service.init();
        return new Result.success();
    }


}