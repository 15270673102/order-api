package com.loushi.vo.commonPython;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonPythonVO<T> {


    /**
     * data : 数据集合
     * msg : error
     * ok : 500
     */

    private T data;

    private String msg;

    private int ok;

}
