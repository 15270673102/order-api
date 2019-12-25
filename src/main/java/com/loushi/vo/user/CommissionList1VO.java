package com.loushi.vo.user;

import com.loushi.vo.common.PagingVO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommissionList1VO extends PagingVO {


    /**
     * 时间过滤(yyyy-MM)
     */
    @NotNull(message = "filterTime不能为空")
    private String filterTime;

}
