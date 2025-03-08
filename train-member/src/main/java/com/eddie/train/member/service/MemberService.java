package com.eddie.train.member.service;

import cn.hutool.core.collection.CollUtil;
import com.eddie.train.common.exception.BusinessException;
import com.eddie.train.common.exception.BusinessExceptionEnum;
import com.eddie.train.common.util.SnowUtil;
import com.eddie.train.member.domain.Member;
import com.eddie.train.member.domain.MemberExample;
import com.eddie.train.member.mapper.MemberMapper;
import com.eddie.train.member.req.MemberRegisterReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Resource
    private MemberMapper memberMapper;


    public int count(){
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    public long register(MemberRegisterReq req){
        String mobile = req.getMobile();
        //查询mobile是否重复
        MemberExample example = new MemberExample();
        example.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(example);

        if(!CollUtil.isEmpty(list)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }
        Member member = new Member();
        member.setMobile(mobile);
        //机器中心、数据中心
        member.setId(SnowUtil.getSnowflakeNextId());
        memberMapper.insertSelective(member);
        return member.getId();
    }
}
