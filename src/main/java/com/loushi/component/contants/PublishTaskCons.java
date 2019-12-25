package com.loushi.component.contants;


public class PublishTaskCons extends CommonCons {

    //发布保留时间redis前缀
    private static final String publishTaskRedisPrefix = redis_base_url + "publishTaskRedisPrefix:";

    //微博平台
    public static final String weibo = publishTaskRedisPrefix + "weibo:";


}
