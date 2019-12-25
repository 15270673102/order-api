package com.loushi.vo.util;


import com.loushi.component.annotation.DateVid;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AgainCrawlVO {

    @NotNull(message = "userName不能为空")
    private String userName;

    private Integer delayMinuteTimes;

    @NotNull(message = "startTime不能为空")
    @DateVid
    private String startTime;

    @NotNull(message = "endTime不能为空")
    @DateVid
    private String endTime;

}
