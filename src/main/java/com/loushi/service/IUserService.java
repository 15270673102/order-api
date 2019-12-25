package com.loushi.service;

import com.loushi.model.Users;
import com.loushi.vo.login.*;
import com.loushi.vo.user.BindDataVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户表
 */

public interface IUserService {

    /**
     * 保存用户数据
     * @param openId
     * @param nickName
     * @param request
     * @return
     */
    UserLoginStateVO saveUser(String openId, String nickName, HttpServletRequest request) throws Exception;

    /**
     * 登入
     * @return
     */
    TokenMapVO login(LoginVO vo) throws Exception;

    /**
     * 发验证码
     */
    void sendCode(SendCodeVO vo, boolean isProd) throws Exception;

    /**
     * 注册
     * @param phoneNum
     * @param code
     * @return
     */
    TokenMapVO userRegister(String phoneNum, String code) throws Exception;

    /**
     * 绑定资料
     * @param uid
     */
    void bindData(Integer uid, BindDataVO vo);

    /**
     * 用户数据
     * @param uid
     * @return
     */
    Users getUserInfo(Integer uid);

    /**
     * 博主注册
     * @param vo
     * @return
     */
    TokenMapVO bozhuRegister(BozhuRegisterVO vo) throws Exception;

    /**
     * 检查手机号码是否注册过
     * @param phoneNum
     */
    ChechUserPhoneNumVO chechUserPhoneNum(String phoneNum);

    /**
     * 用户更新密码
     * @param vo
     */
    void userUpdatePassword(UserUpdatePasswordVO vo) throws Exception;

    /**
     * 博主更新密码
     * @param vo
     */
    void bozhuUpdatePassword(BozhuUpdatePasswordVO vo) throws Exception;

    /**
     * 检查博主用户账号是否存在
     * @param usernmae
     * @return
     */
    ChechUserPhoneNumVO chechBozhuUsername(String usernmae);
}
