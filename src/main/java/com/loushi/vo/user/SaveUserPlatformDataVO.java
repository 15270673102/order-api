package com.loushi.vo.user;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class SaveUserPlatformDataVO {

    /**
     * 平台类型[1.微博]
     */
    @NotNull(message = "userPlatformType不能为空")
    private Integer userPlatformType;

    /**
     * 昵称
     */
    @NotNull(message = "nickName不能为空")
    private String nickName;

    /**
     * UID
     */
    @NotNull(message = "UID不能为空")
    @Pattern(regexp = "^[0-9]*$", message = "UID格式不对")
    private String UID;

}
