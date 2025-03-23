package com.eddie.train.business.req;

import com.eddie.train.common.req.PageReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyTrainSeatQueryReq extends PageReq {

    private String trainCode;
}
