package com.loushi.vo.commonPython;

import lombok.Data;

import java.util.List;


@Data
public class WeiboCommentListVO {


    /**
     * commentList : [{"id":4417883624695524,"publish_time":1568789025,"publish_time_str":"2019-09-18 14:43:45","screen_name":"一个花少北得不到的女人","text":"回复 :请问哪里可以玩","user_id":5228344291},{"id":4417883390337029,"publish_time":1568788965,"publish_time_str":"2019-09-18 14:42:45","screen_name":"有事明天再说咯","text":"？？？？// :童年糖// :可爱(ง'-̀'́)ง// :怪不得吼多吼多拉郎\u2026// :好可爱啊！！！// : 谁不喜欢这个时候的少爷呢！！！！// :转发微博","user_id":2208307714},{"id":4417882739627511,"publish_time":1568788845,"publish_time_str":"2019-09-18 14:40:45","screen_name":"绝情冷少吹","text":"小德拉科的大背头真的绝了// :童年糖// :可爱(ง'-̀'́)ง// :怪不得吼多吼多拉郎\u2026// :好可爱啊！！！// : 谁不喜欢这个时候的少爷呢！！！！// :转发微博","user_id":6114209403},{"id":4417882349924491,"publish_time":1568788725,"publish_time_str":"2019-09-18 14:38:45","screen_name":"萌萌哒汪汪酱Wang","text":"我以为这是中国小朋友专属游戏// :童年糖// :可爱(ง'-̀'́)ง// :怪不得吼多吼多拉郎\u2026// :好可爱啊！！！// : 谁不喜欢这个时候的少爷呢！！！！// :转发微博","user_id":2814037541},{"id":4417881477402661,"publish_time":1568788545,"publish_time_str":"2019-09-18 14:35:45","screen_name":"YannQin","text":"赫敏：老娘不信赢不了你！(撸袖子)","user_id":6142438486},{"id":4417881309821993,"publish_time":1568788485,"publish_time_str":"2019-09-18 14:34:45","screen_name":"远远要吃霸王龙","text":"好可爱啊这就是童年啊// : 童年糖// :可爱(ง'-̀'́)ง// :怪不得吼多吼多拉郎\u2026// :好可爱啊！！！// : 谁不喜欢这个时候的少爷呢！！！！// :转发微博","user_id":1251100223},{"id":4417880316010211,"publish_time":1568788245,"publish_time_str":"2019-09-18 14:30:45","screen_name":"八PreTtyClosE九","text":"看着我就打开了我的游戏","user_id":2884083350},{"id":4417879795107850,"publish_time":1568788125,"publish_time_str":"2019-09-18 14:28:45","screen_name":"绿山墙的小红帽","text":"赫敏发量让人羡慕","user_id":3021093011},{"id":4417879505996829,"publish_time":1568788065,"publish_time_str":"2019-09-18 14:27:45","screen_name":"死宅菌","text":"太可爱了呜呜呜","user_id":1956924305},{"id":4417879015208941,"publish_time":1568787945,"publish_time_str":"2019-09-18 14:25:45","screen_name":"飞到外太空跟你遨游","text":"@不如喝点酒","user_id":1700621173}]
     * nextPage : true
     * page : 2
     */

    private boolean nextPage;
    private int page;
    private List<Comment> commentList;

    @Data
    public static class Comment {
        /**
         * id : 4417883624695524
         * publish_time : 1568789025
         * publish_time_str : 2019-09-18 14:43:45
         * screen_name : 一个花少北得不到的女人
         * text : 回复 :请问哪里可以玩
         * user_id : 5228344291
         */

        private long id;
        private int publish_time;
        private String publish_time_str;
        private String screen_name;
        private String text;
        private long user_id;
    }

}