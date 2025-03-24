package com.eddie.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.eddie.train.business.domain.*;
import com.eddie.train.business.mapper.TrainMapper;
import com.eddie.train.business.req.TrainQueryReq;
import com.eddie.train.business.req.TrainSaveReq;
import com.eddie.train.business.resp.TrainQueryResp;
import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.exception.BusinessException;
import com.eddie.train.common.exception.BusinessExceptionEnum;
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
public class TrainService {

    @Resource
    private TrainMapper trainMapper;



    public void save(TrainSaveReq req) {
        DateTime now = DateTime.now();
        Train train = BeanUtil.copyProperties(req, Train.class);
        if (ObjectUtil.isNull(req.getId())) {
            Train trainDB = SelectByUnique(req.getCode());
            if (ObjectUtil.isNotEmpty(trainDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_CODE_UNIQUE_ERROR);
            }
            train.setId(SnowUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            trainMapper.insert(train);
        }else {
            train.setUpdateTime(now);
            trainMapper.updateByPrimaryKeySelective(train);
        }
    }

    private Train SelectByUnique(String code) {
        TrainExample trainExample = new TrainExample();
        trainExample.createCriteria()
                .andCodeEqualTo(code);
        List<Train> stationList = trainMapper.selectByExample(trainExample);
        if (CollUtil.isNotEmpty(stationList)) {
            return stationList.get(0);
        }
        return null;
    }

    public PageResp<TrainQueryResp> queryList(TrainQueryReq req){
        //设置查询条件
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("id desc");
        TrainExample.Criteria criteria = trainExample.createCriteria();

        //设置分页参数
        PageHelper.startPage(req.getPage(), req.getSize());
        //进行条件查询
        List<Train> trainList = trainMapper.selectByExample(trainExample);
        //将查询结果封装为pageInfo
        PageInfo<Train> pageInfo = new PageInfo<>(trainList);
        log.info("总条数,{}",pageInfo.getTotal());
        log.info("总页数,{}",pageInfo.getPages());
        //类型转换
        List<TrainQueryResp> list = BeanUtil.copyToList(trainList, TrainQueryResp.class);
        PageResp<TrainQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public void delete(Long id) {
        trainMapper.deleteByPrimaryKey(id);
    }

    public List<TrainQueryResp> queryMine() {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("name asc");
        TrainExample.Criteria criteria = trainExample.createCriteria();

        List<Train> list = trainMapper.selectByExample(trainExample);
        return BeanUtil.copyToList(list, TrainQueryResp.class);
    }

    public List<TrainQueryResp> queryAll(){
        List<Train> list = selectAll();
        return BeanUtil.copyToList(list, TrainQueryResp.class);
    }

    public List<Train> selectAll() {
        //设置查询条件
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("code desc");
        //进行条件查询
        List<Train> trainList = trainMapper.selectByExample(trainExample);
        return trainList;
    }
}
