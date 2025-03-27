package com.eddie.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.eddie.train.business.domain.*;
import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.util.SnowUtil;
import com.eddie.train.business.mapper.DailyTrainTicketMapper;
import com.eddie.train.business.req.DailyTrainTicketQueryReq;
import com.eddie.train.business.req.DailyTrainTicketSaveReq;
import com.eddie.train.business.resp.DailyTrainTicketQueryResp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class DailyTrainTicketService {

    @Resource
    private DailyTrainTicketMapper dailyTrainTicketMapper;
    @Resource
    private TrainStationService trainStationService;


    public void save(DailyTrainTicketSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(req, DailyTrainTicket.class);
        if (ObjectUtil.isNull(req.getId())) {
            dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainTicket.setCreateTime(now);
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.insert(dailyTrainTicket);
        } else {
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.updateByPrimaryKeySelective(dailyTrainTicket);
        }
    }

    public PageResp<DailyTrainTicketQueryResp> queryList(DailyTrainTicketQueryReq req) {
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
        log.info("总条数,{}", pageInfo.getTotal());
        log.info("总页数,{}", pageInfo.getPages());
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

    @Transactional
    public void genDaily(Date date, String trainCode) {
        log.info("开始生成【{}】车次【{}】的余票信息", DateUtil.formatDate(date), trainCode);


        // 现删除某日某车次的余票信息
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        dailyTrainTicketMapper.deleteByExample(dailyTrainTicketExample);

        // 查出某车次的所有车站信息
        List<TrainStation> trainStationList = trainStationService.SelectByTrainCode(trainCode);
        if (CollUtil.isEmpty(trainStationList)) {
            log.info("没有车站基础数据 任务结束");
            return;
        }
        DateTime now = DateTime.now();
        for (int i = 0; i < trainStationList.size(); i++) {
            // 得到出发站
            TrainStation trainStationStart = trainStationList.get(i);
            if (trainStationList.size() == 1) {
                // 当车站列表长度为 1 时，以该车站作为出发站和到达站生成余票信息
                TrainStation trainStation = trainStationList.get(0);
                DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();
                dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
                dailyTrainTicket.setDate(date);
                dailyTrainTicket.setTrainCode(trainCode);
                dailyTrainTicket.setStart(trainStation.getName());
                dailyTrainTicket.setStartPinyin(trainStation.getNamePinyin());
                dailyTrainTicket.setStartTime(trainStation.getOutTime());
                dailyTrainTicket.setStartIndex(trainStation.getIndex());
                dailyTrainTicket.setEnd(trainStation.getName());
                dailyTrainTicket.setEndPinyin(trainStation.getNamePinyin());
                dailyTrainTicket.setEndTime(trainStation.getInTime());
                dailyTrainTicket.setEndIndex(trainStation.getIndex());
                dailyTrainTicket.setYdz(0);
                dailyTrainTicket.setYdzPrice(BigDecimal.ZERO);
                dailyTrainTicket.setEdz(0);
                dailyTrainTicket.setEdzPrice(BigDecimal.ZERO);
                dailyTrainTicket.setRw(0);
                dailyTrainTicket.setRwPrice(BigDecimal.ZERO);
                dailyTrainTicket.setYw(0);
                dailyTrainTicket.setYwPrice(BigDecimal.ZERO);
                dailyTrainTicket.setCreateTime(now);
                dailyTrainTicket.setUpdateTime(now);
                // 这里需要添加保存余票信息的代码，假设为 dailyTrainTicketMapper.insert(dailyTrainTicket);
                dailyTrainTicketMapper.insert(dailyTrainTicket);
            } else {
                for (int j = i + 1; j < trainStationList.size(); j++) {
                    TrainStation trainStationEnd = trainStationList.get(j);
                    // 构造余票信息
                    DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();
                    dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
                    dailyTrainTicket.setDate(date);
                    dailyTrainTicket.setTrainCode(trainCode);
                    dailyTrainTicket.setStart(trainStationStart.getName());
                    dailyTrainTicket.setStartPinyin(trainStationStart.getNamePinyin());
                    dailyTrainTicket.setStartTime(trainStationStart.getOutTime());
                    dailyTrainTicket.setStartIndex(trainStationStart.getIndex());
                    dailyTrainTicket.setEnd(trainStationEnd.getName());
                    dailyTrainTicket.setEndPinyin(trainStationEnd.getNamePinyin());
                    dailyTrainTicket.setEndTime(trainStationEnd.getInTime());
                    dailyTrainTicket.setEndIndex(trainStationEnd.getIndex());
                    dailyTrainTicket.setYdz(0);
                    dailyTrainTicket.setYdzPrice(BigDecimal.ZERO);
                    dailyTrainTicket.setEdz(0);
                    dailyTrainTicket.setEdzPrice(BigDecimal.ZERO);
                    dailyTrainTicket.setRw(0);
                    dailyTrainTicket.setRwPrice(BigDecimal.ZERO);
                    dailyTrainTicket.setYw(0);
                    dailyTrainTicket.setYwPrice(BigDecimal.ZERO);
                    dailyTrainTicket.setCreateTime(now);
                    dailyTrainTicket.setUpdateTime(now);
                    // 保存余票信息到数据库
                    dailyTrainTicketMapper.insert(dailyTrainTicket);
                }

            }

            log.info("结束生成【{}】车次【{}】的余票信息", DateUtil.formatDate(date), trainCode);
        }
    }
}
