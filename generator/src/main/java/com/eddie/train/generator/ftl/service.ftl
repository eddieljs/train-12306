package com.eddie.train.${module2}.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.util.SnowUtil;
import com.eddie.train.${module2}.domain.${Domain};
import com.eddie.train.${module2}.domain.${Domain}Example;
import com.eddie.train.${module2}.mapper.${Domain}Mapper;
import com.eddie.train.${module2}.req.${Domain}QueryReq;
import com.eddie.train.${module2}.req.${Domain}SaveReq;
import com.eddie.train.${module2}.resp.${Domain}QueryResp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ${Domain}Service {

    @Resource
    private ${Domain}Mapper ${domain}Mapper;


    public void save(${Domain}SaveReq req) {
        DateTime now = DateTime.now();
        ${Domain} ${domain} = BeanUtil.copyProperties(req, ${Domain}.class);
        if (ObjectUtil.isNull(req.getId())) {
            ${domain}.setId(SnowUtil.getSnowflakeNextId());
            ${domain}.setCreateTime(now);
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.insert(${domain});
        }else {
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.updateByPrimaryKeySelective(${domain});
        }
    }

    public PageResp<${Domain}QueryResp> queryList(${Domain}QueryReq req){
        //设置查询条件
        ${Domain}Example ${domain}Example = new ${Domain}Example();
        ${domain}Example.setOrderByClause("id desc");
        ${Domain}Example.Criteria criteria = ${domain}Example.createCriteria();

        //设置分页参数
        PageHelper.startPage(req.getPage(), req.getSize());
        //进行条件查询
        List<${Domain}> ${domain}List = ${domain}Mapper.selectByExample(${domain}Example);
        //将查询结果封装为pageInfo
        PageInfo<${Domain}> pageInfo = new PageInfo<>(${domain}List);
        log.info("总条数,{}",pageInfo.getTotal());
        log.info("总页数,{}",pageInfo.getPages());
        //类型转换
        List<${Domain}QueryResp> list = BeanUtil.copyToList(${domain}List, ${Domain}QueryResp.class);
        PageResp<${Domain}QueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public void delete(Long id) {
        ${domain}Mapper.deleteByPrimaryKey(id);
    }

    public List<${Domain}QueryResp> queryMine() {
        ${Domain}Example ${domain}Example = new ${Domain}Example();
        ${domain}Example.setOrderByClause("name asc");
        ${Domain}Example.Criteria criteria = ${domain}Example.createCriteria();

        List<${Domain}> list = ${domain}Mapper.selectByExample(${domain}Example);
        return BeanUtil.copyToList(list, ${Domain}QueryResp.class);
    }
}
