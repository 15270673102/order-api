package com.loushi.model;

import com.loushi.vo.order.DoTaskItemVO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单表
 * @author 技术部
 */

@Data
public class UserOrder implements Serializable {

    private Integer id;
    private String orderNo;
    private Integer userId;

    private Integer taskId;
    private UserTask task;

    private Date startTime;
    private Date submitTime;
    private Date checkTime;

    private Integer orderNum;
    private Double orderPrice;
    private BigDecimal orderTotalPrice;

    private Integer orderStatus;
    private String orderStatusStr;

    private Date createTime;

    //===
    private String failureCause;
    private Date appealTime;

    //====
    private DoTaskItemVO doTaskItemVO;
    private List<UserOrderCheck> userOrderChecks;

}