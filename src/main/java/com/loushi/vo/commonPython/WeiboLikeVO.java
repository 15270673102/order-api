package com.loushi.vo.commonPython;


import lombok.Data;

import java.util.List;

@Data
public class WeiboLikeVO {


    /**
     * likeList : [{"created_at":1568877039,"created_at_str":"2019-09-19 15:10:39","id":"4418252736229191","screen_name":"cooker土","uid":5287269733},{"created_at":1568876139,"created_at_str":"2019-09-19 14:55:39","id":"4418248923403693","screen_name":"纬驚","uid":5436525327},{"created_at":1568876079,"created_at_str":"2019-09-19 14:54:39","id":"4418248814949485","screen_name":"大西瓜在岸上也要加油呀","uid":3453305374}]
     * nextPage : false
     * page : 2
     */

    private boolean nextPage;
    private int page;
    private List<Like> likeList;

    @Data
    public static class Like {
        /**
         * created_at : 1568877039
         * created_at_str : 2019-09-19 15:10:39
         * id : 4418252736229191
         * screen_name : cooker土
         * uid : 5287269733
         */

        private int created_at;
        private String created_at_str;
        private String id;
        private String screen_name;
        private long uid;
    }
}
