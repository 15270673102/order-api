package com.loushi.vo.order;


import com.loushi.model.UserOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemVO extends UserOrder {

    //订单
    private UserOrder order;

    //private DoTaskItemVO doTaskItem;

}
