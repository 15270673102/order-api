package com.loushi.vo.order;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SaveOrderCheckFaildVO {

    @NotNull(message = "orderId不能为空")
    private Integer orderId;

    @NotNull(message = "appealExplain不能为空")
    private String appealExplain;

    /**
     * 图片集合(用,隔开)
     */
    @NotNull(message = "pics不能为空")
    private String pics;
}
