package com.loushi;

import cn.hutool.core.lang.Console;
import com.loushi.component.enumeration.order.OrderStatusEnu;
import com.loushi.component.task.UserPlatFormTask;
import com.loushi.mapper.UserOrderCheckMapper;
import com.loushi.mapper.UserPayMapper;
import com.loushi.mapper.UserPlatformMapper;
import com.loushi.mapper.UserTaskMapper;
import com.loushi.model.UserPay;
import com.loushi.service.IUserPlatformService;
import com.loushi.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Resource
    private UserOrderCheckMapper orderCheckMapper;


    @Test
    public void name1() throws Exception {
    }


}
