package com.eddie.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.eddie.train.business.domain.*;
import com.eddie.train.business.enums.SeatTypeEnum;
import com.eddie.train.business.enums.TrainTypeEnum;
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
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class DailyTrainTicketService {

    @Resource
    private DailyTrainTicketMapper dailyTrainTicketMapper;
    @Resource
    private TrainStationService trainStationService;
    @Resource
    private DailyTrainSeatService dailyTrainSeatService;


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
        if(ObjectUtil.isNotNull(req.getDate())){
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }
        if (ObjUtil.isNotEmpty(req.getStart())) {
            criteria.andStartEqualTo(req.getStart());
        }
        if (ObjUtil.isNotEmpty(req.getEnd())) {
            criteria.andEndEqualTo(req.getEnd());
        }

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
    public void genDaily(DailyTrain dailyTrain, Date date, String trainCode) {
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
            BigDecimal sumKM = BigDecimal.ZERO;
            for (int j = (i + 1); j < trainStationList.size(); j++) {
                TrainStation trainStationEnd = trainStationList.get(j);
                sumKM = sumKM.add(trainStationEnd.getKm());

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
                int ydz = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.YDZ.getCode());
                int edz = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.EDZ.getCode());
                int rw = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.RW.getCode());
                int yw = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.YW.getCode());
                // 票价 = 里程之和 * 座位单价 * 车次类型系数
                String trainType = dailyTrain.getType();
                // 计算票价系数：TrainTypeEnum.priceRate
                // getFieldBy (需要哪一列， 条件， 条件的值)
                BigDecimal priceRate = EnumUtil.getFieldBy(TrainTypeEnum::getPriceRate, TrainTypeEnum::getCode, trainType);
                BigDecimal ydzPrice = sumKM.multiply(SeatTypeEnum.YDZ.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal edzPrice = sumKM.multiply(SeatTypeEnum.EDZ.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal rwPrice = sumKM.multiply(SeatTypeEnum.RW.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ywPrice = sumKM.multiply(SeatTypeEnum.YW.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                dailyTrainTicket.setYdz(ydz);
                dailyTrainTicket.setYdzPrice(ydzPrice);
                dailyTrainTicket.setEdz(edz);
                dailyTrainTicket.setEdzPrice(edzPrice);
                dailyTrainTicket.setRw(rw);
                dailyTrainTicket.setRwPrice(rwPrice);
                dailyTrainTicket.setYw(yw);
                dailyTrainTicket.setYwPrice(ywPrice);
                dailyTrainTicket.setCreateTime(now);
                dailyTrainTicket.setUpdateTime(now);
                dailyTrainTicketMapper.insert(dailyTrainTicket);
            }
        }

        log.info("结束生成【{}】车次【{}】的余票信息", DateUtil.formatDate(date), trainCode);
    }

    public DailyTrainTicket SelectByUnique(Date date, String trainCode, String start, String end) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode)
                .andStartEqualTo(start)
                .andEndEqualTo(end);
        List<DailyTrainTicket> stationList = dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);
        if (CollUtil.isNotEmpty(stationList)) {
            return stationList.get(0);
        }
        return null;
    }
}
