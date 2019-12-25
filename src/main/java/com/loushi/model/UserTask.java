package com.loushi.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 任务表
 */

@Data
public class UserTask implements Serializable {

    private Integer id;

    private String taskDesc;

    private String taskNo;

    private String title;

    private String taskform;
    private String taskformStr;

    private String sourceLink;

    private Double price;

    private Integer userId;


    //总数量
    private Integer totalNum;
    //剩余数量
    private Integer remainNum;
    //完成数量
    private Integer finishNum;


    //上架到下架的时间间隔
    private Integer consumeTime;
    //剩余时间
    private Long remainTime;
    //保留时间(天)
    private Integer reserveDay;


    //平台类型
    private Integer platform;
    private String platformStr;

    //表示这个人能不能接单类型
    private Integer taskType;


    private Integer taskStatus;

    //发布时间
    private Date publishTime;

    private Date cancelTime;

    private Date createTime;

    //最多接单数量
    private Integer joinNum;

}