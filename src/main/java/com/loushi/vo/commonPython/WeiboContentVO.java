package com.loushi.vo.commonPython;

import lombok.Data;

@Data
public class WeiboContentVO {


    /**
     * ad_state :
     * msg_bid : I7v9w1dlu
     * msg_comment_num : 0
     * msg_forward_num : 0
     * msg_id : 4417880500289632
     * msg_like_num : 0
     * publish_attach_url : ['https://video.weibo.com/show?fid=1034%3A4417873775407893']
     * publish_msg : 妈蛋的养你有何用，见危险跑的比谁都快。
     * publish_msg_from : 微博视频
     * publish_time : Wed Sep 18 14:30:54 +0800 2019
     * publisher_nick_name : 爱拍视频
     * publisher_uid : 2402749903
     * reptile_url : https://m.weibo.cn/detail/4417880500289632
     * video_play_num : 162
     */

    private String ad_state;
    private String msg_bid;
    private String msg_comment_num;
    private String msg_forward_num;
    private String msg_id;
    private String msg_like_num;
    private String publish_attach_url;
    private String publish_msg;
    private String publish_msg_from;
    private String publish_time;
    private String publisher_nick_name;
    private long publisher_uid;
    private String reptile_url;
    private String video_play_num;

}
