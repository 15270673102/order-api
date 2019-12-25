package com.loushi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderCrawlForward implements Serializable {
    private Integer id;

    private Integer taskId;

    private String msgId;

    private Date createdAt;

    private String userId;

    private String screenName;

    private Date createTime;

}