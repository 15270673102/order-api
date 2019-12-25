package com.loushi.vo.user;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BindDataVO {

    @NotNull(message = "nickName不能为空")
    private String nickName;

    @NotNull(message = "realName不能为空")
    private String realName;

    @NotNull(message = "wechatName不能为空")
    private String wechatName;

    @NotNull(message = "zhifuhaoNum不能为空")
    private String zhifuhaoNum;

}
