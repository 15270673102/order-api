package com.loushi.vo.task;

import com.loushi.vo.common.PagingVO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BozhuTaskListVO extends PagingVO {

    /**
     * 任务状态
     */
    @NotNull(message = "taskStatus不能为空")
    private Integer taskStatus;

    /**
     * 时间过滤(yyyy-MM-dd)
     */
    @NotNull(message = "filterTime不能为空")
    private String filterTime;

}
