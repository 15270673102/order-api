package com.loushi.utils;

import cn.hutool.core.date.DateUtil;
import com.loushi.component.enumeration.task.TaskStatusEnu;
import com.loushi.model.UserTask;
import com.loushi.vo.util.AgainCrawlVO;

import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils {

    /**
     * 时间戳(10位) long --> date
     * @param time 某个时间戳
     */
    public static Date weiboCommentStrToDate(long time) {
        return new Date(time * 1000);
    }


    /**
     * 计算到期时间
     * @param task
     * @param vo
     * @return
     */
    public static Date calculateAgainCrawlCancelDatetime(UserTask task, AgainCrawlVO vo) {
        //这个时间应该是爬取的最大时间
        Date maxTime = DateUtil.offsetMinute(task.getPublishTime(), task.getConsumeTime());
        if (TaskStatusEnu.cancel.getValue() == task.getTaskStatus()) {
            maxTime = task.getCancelTime();
        }
        if (vo.getDelayMinuteTimes() != null) {
            maxTime = DateUtil.offsetMinute(maxTime, vo.getDelayMinuteTimes());
        }
        return maxTime;
    }


    /**
     * 计算到期时间
     * @param task
     * @return
     */
    public static Date calculateFirstAuditWeiboTaskCancelDatetime(UserTask task) {
        Date maxTime = DateUtil.offsetMinute(task.getPublishTime(), task.getConsumeTime());
        if (TaskStatusEnu.cancel.getValue() == task.getTaskStatus()) {
            maxTime = task.getCancelTime();
        }
        return maxTime;
    }

}
