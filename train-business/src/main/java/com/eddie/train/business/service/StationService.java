package com.eddie.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.eddie.train.business.domain.Station;
import com.eddie.train.business.domain.StationExample;
import com.eddie.train.business.mapper.StationMapper;
import com.eddie.train.business.req.StationQueryReq;
import com.eddie.train.business.resp.StationQueryResp;
import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.util.SnowUtil;
import com.eddie.train.business.req.StationQueryReq;
import com.eddie.train.business.req.StationSaveReq;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StationService {

    @Resource
    private StationMapper stationMapper;


    public void save(StationSaveReq req) {
        DateTime now = DateTime.now();
        Station station = BeanUtil.copyProperties(req, Station.class);
        if (ObjectUtil.isNull(req.getId())) {
            station.setId(SnowUtil.getSnowflakeNextId());
            station.setCreateTime(now);
            station.setUpdateTime(now);
            stationMapper.insert(station);
        }else {
            station.setUpdateTime(now);
            stationMapper.updateByPrimaryKeySelective(station);
        }
    }

    public PageResp<StationQueryResp> queryList(StationQueryReq req){
        //设置查询条件
        StationExample stationExample = new StationExample();
        stationExample.setOrderByClause("id desc");
        StationExample.Criteria criteria = stationExample.createCriteria();

        //设置分页参数
        PageHelper.startPage(req.getPage(), req.getSize());
        //进行条件查询
        List<Station> stationList = stationMapper.selectByExample(stationExample);
        //将查询结果封装为pageInfo
        PageInfo<Station> pageInfo = new PageInfo<>(stationList);
        log.info("总条数,{}",pageInfo.getTotal());
        log.info("总页数,{}",pageInfo.getPages());
        //类型转换
        List<StationQueryResp> list = BeanUtil.copyToList(stationList, StationQueryResp.class);
        PageResp<StationQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public void deleteById(Long id) {
        stationMapper.deleteByPrimaryKey(id);
    }
}
