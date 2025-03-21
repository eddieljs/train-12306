package com.eddie.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.eddie.train.business.domain.*;
import com.eddie.train.business.enums.SeatColEnum;
import com.eddie.train.business.mapper.TrainCarriageMapper;
import com.eddie.train.business.req.TrainCarriageQueryReq;
import com.eddie.train.business.req.TrainCarriageSaveReq;
import com.eddie.train.business.resp.TrainCarriageQueryResp;
import com.eddie.train.business.resp.TrainStationQueryResp;
import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.util.SnowUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TrainCarriageService {

    @Resource
    private TrainCarriageMapper trainCarriageMapper;


    public void save(TrainCarriageSaveReq req) {
        DateTime now = DateTime.now();

        //自动计算列数与座位数
        List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(seatColEnums.size());
        req.setSeatCount(req.getRowCount()*req.getColCount());

        TrainCarriage trainCarriage = BeanUtil.copyProperties(req, TrainCarriage.class);
        if (ObjectUtil.isNull(req.getId())) {
            trainCarriage.setId(SnowUtil.getSnowflakeNextId());
            trainCarriage.setCreateTime(now);
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.insert(trainCarriage);
        }else {
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.updateByPrimaryKeySelective(trainCarriage);
        }
    }

    public PageResp<TrainCarriageQueryResp> queryList(TrainCarriageQueryReq req){
        //设置查询条件
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("id desc");
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();

        //设置分页参数
        PageHelper.startPage(req.getPage(), req.getSize());
        //进行条件查询
        List<TrainCarriage> trainCarriageList = trainCarriageMapper.selectByExample(trainCarriageExample);
        //将查询结果封装为pageInfo
        PageInfo<TrainCarriage> pageInfo = new PageInfo<>(trainCarriageList);
        log.info("总条数,{}",pageInfo.getTotal());
        log.info("总页数,{}",pageInfo.getPages());
        //类型转换
        List<TrainCarriageQueryResp> list = BeanUtil.copyToList(trainCarriageList, TrainCarriageQueryResp.class);
        PageResp<TrainCarriageQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public void delete(Long id) {
        trainCarriageMapper.deleteByPrimaryKey(id);
    }

    public List<TrainCarriageQueryResp > queryMine() {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("name asc");
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();

        List<TrainCarriage> list = trainCarriageMapper.selectByExample(trainCarriageExample);
        return BeanUtil.copyToList(list, TrainCarriageQueryResp.class);
    }

        public List<TrainCarriage> selectByTrainCode(String trainCode){
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("`index` desc");
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        List<TrainCarriage> trainCarriages = trainCarriageMapper.selectByExample(trainCarriageExample);
        return trainCarriages;
    }
}
