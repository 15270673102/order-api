package com.loushi.service;

import cn.hutool.core.util.RandomUtil;
import com.loushi.component.contants.LoginCons;
import com.loushi.component.enumeration.AuthCodeStatus;
import com.loushi.component.enumeration.user.UserLoginEnu;
import com.loushi.component.enumeration.user.UserRoleEnu;
import com.loushi.component.enumeration.user.UserStatusEnu;
import com.loushi.component.filter.JwtFilter;
import com.loushi.component.properties.JwtProperties;
import com.loushi.component.properties.PhoneCodeProperties;
import com.loushi.component.properties.UserProperties;
import com.loushi.mapper.CdkeyMapper;
import com.loushi.mapper.SysDeptMapper;
import com.loushi.mapper.UsersMapper;
import com.loushi.model.Cdkey;
import com.loushi.model.SysDept;
import com.loushi.model.Users;
import com.loushi.utils.AliDysmsClient;
import com.loushi.utils.RedisUtil;
import com.loushi.utils.jwt.JwtHelper;
import com.loushi.vo.login.*;
import com.loushi.vo.user.BindDataVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 用户表
 * @author 技术部
 */

@Service
@Slf4j
@Transactional(noRollbackFor = Exception.class)
public class UserServiceImpl implements IUserService {

    @Resource
    private UsersMapper userMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private CdkeyMapper cdkeyMapper;
    @Resource
    private AliDysmsClient aliDysmsClient;
    @Resource
    private PhoneCodeProperties phoneCodeProperties;
    @Resource
    private UserProperties userProperties;
    @Resource
    private SysDeptMapper deptMapper;


    //验证码缓存地址
    private String codeKey = LoginCons.phone_verification_code;


    @Override
    public UserLoginStateVO saveUser(String openId, String nickName, HttpServletRequest request) throws Exception {
        //获取请求中的token
        String token = JwtHelper.getRequestToken(request);
        Claims claims = JwtHelper.parseJWT(token, jwtProperties.getBase64Secret());

        //没有登入
        if (claims == null)
            return new UserLoginStateVO(UserLoginEnu.no_login.getValue(), UserLoginEnu.no_login.getName(), openId, null);

        //授权成功
        Users user = userMapper.selectByPrimaryKey(JwtHelper.getClaimsUid(claims));
        if (user == null)
            return new UserLoginStateVO(UserLoginEnu.authorization.getValue(), UserLoginEnu.authorization.getName(), openId, null);

        //已经登入
        TokenMapVO tokenMapVO = login(new LoginVO(user.getPhone(), user.getPassword(), user.getUserRole()));
        if (user.getUserRole() == UserRoleEnu.bozhu.getValue())
            return new UserLoginStateVO(UserLoginEnu.bozhu_yes_login.getValue(), UserLoginEnu.bozhu_yes_login.getName(), null, tokenMapVO);
        else
            return new UserLoginStateVO(UserLoginEnu.user_yes_login.getValue(), UserLoginEnu.user_yes_login.getName(), null, tokenMapVO);
    }


    @Override
    public TokenMapVO login(LoginVO vo) throws Exception {
        Users user = userMapper.selectByUsernamePassword(vo.getUsername(), vo.getPassword(), vo.getUserRole());
        if (user == null)
            throw new Exception("用户名密码错误...");
        if (user.getUserStatus() == UserStatusEnu.lock.getValue())
            throw new Exception(JwtFilter.LoginState.user_lock.getMsg());

        //获取token
        String token = JwtHelper.createJWT(user.getId(), jwtProperties.getExpiresSecond(), jwtProperties.getBase64Secret());
        return new TokenMapVO(token, user.getPhone(), vo.getUserRole());
    }


    @Override
    public ChechUserPhoneNumVO chechUserPhoneNum(String phoneNum) {
        Users user = userMapper.selectByPhone(phoneNum, UserRoleEnu.user.getValue());
        if (user == null)
            return new ChechUserPhoneNumVO(false, "号码未被注册");
        else
            return new ChechUserPhoneNumVO(true, "号码已被注册了");
    }


    @Override
    public void sendCode(SendCodeVO vo, boolean isProd) throws Exception {
        // 获取验证码
        String code = RandomUtil.randomNumbers(4);
        log.info("验证码...【{}】", code);

        //注册发送验证码
        if (isProd) {
            if (!aliDysmsClient.registerSendSms(vo.getPhoneNum(), code))
                throw new Exception("短信发的太频繁了,请稍后...");
        } else {
            log.info("当前不是正式环境，不发短信... 查看日志...");
        }

        //发送验证码成功了，才把验证码放到redis中去
        String codeKey = this.codeKey + vo.getPhoneNum();
        redisUtil.set(codeKey, code, phoneCodeProperties.getExpiresTime());
    }


