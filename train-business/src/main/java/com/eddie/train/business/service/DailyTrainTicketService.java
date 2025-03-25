package com.eddie.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.util.SnowUtil;
import com.eddie.train.business.domain.DailyTrainTicket;
import com.eddie.train.business.domain.DailyTrainTicketExample;
import com.eddie.train.business.mapper.DailyTrainTicketMapper;
import com.eddie.train.business.req.DailyTrainTicketQueryReq;
import com.eddie.train.business.req.DailyTrainTicketSaveReq;
import com.eddie.train.business.resp.DailyTrainTicketQueryResp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DailyTrainTicketService {

    @Resource
    private DailyTrainTicketMapper dailyTrainTicketMapper;


    public void save(DailyTrainTicketSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(req, DailyTrainTicket.class);
        if (ObjectUtil.isNull(req.getId())) {
            dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainTicket.setCreateTime(now);
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.insert(dailyTrainTicket);
        }else {
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.updateByPrimaryKeySelective(dailyTrainTicket);
        }
    }

    public PageResp<DailyTrainTicketQueryResp> queryList(DailyTrainTicketQueryReq req){
        //设置查询条件
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.setOrderByClause("id desc");
        DailyTrainTicketExample.Criteria criteria = dailyTrainTicketExample.createCriteria();

        //设置分页参数
        PageHelper.startPage(req.getPage(), req.getSize());
        //进行条件查询
        List<DailyTrainTicket> dailyTrainTicketList = dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);
        //将查询结果封装为pageInfo
        PageInfo<DailyTrainTicket> pageInfo = new PageInfo<>(dailyTrainTicketList);
        log.info("总条数,{}",pageInfo.getTotal());
        log.info("总页数,{}",pageInfo.getPages());
        //类型转换
        List<DailyTrainTicketQueryResp> list = BeanUtil.copyToList(dailyTrainTicketList, DailyTrainTicketQueryResp.class);
        PageResp<DailyTrainTicketQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public void delete(Long id) {
        dailyTrainTicketMapper.deleteByPrimaryKey(id);
    }

    public List<DailyTrainTicketQueryResp> queryMine() {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.setOrderByClause("name asc");
        DailyTrainTicketExample.Criteria criteria = dailyTrainTicketExample.createCriteria();

        List<DailyTrainTicket> list = dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);
        return BeanUtil.copyToList(list, DailyTrainTicketQueryResp.class);
    }
}
