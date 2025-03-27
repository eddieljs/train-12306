package com.eddie.train.business.req;

import com.eddie.train.common.req.PageReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyTrainTicketQueryReq extends PageReq {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String trainCode;

    private String start;

    private String end;

}
