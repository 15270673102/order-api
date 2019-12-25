package com.loushi.vo.common;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * 分页VO
 * @author 技术部
 */

@Data
public class PagingVO {

    /**
     * 当前页面pageNum
     */
    @NotNull(message = "当前页面pageNum不能为空")
    @Min(value = 1)
    private Integer pageNum;

    /**
     * 页面大小pageSize
     */
    @NotNull(message = "当前大小pageSize不能为空")
    @Min(value = 1)
    private Integer pageSize;

}
