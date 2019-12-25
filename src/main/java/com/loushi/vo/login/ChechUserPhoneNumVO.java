package com.loushi.vo.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChechUserPhoneNumVO {

    private boolean exist;

    private String msg;

}
