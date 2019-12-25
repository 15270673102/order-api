package com.loushi.vo.order;

import com.loushi.vo.common.PagingVO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserOrderVO extends PagingVO {

    /**
     * 状态类型
     */
    @NotNull(message = "state不能为空")
    private Integer state;

}
