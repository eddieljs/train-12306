package com.eddie.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
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

import java.util.List;

@Slf4j
@Service
public class TrainSeatService {

    @Resource
    private TrainSeatMapper trainSeatMapper;


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
}
