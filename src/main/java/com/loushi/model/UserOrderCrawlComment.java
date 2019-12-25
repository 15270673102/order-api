package com.loushi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 爬取的微博 评论数据
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderCrawlComment implements Serializable {

    private Integer id;

    private Integer taskId;

    private String msgId;

    private Date publishTime;

    private String text;

    private String userId;

    private String screenName;

    private Date createTime;

}