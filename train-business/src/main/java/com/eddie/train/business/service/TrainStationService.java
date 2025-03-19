package com.eddie.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.eddie.train.business.domain.Train;
import com.eddie.train.business.domain.TrainExample;
import com.eddie.train.business.domain.TrainStation;
import com.eddie.train.business.domain.TrainStationExample;
import com.eddie.train.business.mapper.TrainStationMapper;
import com.eddie.train.business.req.TrainStationQueryReq;
import com.eddie.train.business.req.TrainStationSaveReq;
import com.eddie.train.business.resp.TrainQueryResp;
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
public class TrainStationService {

    @Resource
    private TrainStationMapper trainStationMapper;


    public void save(TrainStationSaveReq req) {
        DateTime now = DateTime.now();
        TrainStation trainStation = BeanUtil.copyProperties(req, TrainStation.class);
        if (ObjectUtil.isNull(req.getId())) {
            trainStation.setId(SnowUtil.getSnowflakeNextId());
            trainStation.setCreateTime(now);
            trainStation.setUpdateTime(now);
            trainStationMapper.insert(trainStation);
        }else {
            trainStation.setUpdateTime(now);
            trainStationMapper.updateByPrimaryKeySelective(trainStation);
        }
    }

    public PageResp<TrainStationQueryResp> queryList(TrainStationQueryReq req){
        //设置查询条件
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.setOrderByClause("id desc");
        TrainStationExample.Criteria criteria = trainStationExample.createCriteria();

        //设置分页参数
        PageHelper.startPage(req.getPage(), req.getSize());
        //进行条件查询
        List<TrainStation> trainStationList = trainStationMapper.selectByExample(trainStationExample);
        //将查询结果封装为pageInfo
        PageInfo<TrainStation> pageInfo = new PageInfo<>(trainStationList);
        log.info("总条数,{}",pageInfo.getTotal());
        log.info("总页数,{}",pageInfo.getPages());
        //类型转换
        List<TrainStationQueryResp> list = BeanUtil.copyToList(trainStationList, TrainStationQueryResp.class);
        PageResp<TrainStationQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public void delete(Long id) {
        trainStationMapper.deleteByPrimaryKey(id);
    }

    public List<TrainStationQueryResp> queryMine() {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.setOrderByClause("name asc");
        TrainStationExample.Criteria criteria = trainStationExample.createCriteria();

        List<TrainStation> list = trainStationMapper.selectByExample(trainStationExample);
        return BeanUtil.copyToList(list, TrainStationQueryResp.class);
    }
}
