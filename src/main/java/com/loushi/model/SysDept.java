package com.loushi.model;

import lombok.Data;

import java.util.Date;


@Data
public class SysDept {

    private Integer id;

    private String deptName;

    private Date createTime;

}