    /**
     * 检查手机验证码
     */
    private void checkPhoneCode(String phoneNum, String code) throws Exception {
        String codeKey = this.codeKey + phoneNum;

        //判断这个redis中有没有这个人的验证码数据
        if (!redisUtil.hasKey(codeKey))
            throw new Exception("验证码已经过期了... 请重试...");

        String redisCode = (String) redisUtil.get(codeKey);
        if (!redisCode.equals(code))
            throw new Exception("验证码错误... 请重试...");
    }


    @Override
    public void userUpdatePassword(UserUpdatePasswordVO vo) throws Exception {
        Users user = userMapper.selectByPhone(vo.getPhoneNum(), UserRoleEnu.user.getValue());
        if (user == null)
            throw new Exception("当前手机号码还没有注册...");

        //检查手机验证码
        checkPhoneCode(vo.getPhoneNum(), vo.getCode());

        //更新密码
        user.setPassword(vo.getPassword());
        userMapper.updateByPrimaryKeySelective(user);
    }


    @Override
    public void bozhuUpdatePassword(BozhuUpdatePasswordVO vo) throws Exception {
        //检查手机验证码
        checkPhoneCode(vo.getPhoneNum(), vo.getCode());
        Users user = userMapper.selectByPhone(vo.getUsername(), UserRoleEnu.bozhu.getValue());
        if (user == null)
            throw new Exception("当前用户名还未注册...");

        user.setPassword(vo.getPassword());
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public ChechUserPhoneNumVO chechBozhuUsername(String usernmae) {
        Users user = userMapper.selectByPhone(usernmae, UserRoleEnu.bozhu.getValue());
        if (user == null)
            return new ChechUserPhoneNumVO(false, "账号不存在");
        else
            return new ChechUserPhoneNumVO(true, "账号存在");
    }


    @Override
    public TokenMapVO userRegister(String phoneNum, String code) throws Exception {
        //检查手机号码是否注册过
        chechUserPhoneNum(phoneNum);
        //检查手机验证码
        checkPhoneCode(phoneNum, code);

        String password = RandomUtil.randomNumbers(4);
        Users user = new Users();
        user.setPhone(phoneNum);
        user.setPassword(password);
        user.setUserStatus(UserStatusEnu.register.getValue());
        user.setUserRole(UserRoleEnu.user.getValue());
        user.setCreditNum(userProperties.getCreditNum());
        user.setCreateTime(new Date());
        userMapper.insertSelective(user);

        //注册好了,默认登入
        return login(new LoginVO(phoneNum, password, UserRoleEnu.user.getValue()));
    }


    @Override
    public TokenMapVO bozhuRegister(BozhuRegisterVO vo) throws Exception {
        //判断激活码是否有效
        Cdkey cdkey = cdkeyMapper.selectByCode(vo.getAuthCode());
        if (cdkey == null || cdkey.getCodeStatus() == AuthCodeStatus.yet_activate.getValue())
            throw new Exception("授权码无效");

        Users user = userMapper.selectByPhone(vo.getUsername(), UserRoleEnu.bozhu.getValue());
        if (user != null)
            throw new Exception("用户名存在了");


        //保存用户名和密码
        user = new Users();
        user.setPhone(vo.getUsername());
        user.setPassword(vo.getPassword());
        user.setUserStatus(UserStatusEnu.register.getValue());
        user.setUserRole(UserRoleEnu.bozhu.getValue());
        user.setCreditNum(userProperties.getCreditNum());
        user.setDeptId(vo.getDeptId());
        user.setActiveCode(vo.getAuthCode());
        user.setCreateTime(new Date());
        userMapper.insertSelective(user);

        //激活改成不能用了
        cdkey.setCodeStatus(AuthCodeStatus.yet_activate.getValue());
        cdkeyMapper.updateByPrimaryKeySelective(cdkey);

        //注册好了,默认登入
        return login(new LoginVO(vo.getUsername(), vo.getPassword(), UserRoleEnu.bozhu.getValue()));
    }


    @Override
    public void bindData(Integer uid, BindDataVO vo) {
        Users user = userMapper.selectByPrimaryKey(uid);
        user.setNickName(vo.getNickName());
        user.setRealName(vo.getRealName());
        user.setAlipay(vo.getZhifuhaoNum());
        user.setWechat(vo.getWechatName());
        user.setUserStatus(UserStatusEnu.bing.getValue());
        userMapper.updateByPrimaryKeySelective(user);
    }


    @Override
    public Users getUserInfo(Integer uid) {
        Users users = userMapper.selectByPrimaryKey(uid);
        if (null != users.getDeptId()) {
            SysDept sysDept = deptMapper.selectByPrimaryKey(users.getDeptId());
            users.setDeptName(sysDept.getDeptName());
        }
        return users;
    }

}
