package com.loushi.controller.common;

import com.loushi.component.properties.BasisProperties;
import com.loushi.utils.jwt.JwtHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public abstract class BaseController extends AbstractProfiles {

    @Resource
    private HttpServletRequest request;
    @Resource
    private BasisProperties basisProperties;

    /**
     * 获取到HttpServletRequest中的uid
     */
    protected Integer getToken() {
        return JwtHelper.getRequestUid(request);
    }


    protected boolean isProdActive() {
        return getActive().equals(basisProperties.getPro());
    }
}
