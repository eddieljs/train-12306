package com.eddie.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.eddie.train.business.domain.DailyTrainTicket;
import com.eddie.train.business.enums.ConfirmOrderStatusEnum;
import com.eddie.train.business.enums.SeatColEnum;
import com.eddie.train.business.enums.SeatTypeEnum;
import com.eddie.train.business.req.ConfirmOrderTicketReq;
import com.eddie.train.common.context.LoginMemberContext;
import com.eddie.train.common.exception.BusinessException;
import com.eddie.train.common.exception.BusinessExceptionEnum;
import com.eddie.train.common.resp.PageResp;
import com.eddie.train.common.util.SnowUtil;
import com.eddie.train.business.domain.ConfirmOrder;
import com.eddie.train.business.domain.ConfirmOrderExample;
import com.eddie.train.business.mapper.ConfirmOrderMapper;
import com.eddie.train.business.req.ConfirmOrderQueryReq;
import com.eddie.train.business.req.ConfirmOrderDoReq;
import com.eddie.train.business.resp.ConfirmOrderQueryResp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ConfirmOrderService {

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;


    public void save(ConfirmOrderDoReq req) {
        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(req, ConfirmOrder.class);
        if (ObjectUtil.isNull(confirmOrder.getId())) {
            confirmOrder.setId(SnowUtil.getSnowflakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.insert(confirmOrder);
        } else {
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.updateByPrimaryKey(confirmOrder);
        }
    }

    public PageResp<ConfirmOrderQueryResp> queryList(ConfirmOrderQueryReq req){
        //设置查询条件
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        confirmOrderExample.setOrderByClause("id desc");
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();

        //设置分页参数
        PageHelper.startPage(req.getPage(), req.getSize());
        //进行条件查询
        List<ConfirmOrder> confirmOrderList = confirmOrderMapper.selectByExample(confirmOrderExample);
        //将查询结果封装为pageInfo
        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>(confirmOrderList);
        log.info("总条数,{}",pageInfo.getTotal());
        log.info("总页数,{}",pageInfo.getPages());
        //类型转换
        List<ConfirmOrderQueryResp> list = BeanUtil.copyToList(confirmOrderList, ConfirmOrderQueryResp.class);
        PageResp<ConfirmOrderQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public void delete(Long id) {
        confirmOrderMapper.deleteByPrimaryKey(id);
    }

    public List<ConfirmOrderQueryResp> queryMine() {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        confirmOrderExample.setOrderByClause("name asc");
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();

        List<ConfirmOrder> list = confirmOrderMapper.selectByExample(confirmOrderExample);
        return BeanUtil.copyToList(list, ConfirmOrderQueryResp.class);
    }

    public void doConfirm(ConfirmOrderDoReq req) {
        DateTime now = DateTime.now();
        // 省略业务数据校验，如:车次是否存在，余票是否存在，车次是否在有效期内

        // 保存确认订单表，状态初始
        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setId(SnowUtil.getSnowflakeNextId());
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);
        confirmOrder.setMemberId(LoginMemberContext.getId());
        Date date = req.getDate();
        String trainCode = req.getTrainCode();
        String start = req.getStart();
        String end = req.getEnd();
        confirmOrder.setDate(date);
        confirmOrder.setTrainCode(trainCode);
        confirmOrder.setStart(start);
        confirmOrder.setEnd(end);
        confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
        confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
        List<ConfirmOrderTicketReq> tickets = req.getTickets();
        confirmOrder.setTickets(JSON.toJSONString(tickets));

        confirmOrderMapper.insert(confirmOrder);

        // 查出余票记录，需要得到真实的库存
        DailyTrainTicket dailyTrainTicket = dailyTrainTicketService.SelectByUnique(date, trainCode, start, end);
        log.info("查到余票记录：{}", dailyTrainTicket);
        // 扣减余票数量，并判断余票是否足够
        for (ConfirmOrderTicketReq ticketReq : tickets) {
            String seatTypeCode = ticketReq.getSeatTypeCode();
            SeatTypeEnum seatTypeEnum = EnumUtil.getBy(SeatTypeEnum::getCode, seatTypeCode);
            switch (seatTypeEnum) {
                case YDZ -> {
                    int remainingTicketCount = dailyTrainTicket.getYdz() - 1;
                    if (remainingTicketCount < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYdz(remainingTicketCount);
                }
                case EDZ -> {
                    int remainingTicketCount = dailyTrainTicket.getEdz() - 1;
                    if (remainingTicketCount < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYdz(remainingTicketCount);
                }
                case RW -> {
                    int remainingTicketCount = dailyTrainTicket.getRw() - 1;
                    if (remainingTicketCount < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYdz(remainingTicketCount);
                }
                case YW -> {
                    int remainingTicketCount = dailyTrainTicket.getYw() - 1;
                    if (remainingTicketCount < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYdz(remainingTicketCount);
                }
            }
        }
        // 选座
        ConfirmOrderTicketReq ticketReq0 = tickets.get(0);
        if (StrUtil.isNotBlank(ticketReq0.getSeat())) {
            log.info("本次购票有选座");
            // 查出本次选座的座位类型都有哪些列，用于计算所选座位与第一个座位的偏离值
            List<SeatColEnum> colsEnumList = SeatColEnum.getColsByType(ticketReq0.getSeatTypeCode());
            log.info("本次选座的座位类型：{}", colsEnumList);
            // 组成和前端一样的列表，用于做参照的座位列表
            List<String> referSeatList = new ArrayList<>();
            for (int i = 1; i <= 2; i++) {
                for (SeatColEnum seatColEnum : colsEnumList) {
                    referSeatList.add(seatColEnum.getType() + i);
                }
            }
            log.info("用于做参照的两排座位：{}", referSeatList);
            // 获取绝对偏移值
            List<Integer> absoluteOffsetList = new ArrayList<>();
            List<Integer> offsetList = new ArrayList<>();
            for (ConfirmOrderTicketReq ticketReq : tickets) {
                int index = referSeatList.indexOf(ticketReq.getSeat());
                absoluteOffsetList.add(index);
            }
            log.info("计算得到的所有绝对偏移值为:{}", absoluteOffsetList);
            // 计算相对偏移值
            for (Integer index : absoluteOffsetList) {
                int offset = index - absoluteOffsetList.get(0);
                offsetList.add(offset);
            }
            log.info("计算得到的所有绝对偏移值为:{}", offsetList);
        }else {
            log.info("本次购票没有选座");

        }
        // 一个车箱一个车箱的获取座位数据

            // 挑选符合条件的座位，如果这个车箱不满足，则进入下个车箱(多个选座应该在同一个车厢)

        // 选中座位后事务处理:

            // 座位表修改售卖情况sell;

            //余票详情表修改余票;

            // 为会员增加购票记录

            // 更新确认订单为成功
    }
}
