package com.loushi.service;

import com.github.pagehelper.PageInfo;
import com.loushi.model.UserTask;
import com.loushi.vo.task.BozhuTaskListVO;
import com.loushi.vo.task.PublishTaskRabbitVO;
import com.loushi.vo.task.PublishTaskVO;

/**
 * 任务中心
 */
public interface ITaskService {

    /**
     * 主页所有任务展示
     *
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<UserTask> selectTaskAll(Integer uid, Integer pageNum, Integer pageSize);

    /**
     * 任务详情
     * @param uid
     * @param taskId
     */
    UserTask orderItem(Integer uid, Integer taskId);

    /**
     * 取消任务
     * @param taskId
     * @param uid
     */
    void cancelTask(Integer taskId, Integer uid) throws Exception;

    /**
     * 发布任务
     * @param vo
     * @param uid
     */
    void publishTask(PublishTaskVO vo, Integer uid) throws Exception;

    /**
     * 博主任务列表
     * @param uid
     * @param vo
     * @return
     */
    PageInfo<UserTask> selectBozhuTaskList(Integer uid, BozhuTaskListVO vo);

    void publishTaskConsumer(PublishTaskRabbitVO vo);

}
