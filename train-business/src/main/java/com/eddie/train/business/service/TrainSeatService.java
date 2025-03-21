package com.eddie.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.eddie.train.business.domain.TrainCarriage;
import com.eddie.train.business.enums.SeatColEnum;
import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.util.SnowUtil;
import com.eddie.train.business.domain.TrainSeat;
import com.eddie.train.business.domain.TrainSeatExample;
import com.eddie.train.business.mapper.TrainSeatMapper;
import com.eddie.train.business.req.TrainSeatQueryReq;
import com.eddie.train.business.req.TrainSeatSaveReq;
import com.eddie.train.business.resp.TrainSeatQueryResp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TrainSeatService {

    @Resource
    private TrainSeatMapper trainSeatMapper;
    @Resource
    private TrainCarriageService trainCarriageService;


    public void save(TrainSeatSaveReq req) {
        DateTime now = DateTime.now();
        TrainSeat trainSeat = BeanUtil.copyProperties(req, TrainSeat.class);
        if (ObjectUtil.isNull(req.getId())) {
            trainSeat.setId(SnowUtil.getSnowflakeNextId());
            trainSeat.setCreateTime(now);
            trainSeat.setUpdateTime(now);
            trainSeatMapper.insert(trainSeat);
        }else {
            trainSeat.setUpdateTime(now);
            trainSeatMapper.updateByPrimaryKeySelective(trainSeat);
        }
    }

    public PageResp<TrainSeatQueryResp> queryList(TrainSeatQueryReq req){
        //设置查询条件
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        trainSeatExample.setOrderByClause("id desc");
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        // 检查 trainCodeValue 是否为空，如果不为空则添加查询条件
        if (req.getTrainCode() != null && !req.getTrainCode().isEmpty()) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        //设置分页参数
        PageHelper.startPage(req.getPage(), req.getSize());
        //进行条件查询
        List<TrainSeat> trainSeatList = trainSeatMapper.selectByExample(trainSeatExample);
        //将查询结果封装为pageInfo
        PageInfo<TrainSeat> pageInfo = new PageInfo<>(trainSeatList);
        log.info("总条数,{}",pageInfo.getTotal());
        log.info("总页数,{}",pageInfo.getPages());
        //类型转换
        List<TrainSeatQueryResp> list = BeanUtil.copyToList(trainSeatList, TrainSeatQueryResp.class);
        PageResp<TrainSeatQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public void delete(Long id) {
        trainSeatMapper.deleteByPrimaryKey(id);
    }

    public List<TrainSeatQueryResp> queryMine() {
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        trainSeatExample.setOrderByClause("name asc");
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();

        List<TrainSeat> list = trainSeatMapper.selectByExample(trainSeatExample);
        return BeanUtil.copyToList(list, TrainSeatQueryResp.class);
    }

    @Transactional
    public void genTrainSeat(String trainCode) {
        DateTime now = DateTime.now();
        // 清空当前车次的座位
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        trainSeatMapper.deleteByExample(trainSeatExample);
        // 查找当前车次的所有车厢
        List<TrainCarriage> carriageList = trainCarriageService.selectByTrainCode(trainCode);
        log.info("当前车次下的车厢数目：{}", carriageList.size());
        // 循环生成每个车次的座位
        for (TrainCarriage trainCarriage : carriageList) {
            // 拿到车厢的行数、列数、座位类型
            Integer rowCount = trainCarriage.getRowCount();
            String seatType = trainCarriage.getSeatType();
            int seatIndex = 1;
            // 根据类型筛选所有的列
            List<SeatColEnum> colEnumList = SeatColEnum.getColsByType(seatType);
            log.info("根据类型筛选所有的列：{}", colEnumList);
            // 循环行数
            for (Integer row = 1; row <= rowCount; row++) {
                // 循环列数
                for (SeatColEnum seatColEnum : colEnumList) {
                    // 构造座位数据并保存数据库
                    TrainSeat trainSeat = new TrainSeat();
                    trainSeat.setId(SnowUtil.getSnowflakeNextId());
                    trainSeat.setTrainCode(trainCode);
                    trainSeat.setCarriageIndex(trainCarriage.getIndex());
                    //为行数填充0 保证所有行数都为两位数
                    trainSeat.setRow(StrUtil.fillBefore(String.valueOf(row),'0', 2));
                    trainSeat.setCol(seatColEnum.getCode());
                    trainSeat.setSeatType(seatType);
                    trainSeat.setCarriageSeatIndex(seatIndex++);
                    trainSeat.setCreateTime(now);
                    trainSeat.setUpdateTime(now);
                    log.info("插入座位信息：{}", trainSeat);
                    trainSeatMapper.insert(trainSeat);
                }

            }

        }
    }
}
