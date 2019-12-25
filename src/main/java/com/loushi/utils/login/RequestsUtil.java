package com.loushi.utils.login;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * requests
 */
public class RequestsUtil {


    /**
     * 登录凭证校验。通过 wx.login 接口获得临时登录凭证 code 后传到开发者服务器调用此接口完成登录流程 (向微信服务器 使用登录凭证 code 获取 session_key 和 openid)
     * @param appid
     * @param secret
     * @param code
     * @return
     */
    public static JSONObject decodeUserInfo(String appid, String secret, String code) {
        //登录凭证校验。通过 wx.login 接口获得临时登录凭证 code 后传到开发者服务器调用此接口完成登录流程(向微信服务器 使用登录凭证 code 获取 session_key 和 openid)
        String url = "https://api.weixin.qq.com/sns/jscode2session";

        Map<String, Object> params = new HashMap<>();
        params.put("appid", appid);
        params.put("secret", secret);
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        String read = HttpUtil.get(url, params);
        return JSONObject.parseObject(read);
    }

}
