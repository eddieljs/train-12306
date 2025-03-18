package com.eddie.train.${module2}.controller;

import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.resp.Result;
import com.eddie.train.${module2}.req.MemberLoginReq;
import com.eddie.train.${module2}.req.${Domain}QueryReq;
import com.eddie.train.${module2}.req.${Domain}SavaReq;
import com.eddie.train.${module2}.resp.MemberLoginResp;
import com.eddie.train.${module2}.resp.${Domain}QueryResp;
import com.eddie.train.${module2}.service.${Domain}Service;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${domain}")
public class ${Domain}Controller {

    @Resource
    private ${Domain}Service ${domain}Service;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody ${Domain}SavaReq req) {
        ${domain}Service.save(req);
        return Result.success("新增成功！");
    }

    @GetMapping("/query-list")
    public Result<PageResp<${Domain}QueryResp>> queryList(@Valid ${Domain}QueryReq req) {
        PageResp<${Domain}QueryResp> ${domain}List = ${domain}Service.queryList(req);
        return Result.success(${domain}List);
    }

    @DeleteMapping("/delete/{id}")
    public Result login(@Valid @PathVariable Long id) {
        ${domain}Service.deleteById(id);
        return Result.success("信息删除成功!");
    }
}
