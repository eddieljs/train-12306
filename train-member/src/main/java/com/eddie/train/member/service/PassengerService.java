package com.eddie.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.eddie.train.common.util.SnowUtil;
import com.eddie.train.member.domain.Passenger;
import com.eddie.train.member.mapper.PassengerMapper;
import com.eddie.train.member.req.PassengerSavaReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PassengerService {

    @Resource
    private PassengerMapper passengerMapper;


    public void save(PassengerSavaReq req) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        passenger.setId(SnowUtil.getSnowflakeNextId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        passengerMapper.insert(passenger);
    }
}
