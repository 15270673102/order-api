package com.loushi.controller.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.loushi.component.enumeration.task.TaskPlatformEnu;
import com.loushi.component.enumeration.task.TaskPubConsumeTimeEnu;
import com.loushi.component.enumeration.task.TaskPubEnu;
import com.loushi.component.enumeration.task.TaskReserveEnu;
import com.loushi.component.properties.BasisProperties;
import com.loushi.controller.common.BaseController;
import com.loushi.util.AjaxResult;
import com.loushi.utils.sts.OssFileUtil;
import com.loushi.utils.sts.StsUtil;
import com.loushi.vo.util.EnuKeyValueModel;
import com.loushi.vo.util.UploadPicVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 工具类controller
 * @author 技术部
 */

@RestController
@Api(tags = "工具类")
@Slf4j
@RequestMapping(value = "/utils")
public class UtilsController extends BaseController {

    @Resource
    private OssFileUtil ossFileUtil;
    @Resource
    private StsUtil stsUtil;


    @GetMapping(value = "/getStsToken")
    @ApiOperation(value = "获取阿里云图片上传所需的sts参数", notes = "获取阿里云图片上传所需的sts参数")
    public AjaxResult getStsServiceInfo() throws Exception {
        HashMap<String, Object> map = stsUtil.getStsToken();
        return AjaxResult.success(map);
    }


    @PostMapping(value = "/uploadPic")
    @ApiOperation(value = "图片上传", notes = "图片上传")
    public AjaxResult uploadPic(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = StrUtil.format("image/order_api_{}.jpg", IdUtil.simpleUUID());
            ossFileUtil.uploadFileObjectByStream(fileName, file.getInputStream());
            return AjaxResult.success("上传成功", fileName);

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error500("上传图片太大了...");
        }
    }


    @GetMapping(value = "/getPublishTaskReserve")
    @ApiOperation(value = "获取发任务保留时间", notes = "获取发任务保留时间")
    public AjaxResult getPublishTaskReserve() {
        return AjaxResult.success(TaskReserveEnu.getReserveEnu());
    }


    @GetMapping(value = "/getPublishTaskConsumeTime")
    @ApiOperation(value = "获取发任务过期时间", notes = "获取发任务过期时间")
    public AjaxResult getPublishTaskConsumeTime() {
        List<EnuKeyValueModel> list = TaskPubConsumeTimeEnu.getTaskPubConsumeTimeEnu(isProdActive());
        return AjaxResult.success(list);
    }


    @GetMapping(value = "/getTaskPlatform")
    @ApiOperation(value = "获取任务发布的平台", notes = "获取任务发布的平台")
    public AjaxResult getTaskPlatform() {
        return AjaxResult.success(TaskPlatformEnu.getTaskPlatformEnu());
    }


    @GetMapping(value = "/getTaskPbu")
    @ApiOperation(value = "获取任务类型", notes = "获取任务类型")
    public AjaxResult getTaskPbu() {
        return AjaxResult.success(TaskPubEnu.getTaskPbuEnu());
    }

}
