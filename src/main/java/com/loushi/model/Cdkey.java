package com.loushi.model;


import lombok.Data;

import java.io.Serializable;

/**
 * 激活码
 */

@Data
public class Cdkey implements Serializable {

    private Integer id;

    private String codeNum;

    private Integer codeStatus;
}