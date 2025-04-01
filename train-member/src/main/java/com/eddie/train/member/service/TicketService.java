package com.eddie.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.req.MemberTicketReq;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.util.SnowUtil;
import com.eddie.train.member.domain.Ticket;
import com.eddie.train.member.domain.TicketExample;
import com.eddie.train.member.mapper.TicketMapper;
import com.eddie.train.member.req.TicketQueryReq;
import com.eddie.train.member.req.TicketSaveReq;
import com.eddie.train.member.resp.TicketQueryResp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TicketService {

    @Resource
    private TicketMapper ticketMapper;


    /*public void save(TicketSaveReq req) {
        DateTime now = DateTime.now();
        Ticket ticket = BeanUtil.copyProperties(req, Ticket.class);
        if (ObjectUtil.isNull(req.getId())) {
            ticket.setId(SnowUtil.getSnowflakeNextId());
            ticket.setCreateTime(now);
            ticket.setUpdateTime(now);
            ticketMapper.insert(ticket);
        }else {
            ticket.setUpdateTime(now);
            ticketMapper.updateByPrimaryKeySelective(ticket);
        }
    }*/

    public void save(MemberTicketReq req) {
        DateTime now = DateTime.now();
        Ticket ticket = BeanUtil.copyProperties(req, Ticket.class);
        ticket.setId(SnowUtil.getSnowflakeNextId());
        ticket.setCreateTime(now);
        ticket.setUpdateTime(now);
        ticketMapper.insert(ticket);
    }

    public PageResp<TicketQueryResp> queryList(TicketQueryReq req){
        //设置查询条件
        TicketExample ticketExample = new TicketExample();
        ticketExample.setOrderByClause("id desc");
        TicketExample.Criteria criteria = ticketExample.createCriteria();
        if (ObjectUtil.isNotNull(req.getMemberId())){
            criteria.andMemberIdEqualTo(req.getMemberId());
        }
        //设置分页参数
        PageHelper.startPage(req.getPage(), req.getSize());
        //进行条件查询
        List<Ticket> ticketList = ticketMapper.selectByExample(ticketExample);
        //将查询结果封装为pageInfo
        PageInfo<Ticket> pageInfo = new PageInfo<>(ticketList);
        log.info("总条数,{}",pageInfo.getTotal());
        log.info("总页数,{}",pageInfo.getPages());
        //类型转换
        List<TicketQueryResp> list = BeanUtil.copyToList(ticketList, TicketQueryResp.class);
        PageResp<TicketQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public void delete(Long id) {
        ticketMapper.deleteByPrimaryKey(id);
    }

    public List<TicketQueryResp> queryMine() {
        TicketExample ticketExample = new TicketExample();
        ticketExample.setOrderByClause("name asc");
        TicketExample.Criteria criteria = ticketExample.createCriteria();

        List<Ticket> list = ticketMapper.selectByExample(ticketExample);
        return BeanUtil.copyToList(list, TicketQueryResp.class);
    }
}
