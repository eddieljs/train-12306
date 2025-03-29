package com.eddie.train.business.service;
import com.eddie.train.business.domain.ConfirmOrder;
import com.eddie.train.business.domain.DailyTrainSeat;
import com.eddie.train.business.domain.DailyTrainTicket;
import com.eddie.train.business.enums.ConfirmOrderStatusEnum;
import com.eddie.train.business.mapper.ConfirmOrderMapper;
import com.eddie.train.business.mapper.DailyTrainSeatMapper;
import com.eddie.train.business.req.ConfirmOrderTicketReq;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 选完票之后来更新数据库
 */
@Service
public class AfterConfirmOrderService{
    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;
    /**
     *座位表修改售卖情况sell;
     *余票详情表修改余票;
     *为会员增加购票记录
     *更新确认订单为成功
     */
    @Transactional
   public void afterDoConfirm(List<DailyTrainSeat> finalSeatList) {
        for (DailyTrainSeat dailyTrainSeat : finalSeatList) {
            DailyTrainSeat seatForUpdate = new DailyTrainSeat();
            seatForUpdate.setId(dailyTrainSeat.getId());
            seatForUpdate.setSell(dailyTrainSeat.getSell());
            dailyTrainSeatMapper.updateByPrimaryKeySelective(seatForUpdate);
        }
   }

   

}
