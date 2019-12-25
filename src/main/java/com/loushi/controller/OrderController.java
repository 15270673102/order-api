package com.loushi.controller;

import com.github.pagehelper.PageInfo;
import com.loushi.controller.common.BaseController;
import com.loushi.model.UserAppeal;
import com.loushi.model.UserOrder;
import com.loushi.service.IOrderService;
import com.loushi.util.AjaxResult;
import com.loushi.vo.order.OrderItemVO;
import com.loushi.vo.order.SaveOrderCheckFaildVO;
import com.loushi.vo.order.UserOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 订单中心
 * @author 技术部
 */

@RestController
@Api(tags = "订单中心")
@RequestMapping(value = "/orderCenter")
@Validated
public class OrderController extends BaseController {

    @Resource
    private IOrderService orderService;


    @GetMapping(value = "/userOrder")
    @ApiOperation(value = "我的订单列表", notes = "我的订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", required = true, value = "当前页面", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", required = true, value = "页面大小", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "state", required = true, value = "状态类型", dataType = "int", paramType = "query"),
    })
    public AjaxResult userOrderList(@Valid UserOrderVO vo) {
        PageInfo<UserOrder> pageInfo = orderService.userOrderList(getToken(), vo);
        return AjaxResult.success(pageInfo);
    }


    @GetMapping(value = "/orderItem")
    @ApiOperation(value = "订单详情", notes = "订单详情")
    @ApiImplicitParam(name = "orderId", required = true, value = "订单表id", dataType = "int", paramType = "query")
    public AjaxResult orderItem(@RequestParam Integer orderId) {
        return AjaxResult.success(orderService.orderItem(orderId));
    }


    @GetMapping(value = "/appealList")
    @ApiOperation(value = "订单协商列表", notes = "订单协商列表")
    @ApiImplicitParam(name = "orderId", required = true, value = "订单表id", dataType = "int", paramType = "query")
    public AjaxResult appealList(@RequestParam Integer orderId) {
        List<UserAppeal> userAppeals = orderService.appealList(orderId);
        return AjaxResult.success(userAppeals);
    }


    @PostMapping(value = "/saveOrderCheckFaild")
    @ApiOperation(value = "保存订单失败申述", notes = "保存订单失败申述")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", required = true, value = "订单表id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "appealExplain", required = true, value = "申述说明", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pics", required = true, value = "图片集合(用,隔开)", dataType = "string", paramType = "query"),
    })
    public AjaxResult saveOrderCheckFaild(@Valid SaveOrderCheckFaildVO vo) throws Exception {
        orderService.saveOrderCheckFaild(getToken(), vo);
        return AjaxResult.success();
    }

}
