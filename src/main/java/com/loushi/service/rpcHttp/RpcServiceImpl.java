package com.loushi.service.rpcHttp;


import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.loushi.component.contants.CommonPythonCons;
import com.loushi.component.exception.CommonPythonException;
import com.loushi.vo.commonPython.CommonPythonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 技术部
 */

@Service
@Slf4j
public class RpcServiceImpl implements IRpcService {


    @Override
    @Retryable(value = {Exception.class})
    public CommonPythonVO commonPython(Map<String, Object> params, String url) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String res = HttpUtil.get(url, params);
        CommonPythonVO commonPythonVO = JSONObject.parseObject(res, CommonPythonVO.class);
        log.info("commonPython返回的数据...{}", commonPythonVO);

        if (commonPythonVO.getOk() == CommonPythonCons.code) {
            log.error("数据有误...{}", commonPythonVO.getData());
            throw new CommonPythonException("RPC调用异常, 重试中...");
        }
        return commonPythonVO;
    }


    @Recover
    public CommonPythonVO recover(Exception e) {
        log.error("退出重试...", e.getMessage());
        return new CommonPythonVO<>(null, e.getMessage(), CommonPythonCons.code);
    }

}
