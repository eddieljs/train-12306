package com.eddie.train.${module}.controller;

import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.CommonResp;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.${module}.req.*;
import com.eddie.train.${module}.resp.${Domain}QueryResp;
import com.eddie.train.${module}.service.${Domain}Service;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${domain}")
public class ${Domain}Controller {
    @Resource
    private ${Domain}Service ${domain}Service;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody ${Domain}SaveReq req) {
        ${domain}Service.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<${Domain}QueryResp>> queryList(@Valid ${Domain}QueryReq req) {

        PageResp<${Domain}QueryResp> list = ${domain}Service.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        ${domain}Service.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-mine")
    public CommonResp<List<${Domain}QueryResp>> queryMine() {
        List<${Domain}QueryResp> list = ${domain}Service.queryMine();
        return new CommonResp<>(list);
    }

    @GetMapping("/init")
    public CommonResp<Object> init() {
        ${domain}Service.init();
        return new CommonResp<>();
    }


}