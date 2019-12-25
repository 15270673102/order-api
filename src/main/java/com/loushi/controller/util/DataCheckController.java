package com.loushi.controller.util;

import com.loushi.service.weibo.IWeiboCheck;
import com.loushi.util.AjaxResult;
import com.loushi.vo.util.AgainCrawlVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author 技术部
 */

@RestController
@Api(tags = "数据核对")
@RequestMapping("/dataCheck")
@Validated
public class DataCheckController {

    @Resource
    private IWeiboCheck weiboCheck;


    @GetMapping("/againCrawl")
    @ApiOperation(value = "重新爬取数据核对", notes = "重新爬取数据核对")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", required = true, value = "博主登入名字", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "delayMinuteTimes", required = false, value = "延迟时间(任务到期时间 + 延迟时间)[分钟]", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startTime", required = true, value = "开始时间(yyyy-MM-dd)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", required = true, value = "结束时间(yyyy-MM-dd)", dataType = "String", paramType = "query"),
    })
    public AjaxResult againCrawl(@Valid AgainCrawlVO vo) {
        weiboCheck.againCrawl(vo);
        return AjaxResult.success("数据核对完成...");
    }

}
