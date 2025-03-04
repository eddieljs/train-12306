package com.eddie.train.member.req;

import jakarta.validation.constraints.NotBlank;

public class MemberRegisterReq {
    @NotBlank(message = "【手机号】不能为空")
    public String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "MemberRegisterReq{" +
                "mobile='" + mobile + '\'' +
                '}';
    }
}
