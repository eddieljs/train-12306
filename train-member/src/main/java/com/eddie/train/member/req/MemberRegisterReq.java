package com.eddie.train.member.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterReq {
    @NotBlank(message = "【手机号】不能为空")
    public String mobile;
}
