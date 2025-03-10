package com.eddie.train.member.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerSavaReq {
    private Long id;

    @NotNull(message = "【会员ID】不能为空")
    private Long memberId;

    @NotNull(message = "【名字】不能为空")
    private String name;

    @NotBlank(message = "【身份证号】不能为空")
    private String idCard;

    @NotBlank(message = "【旅客类型】不能为空")
    private String type;

    private Date createTime;

    private Date updateTime;

}