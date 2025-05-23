package com.eddie.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.eddie.train.business.domain.Train;
import com.eddie.train.business.resp.TrainQueryResp;
import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.util.SnowUtil;
import com.eddie.train.business.domain.DailyTrain;
import com.eddie.train.business.domain.DailyTrainExample;
import com.eddie.train.business.mapper.DailyTrainMapper;
import com.eddie.train.business.req.DailyTrainQueryReq;
import com.eddie.train.business.req.DailyTrainSaveReq;
import com.eddie.train.business.resp.DailyTrainQueryResp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class DailyTrainService {

    @Resource
    private DailyTrainMapper dailyTrainMapper;
    @Resource
    private TrainService trainService;
    @Resource
    private DailyTrainStationService dailyTrainStationService;
    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;
    @Resource
    private DailyTrainSeatService dailyTrainSeatService;
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;


    public void save(DailyTrainSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(req, DailyTrain.class);
        if (ObjectUtil.isNull(req.getId())) {
            dailyTrain.setId(SnowUtil.getSnowflakeNextId());
            dailyTrain.setCreateTime(now);
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.insert(dailyTrain);
        }else {
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.updateByPrimaryKeySelective(dailyTrain);
        }
    }

    public PageResp<DailyTrainQueryResp> queryList(DailyTrainQueryReq req){
        //设置查询条件
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.setOrderByClause("date desc, code asc");
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();
        if (ObjectUtil.isNotEmpty(req.getCode())){
            criteria.andCodeEqualTo(req.getCode());
        }
        if (ObjectUtil.isNotNull(req.getDate())){
            criteria.andDateEqualTo(req.getDate());
        }
        //设置分页参数
        PageHelper.startPage(req.getPage(), req.getSize());
        //进行条件查询
        List<DailyTrain> dailyTrainList = dailyTrainMapper.selectByExample(dailyTrainExample);
        //将查询结果封装为pageInfo
        PageInfo<DailyTrain> pageInfo = new PageInfo<>(dailyTrainList);
        log.info("总条数,{}",pageInfo.getTotal());
        log.info("总页数,{}",pageInfo.getPages());
        //类型转换
        List<DailyTrainQueryResp> list = BeanUtil.copyToList(dailyTrainList, DailyTrainQueryResp.class);
        PageResp<DailyTrainQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public void delete(Long id) {
        dailyTrainMapper.deleteByPrimaryKey(id);
    }

    public List<DailyTrainQueryResp> queryMine() {
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.setOrderByClause("name asc");
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();

        List<DailyTrain> list = dailyTrainMapper.selectByExample(dailyTrainExample);
        return BeanUtil.copyToList(list, DailyTrainQueryResp.class);
    }

    /**
     * 生成某日所有车次信息
     * @param date
     */
    public void genDaily(Date date) {
        List<Train> trainList = trainService.selectAll();
        if (CollUtil.isEmpty(trainList)) {
            log.info("没有车次基础数据 任务结束");
            return;
        }
        for (Train train : trainList) {
            genDailyTrain(date, train);
        }
    }

    @Transactional
    public void genDailyTrain(Date date, Train train) {
        log.info("开始生成【{}】车次【{}】的信息", DateUtil.formatDate(date),train.getCode());
        Date now = DateTime.now();
        // 删除原有数据
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.createCriteria()
                .andDateEqualTo(date)
                .andCodeEqualTo(train.getCode());
        dailyTrainMapper.deleteByExample(dailyTrainExample);
        // 生成数据
        DailyTrain dailyTrain = BeanUtil.copyProperties(train, DailyTrain.class);
        dailyTrain.setId(SnowUtil.getSnowflakeNextId());
        dailyTrain.setCreateTime(now);
        dailyTrain.setUpdateTime(now);
        dailyTrain.setDate(date);
        dailyTrainMapper.insert(dailyTrain);
        //生成该车次的车站信息
        dailyTrainStationService.genDaily(date, train.getCode());
        //生成车厢信息
        dailyTrainCarriageService.genDaily(date, train.getCode());
        //生成座位信息
        dailyTrainSeatService.genDaily(date, train.getCode());
        // 生成余票数据
        dailyTrainTicketService.genDaily(dailyTrain, date, train.getCode());
        log.info("结束生成【{}】车次【{}】的信息",DateUtil.formatDate(date),train.getCode());

    }
}
