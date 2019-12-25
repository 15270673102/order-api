package com.loushi.utils.weibo;

import cn.hutool.core.util.ReUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 微博工具类
 * @author 技术部
 */

@Slf4j
public class WeiboLinkUtil {

    /**
     * 通过web微博链接 获取微博消息bid
     * 电脑微博连接  https://weibo.com/1722058350/Hxb8F7MQq?from=page_1005051722058350_profile&wvr=6&mod=weibotime
     * 手机微博链接  https://m.weibo.cn/1696312643/4409508371137728
     * h5微博链接    https://m.weibo.cn/detail/4409490726045396
     * h5微博链接    https://m.weibo.cn/status/4408873676776685
     */
    private static final Pattern webMatcher = Pattern.compile("weibo.com/.*/(\\w+)");
    private static final Pattern mobileMatcher = Pattern.compile("m.weibo.cn/.*/(.*)");
    private static final Pattern h5OneMatcher = Pattern.compile("m.weibo.cn/detail/(.*)");
    private static final Pattern h5TwoMatcher = Pattern.compile("m.weibo.cn/status/(.*)");

    public static String getWeiboContentMsgId(String str) throws Exception {
        List<Pattern> patterns = Arrays.asList(webMatcher, mobileMatcher, h5OneMatcher, h5TwoMatcher);

        for (Pattern pattern : patterns) {
            if (ReUtil.contains(pattern, str)) {
                return ReUtil.getGroup1(pattern, str);
            }
        }
        throw new Exception(String.format("爬取链接有误... %s", str));
    }

    public static String getWeiboContentMsgId2(String str) {
        List<Pattern> patterns = Arrays.asList(webMatcher, mobileMatcher, h5OneMatcher, h5TwoMatcher);

        for (Pattern pattern : patterns) {
            if (ReUtil.contains(pattern, str)) {
                return ReUtil.getGroup1(pattern, str);
            }
        }
        return null;
    }

}
