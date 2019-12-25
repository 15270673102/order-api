package com.loushi.utils.commonPython;

import com.loushi.component.contants.CommonPythonCons;
import com.loushi.component.properties.CommonPythonProperties;
import com.loushi.service.rpcHttp.IRpcService;
import com.loushi.vo.commonPython.CommonPythonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * 调用 common-python  api
 */

@Slf4j
@Component
public class PythonHttpApi {

    @Resource
    private CommonPythonProperties commonPythonProperties;
    @Resource
    private IRpcService rpcService;

    private CommonPythonVO setHttp(Map<String, Object> params, String url) {
        log.info("params-->【{}】 url-->【{}】", params, url);
        return rpcService.commonPython(params, url);
    }


    /**
     * 爬取某条微博消息
     */
    public CommonPythonVO getWeiboContent(String msgid) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", msgid);
        return setHttp(params, commonPythonProperties.getBaseUrl() + CommonPythonCons.weibo_content_msg_url);
    }


    /**
     * 根据微博msgid获取评论
     */
    public CommonPythonVO getWeiboCommentList(String msgid, Integer page) {
        Map<String, Object> params = new HashMap<>();
        params.put("msgId", msgid);
        params.put("page", page);
        return setHttp(params, commonPythonProperties.getBaseUrl() + CommonPythonCons.weibo_comment_list_url);
    }


    /**
     * 根据微博msgid获取点赞
     */
    public CommonPythonVO getWeiboLikeList(String msgid, Integer page) {
        Map<String, Object> params = new HashMap<>();
        params.put("msgId", msgid);
        params.put("page", page);
        return setHttp(params, commonPythonProperties.getBaseUrl() + CommonPythonCons.weibo_like_list_url);
    }

    /**
     * 根据微博msgid获取转发
     */
    public CommonPythonVO getWeiboForwordList(String msgid, Integer page) {
        Map<String, Object> params = new HashMap<>();
        params.put("msgId", msgid);
        params.put("page", page);
        return setHttp(params, commonPythonProperties.getBaseUrl() + CommonPythonCons.weibo_forword_list_url);
    }

    /**
     * 根据微博uid获取个人数据
     */
    public CommonPythonVO getWeiboUserInfo(String uid) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        return setHttp(params, commonPythonProperties.getBaseUrl() + CommonPythonCons.weibo_user_info);
    }


}
