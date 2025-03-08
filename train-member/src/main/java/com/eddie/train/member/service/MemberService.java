package com.eddie.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.eddie.train.common.exception.BusinessException;
import com.eddie.train.common.exception.BusinessExceptionEnum;
import com.eddie.train.common.util.SnowUtil;
import com.eddie.train.member.domain.Member;
import com.eddie.train.member.domain.MemberExample;
import com.eddie.train.member.mapper.MemberMapper;
import com.eddie.train.member.req.MemberLoginReq;
import com.eddie.train.member.req.MemberRegisterReq;
import com.eddie.train.member.req.MemberSendCodeReq;
import com.eddie.train.member.resp.MemberLoginResp;
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
        Member member = selectMemberByMobile(mobile);

        if(member != null){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }
        member.setMobile(mobile);
        //机器中心、数据中心
        member.setId(SnowUtil.getSnowflakeNextId());
        memberMapper.insertSelective(member);
        return member.getId();
    }

    public void sendCode(MemberSendCodeReq req){
        String mobile = req.getMobile();
        //查询mobile是否重复
        Member member = selectMemberByMobile(mobile);
        //不存在则插入
        if(member == null){
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


    public MemberLoginResp login(MemberLoginReq req){
        String mobile = req.getMobile();
        String code = req.getCode();
        //查询mobile是否存在
        Member memberDB = selectMemberByMobile(mobile);
        //不存在则插入
        if(memberDB == null){
            Member member = new Member();
            member.setMobile(mobile);
            member.setId(SnowUtil.getSnowflakeNextId());
            memberMapper.insertSelective(member);
            log.info("账号未注册，已进行注册");
        }
        //从数据库中取出
        MemberExample example = new MemberExample();
        example.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(example);
        Member member = list.get(0);
        if (member == null) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }

        if(!"8888".equals(code)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
        }

        return BeanUtil.copyProperties(member, MemberLoginResp.class);
    }

    private Member selectMemberByMobile(String mobile) {
        MemberExample example = new MemberExample();
        example.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(example);
        if(CollUtil.isEmpty(list)){
            return null;
        }else {
            return list.get(0);
        }
    }
}
