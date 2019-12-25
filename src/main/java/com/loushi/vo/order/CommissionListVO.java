package com.loushi.vo.order;


import com.loushi.model.UserTask;
import lombok.Data;

import java.util.Date;

@Data
public class CommissionListVO {


    private Integer taskId;
    private Date checkTime;
    private Double money;
    private Integer payStatus;

    private UserTask task;
}
