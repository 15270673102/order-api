package com.loushi.component.filter;

import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.loushi.component.contants.JwtCons;
import com.loushi.component.enumeration.user.UserStatusEnu;
import com.loushi.component.properties.JwtProperties;
import com.loushi.mapper.UsersMapper;
import com.loushi.model.Users;
import com.loushi.utils.jwt.JwtHelper;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * token验证 过滤器
 * @author 技术部
 */

@Slf4j
public class JwtFilter extends GenericFilterBean {

    private JwtProperties jwtProperties;
    private UsersMapper userMapper;

    public JwtFilter(JwtProperties jwtProperties, UsersMapper userMapper) {
        this.jwtProperties = jwtProperties;
        this.userMapper = userMapper;
    }

    /**
     * Reserved claims（保留），它的含义就像是编程语言的保留字一样，属于JWT标准里面规定的一些claim。
     * JWT标准里面定好的claim有：
     * iss(Issuser)：代表这个JWT的签发主体； sub(Subject)：代表这个JWT的主体，即它的所有人；
     * aud(Audience)：代表这个JWT的接收对象； exp(Expiration time)：是一个时间戳，代表这个JWT的过期时间；
     * nbf(Not Before)：是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的；
     * iat(Issuedat)：是一个时间戳，代表这个JWT的签发时间； jti(JWT ID)：是JWT的唯一标识。
     */
    private final static String CONTENT_TYPE = "application/json; charset=utf-8";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String token = JwtHelper.getRequestToken(request);
        Claims claims = JwtHelper.parseJWT(token, jwtProperties.getBase64Secret());


        //预请求
        if (ServletUtil.METHOD_OPTIONS.equals(request.getMethod())) {
            chain.doFilter(req, res);

        } else if (Strings.isNullOrEmpty(token)) {
            ServletUtil.write(response, getRes(LoginState.no_login.getCode(), LoginState.no_login.getMsg()), CONTENT_TYPE);

        } else if (claims == null) {
            ServletUtil.write(response, getRes(LoginState.invalid_token.getCode(), LoginState.invalid_token.getMsg()), CONTENT_TYPE);

        } else {
            Users user = userMapper.selectByPrimaryKey(JwtHelper.getClaimsUid(claims));
            if (user == null)
                ServletUtil.write(response, getRes(LoginState.invalid_token.getCode(), LoginState.invalid_token.getMsg()), CONTENT_TYPE);

            else if (user.getUserStatus() == UserStatusEnu.lock.getValue()) {
                ServletUtil.write(response, getRes(LoginState.user_lock.getCode(), LoginState.user_lock.getMsg()), CONTENT_TYPE);

            } else {
                request.setAttribute(JwtCons.claims, claims);
                chain.doFilter(req, res);
            }
        }
    }


    @Getter
    public enum LoginState {

        no_login(401, "请登入"),
        invalid_token(402, "Invalid Token"),
        user_lock(403, "账号已冻结");

        private int code;
        private String msg;

        private LoginState(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

    }


    private static String getRes(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }

}
