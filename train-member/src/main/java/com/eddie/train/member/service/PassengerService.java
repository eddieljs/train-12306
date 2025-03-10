package com.eddie.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.util.SnowUtil;
import com.eddie.train.member.domain.Passenger;
import com.eddie.train.member.domain.PassengerExample;
import com.eddie.train.member.mapper.PassengerMapper;
import com.eddie.train.member.req.PassengerQueryReq;
import com.eddie.train.member.req.PassengerSavaReq;
import com.eddie.train.member.resp.PassengerQueryResp;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PassengerService {

    @Resource
    private PassengerMapper passengerMapper;


    public void save(PassengerSavaReq req) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        passenger.setMemberId(LoginMemberContext.getId());
        passenger.setId(SnowUtil.getSnowflakeNextId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        passengerMapper.insert(passenger);
    }

    public List<PassengerQueryResp> queryList(PassengerQueryReq req){
        PassengerExample passengerExample = new PassengerExample();
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        if (ObjectUtil.isNotNull(LoginMemberContext.getId())){
            criteria.andMemberIdEqualTo(LoginMemberContext.getId());
        }
        List<Passenger> passengerList = passengerMapper.selectByExample(passengerExample);
        return BeanUtil.copyToList(passengerList, PassengerQueryResp.class);
    }
}
