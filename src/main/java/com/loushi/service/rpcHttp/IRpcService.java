package com.loushi.service.rpcHttp;

import com.loushi.vo.commonPython.CommonPythonVO;

import java.util.Map;

public interface IRpcService {

    /**
     * rpc  python
     * @param params
     * @param url
     */
    CommonPythonVO commonPython(Map<String, Object> params, String url) ;

}
