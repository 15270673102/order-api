package com.loushi.component.contants;

/**
 * @author 技术部
 */

public class CommonPythonCons {

    public static final int code = 500;

    public static final int start_page = 1;

    //==================================weibo================================
    /**
     * 根据微博msgid获取数据
     */
    public static final String weibo_content_msg_url = "/weibo/weiboContent";

    /**
     * 根据微博msgid获取评论(1,2级评论包含在一起的)老接口
     */
    public static final String weibo_comment_list_url = "/weibo/weiboCommentList";
    public static final String weibo_like_list_url = "/weibo/weiboLikeList";
    public static final String weibo_forword_list_url = "/weibo/weiboForwordList";


    /**
     * 根据微博uid获取个人数据
     */
    public static final String weibo_user_info = "/weibo/weiboUserInfo";

}
