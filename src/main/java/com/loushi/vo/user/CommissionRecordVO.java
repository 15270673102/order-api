package com.loushi.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommissionRecordVO {

    private BigDecimal totalIncome;
    private BigDecimal delayIncome;

    private String msg;
    private String usrename;
    private String dateTime;
}
