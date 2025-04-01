package com.eddie.train.member.req;

import com.eddie.train.common.req.PageReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketQueryReq extends PageReq {

    private Long memberId;

}
