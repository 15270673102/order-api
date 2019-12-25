package com.loushi.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.loushi.component.properties.WxAppletProperties;
import com.loushi.controller.common.BaseController;
import com.loushi.service.IUserService;
import com.loushi.util.AjaxResult;
import com.loushi.utils.login.AesCbcUtil;
import com.loushi.utils.login.RequestsUtil;
import com.loushi.vo.login.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 登入注册
 * @author 技术部
 */

@RestController
@Api(tags = "登入注册")
@RequestMapping(value = "/login")
@Validated
public class LoginController extends BaseController {

    @Resource
    private IUserService userService;
    @Resource
    private WxAppletProperties wxAppletProperties;


    @PostMapping(value = "/decodeUserInfo")
    @ApiOperation(value = "解密用户敏感数据", notes = "解密用户敏感数据(小程序调用)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "encryptedData", required = true, value = "明文,加密数据", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "iv", required = true, value = "加密算法的初始向量", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", required = true, value = "code", dataType = "string", paramType = "query"),
    })
    public AjaxResult decodeUserInfo(@Valid DecodeUserInfo vo, HttpServletRequest request) throws Exception {
        //向微信服务器 使用登录凭证 code 获取 session_key 和 openid
        JSONObject decodeUserInfo = RequestsUtil.decodeUserInfo(wxAppletProperties.getAppId(), wxAppletProperties.getAppSecret(), vo.getCode());
        //AES解密(获取用户的基本数据)
        JSONObject userInfoJson = AesCbcUtil.decrypt(vo.getEncryptedData(), decodeUserInfo.getString("session_key"), vo.getIv(), "UTF-8");
        UserLoginStateVO userLoginStateVO = userService.saveUser(userInfoJson.getString("openId"), userInfoJson.getString("nickName"), request);
        return AjaxResult.success(userLoginStateVO);
    }


    @PostMapping(value = "/login")
    @ApiOperation(value = "登入", notes = "登入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "账号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", required = true, value = "密码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "userRole", required = true, value = "身份【1.接单角色 2.博主】", dataType = "int", paramType = "query"),
    })
    public AjaxResult login(@Valid LoginVO vo) throws Exception {
        TokenMapVO map = userService.login(vo);
        return AjaxResult.success(map);
    }


    @GetMapping(value = "/sendCode")
    @ApiOperation(value = "发验证码", notes = "发验证码")
    @ApiImplicitParam(name = "phoneNum", required = true, value = "手机号码", dataType = "string", paramType = "query")
    public AjaxResult sendCode(@Valid SendCodeVO vo) throws Exception {
        userService.sendCode(vo, isProdActive());
        return AjaxResult.success();
    }


    //=================== 更新密码 ====================================

    @GetMapping(value = "/chechUserPhoneNum")
    @ApiOperation(value = "用户检查手机号码是否注册过", notes = "用户检查手机号码是否注册过")
    @ApiImplicitParam(name = "phoneNum", required = true, value = "手机号码", dataType = "string", paramType = "query")
    public AjaxResult chechUserPhoneNum(@RequestParam String phoneNum) {
        ChechUserPhoneNumVO vo = userService.chechUserPhoneNum(phoneNum);
        return AjaxResult.success(vo);
    }


    @PostMapping(value = "/userUpdatePassword")
    @ApiOperation(value = "用户更新密码", notes = "用户更新密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phoneNum", required = true, value = "手机号码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", required = true, value = "验证码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", required = true, value = "密码", dataType = "string", paramType = "query"),
    })
    public AjaxResult userUpdatePassword(@Valid UserUpdatePasswordVO vo) throws Exception {
        userService.userUpdatePassword(vo);
        return AjaxResult.success();
    }


    @GetMapping(value = "/chechBozhuUsername")
    @ApiOperation(value = "检查博主用户账号是否存在", notes = "检查博主用户账号是否存在")
    @ApiImplicitParam(name = "usernmae", required = true, value = "手机号码", dataType = "string", paramType = "query")
    public AjaxResult chechBozhuUsername(@RequestParam String usernmae) {
        ChechUserPhoneNumVO vo = userService.chechBozhuUsername(usernmae);
        return AjaxResult.success(vo);
    }


    @PostMapping(value = "/bozhuUpdatePassword")
    @ApiOperation(value = "博主更新密码", notes = "博主更新密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "账号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "phoneNum", required = true, value = "手机号码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", required = true, value = "验证码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", required = true, value = "密码", dataType = "string", paramType = "query"),
    })
    public AjaxResult bozhuUpdatePassword(@Valid BozhuUpdatePasswordVO vo) throws Exception {
        userService.bozhuUpdatePassword(vo);
        return AjaxResult.success();
    }


    //=========================================== 注册 ===========================

    @PostMapping(value = "/userRegister")
    @ApiOperation(value = "用户注册", notes = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phoneNum", required = true, value = "手机号码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", required = true, value = "验证码", dataType = "string", paramType = "query")
    })
    public AjaxResult userRegister(@Valid UserRegisterVO vo) throws Exception {
        TokenMapVO map = userService.userRegister(vo.getPhoneNum(), vo.getCode());
        return AjaxResult.success(map);
    }


    @PostMapping(value = "/bozhuRegister")
    @ApiOperation(value = "博主注册", notes = "博主注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "用户名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", required = true, value = "密码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "authCode", required = true, value = "授权码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "deptId", required = true, value = "部门ID", dataType = "int", paramType = "query"),
    })
    public AjaxResult bozhuRegister(@Valid BozhuRegisterVO vo) throws Exception {
        TokenMapVO map = userService.bozhuRegister(vo);
        return AjaxResult.success(map);
    }

}
