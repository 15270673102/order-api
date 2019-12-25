package com.loushi.controller.util;

import com.loushi.model.SysDept;
import com.loushi.service.util.IDeptService;
import com.loushi.util.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 部门
 * @author 技术部
 */

@RestController
@Api(tags = "部门中心")
@RequestMapping("/dept")
@Validated
public class DeptController {

    @Resource
    private IDeptService deptService;


    @GetMapping("/all")
    @ApiOperation(value = "获取部门", notes = "获取部门")
    public AjaxResult all() {
        List<SysDept> list = deptService.all();
        return AjaxResult.success(list);
    }

}
