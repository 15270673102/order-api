package com.loushi.controller;

import com.github.pagehelper.PageInfo;
import com.loushi.controller.common.BaseController;
import com.loushi.model.UserPlatform;
import com.loushi.model.Users;
import com.loushi.service.IOrderService;
import com.loushi.service.IUserPlatformService;
import com.loushi.service.IUserService;
import com.loushi.util.AjaxResult;
import com.loushi.vo.common.PagingVO;
import com.loushi.vo.order.CommissionListVO;
import com.loushi.vo.order.RemitRecordListVO;
import com.loushi.vo.user.BindDataVO;
import com.loushi.vo.user.CommissionList1VO;
import com.loushi.vo.user.CommissionRecordVO;
import com.loushi.vo.user.SaveUserPlatformDataVO;
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
 * 用户中心
 * @author 技术部
 */

@RestController
@Api(tags = "用户中心")
@RequestMapping(value = "/userCenter")
@Validated
public class UserController extends BaseController {

    @Resource
    private IUserService userService;
    @Resource
    private IUserPlatformService userPlatformService;
    @Resource
    private IOrderService orderService;


    @GetMapping(value = "/userInfo")
    @ApiOperation(value = "用户数据", notes = "用户数据")
    public AjaxResult userInfo() {
        Users userInfo = userService.getUserInfo(getToken());
        return AjaxResult.success(userInfo);
    }


    @GetMapping(value = "/commissionRecord")
    @ApiOperation(value = "首页佣金", notes = "首页佣金")
    public AjaxResult commissionRecord() {
        CommissionRecordVO commissionRecordVO = orderService.commissionRecord(getToken());
        return AjaxResult.success(commissionRecordVO);
    }


    @GetMapping(value = "/commissionList")
    @ApiOperation(value = "佣金列表", notes = "佣金列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", required = true, value = "当前页面", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", required = true, value = "页面大小", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "filterTime", required = true, value = "时间过滤(yyyy-MM)", dataType = "string", paramType = "query")
    })
    public AjaxResult commissionList(@Valid CommissionList1VO vo) {
        PageInfo<CommissionListVO> pageInfo = orderService.commissionList(getToken(), vo.getPageNum(), vo.getPageSize(), vo.getFilterTime());
        return AjaxResult.success(pageInfo);
    }


    @GetMapping(value = "/remitRecordList")
    @ApiOperation(value = "打款记录列表", notes = "打款记录列表")
    public AjaxResult remitRecordList() {
        List<RemitRecordListVO> list = orderService.remitRecordList(getToken());
        return AjaxResult.success(list);
    }


    @PostMapping(value = "/bindData")
    @ApiOperation(value = "绑定资料", notes = "绑定资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickName", required = true, value = "用户昵称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "realName", required = true, value = "真实名字", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "wechatName", required = true, value = "微信号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "zhifuhaoNum", required = true, value = "支付包号", dataType = "string", paramType = "query")
    })
    public AjaxResult bindData(@Valid BindDataVO vo) {
        userService.bindData(getToken(), vo);
        return AjaxResult.success();
    }


    @GetMapping(value = "/userPlatformData")
    @ApiOperation(value = "用户平台资料列表", notes = "用户平台资料列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", required = true, value = "当前页面", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", required = true, value = "页面大小", dataType = "int", paramType = "query")
    })
    public AjaxResult userPlatformData(@Valid PagingVO vo) {
        PageInfo<UserPlatform> pageInfo = userPlatformService.userPlatformData(getToken(), vo.getPageNum(), vo.getPageSize());
        return AjaxResult.success(pageInfo);
    }


    @PostMapping(value = "/saveUserPlatformData")
    @ApiOperation(value = "保存用户平台资料", notes = "保存用户平台资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userPlatformType", required = true, value = "平台类型[1.微博]", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "nickName", required = true, value = "昵称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "UID", required = true, value = "UID", dataType = "string", paramType = "query"),
    })
    public AjaxResult saveUserPlatformData(@Valid SaveUserPlatformDataVO vo) throws Exception {
        userPlatformService.saveUserPlatformData(getToken(), vo);
        return AjaxResult.success();
    }


    @DeleteMapping(value = "/deleteUserPlatformData")
    @ApiOperation(value = "删除用户平台资料", notes = "删除用户平台资料")
    @ApiImplicitParam(name = "userPlatformId", required = true, value = "用户平台资料表id", dataType = "int", paramType = "query")
    public AjaxResult deleteUserPlatformData(@RequestParam Integer userPlatformId) {
        userPlatformService.deleteUserPlatformData(userPlatformId);
        return AjaxResult.success();
    }

}
