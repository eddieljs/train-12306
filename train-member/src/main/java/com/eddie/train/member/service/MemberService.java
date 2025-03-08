package com.eddie.train.member.service;

import cn.hutool.core.collection.CollUtil;
import com.eddie.train.common.exception.BusinessException;
import com.eddie.train.common.exception.BusinessExceptionEnum;
import com.eddie.train.common.util.SnowUtil;
import com.eddie.train.member.domain.Member;
import com.eddie.train.member.domain.MemberExample;
import com.eddie.train.member.mapper.MemberMapper;
import com.eddie.train.member.req.MemberRegisterReq;
import com.eddie.train.member.req.MemberSendCodeReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
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

    public void sendCode(MemberSendCodeReq req){
        String mobile = req.getMobile();
        //查询mobile是否重复
        MemberExample example = new MemberExample();
        example.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(example);
        //不存在则插入
        if(CollUtil.isEmpty(list)){
            Member member = new Member();
            member.setMobile(mobile);
            //机器中心、数据中心
            member.setId(SnowUtil.getSnowflakeNextId());
            memberMapper.insertSelective(member);
            log.info("账号未注册，已进行注册");
        }else {
            log.info("手机号已注册，直接发送验证码");
        }
        //String code = RandomUtil.randomString(4);
        String code = "8888";
        log.info("短信验证码发送成功:{}", code);
        //保存短信记录表:手机号，短信验证码，有效期，是否已使用，业务类型，发送时间,使用时间
    }
}
