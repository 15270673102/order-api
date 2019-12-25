package com.loushi.controller;

import com.github.pagehelper.PageInfo;
import com.loushi.controller.common.BaseController;
import com.loushi.model.UserTask;
import com.loushi.service.IOrderService;
import com.loushi.service.ITaskService;
import com.loushi.util.AjaxResult;
import com.loushi.vo.common.PagingVO;
import com.loushi.vo.order.JoinOrderVO;
import com.loushi.vo.task.BozhuTaskListVO;
import com.loushi.vo.task.PublishTaskVO;
import com.loushi.vo.task.TaskJoinOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 任务中心
 * @author 技术部
 */

@RestController
@Api(tags = "任务中心")
@RequestMapping(value = "/taskCenter")
@Validated
public class TaskController extends BaseController {

    @Resource
    private ITaskService taskService;
    @Resource
    private IOrderService orderService;


    @GetMapping(value = "/taskInfo")
    @ApiOperation(value = "任务详情", notes = "任务详情")
    @ApiImplicitParam(name = "taskId", required = true, value = "任务id", dataType = "int", paramType = "query")
    public AjaxResult orderItem(@RequestParam Integer taskId) {
        UserTask task = taskService.orderItem(getToken(), taskId);
        return AjaxResult.success(task);
    }

    //=============================== 用户 ====================================================
    @GetMapping(value = "/task/all")
    @ApiOperation(value = "主页所有任务展示", notes = "主页所有任务展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", required = true, value = "当前页面", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", required = true, value = "页面大小", dataType = "int", paramType = "query")
    })
    public AjaxResult orderCentre(@Valid PagingVO vo) {
        PageInfo<UserTask> pageInfo = taskService.selectTaskAll(getToken(), vo.getPageNum(), vo.getPageSize());
        return AjaxResult.success(pageInfo);
    }


    @PostMapping(value = "/joinOrder")
    @ApiOperation(value = "接单", notes = "接单操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", required = true, value = "任务id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "num", required = true, value = "接单数量", dataType = "int", paramType = "query")
    })
    public AjaxResult joinOrder(@Valid TaskJoinOrderVO vo) throws Exception {
        JoinOrderVO joinOrderVO = orderService.joinOrder(getToken(), vo.getTaskId(), vo.getNum());
        return AjaxResult.success(joinOrderVO);
    }


    //=============================== 博主 ====================================================
    @GetMapping(value = "/bozhuTaskList")
    @ApiOperation(value = "博主任务列表", notes = "博主任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", required = true, value = "当前页面", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", required = true, value = "页面大小", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "filterTime", required = true, value = "时间过滤(yyyy-MM-dd)", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "taskStatus", required = true, value = "任务状态【0. 全部 1.发布中 2.已完成 3.已取消】", dataType = "int", paramType = "query"),
    })
    public AjaxResult bozhuTaskList(@Valid BozhuTaskListVO vo) {
        PageInfo<UserTask> pageInfo = taskService.selectBozhuTaskList(getToken(), vo);
        return AjaxResult.success(pageInfo);
    }


    @PostMapping(value = "/cancelTask")
    @ApiOperation(value = "博主自动取消任务", notes = "博主自动取消任务")
    @ApiImplicitParam(name = "taskId", required = true, value = "任务id", dataType = "int", paramType = "query")
    public AjaxResult cancelTask(@RequestParam Integer taskId) throws Exception {
        taskService.cancelTask(taskId, getToken());
        return AjaxResult.success();
    }


    @PostMapping(value = "/publishTask")
    @ApiOperation(value = "博主发布任务", notes = "博主发布任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platform", required = true, value = "平台类型【1.微博】", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "taskform", required = true, value = "任务类型", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sourceLink", required = true, value = "对应文章链接【电脑、手机、wap 都支持】", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "price", required = true, value = "任务单价", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "totalNum", required = true, value = "任务数量", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "consumeTime", required = true, value = "任务结束时间(多少分钟后结束)", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "desc", required = false, value = "任务描述", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "reserveDay", required = true, value = "任务保留时间", dataType = "int", paramType = "query"),
    })
    public AjaxResult publishTask(@Valid PublishTaskVO vo) throws Exception {
        taskService.publishTask(vo, getToken());
        return AjaxResult.success();
    }

}
