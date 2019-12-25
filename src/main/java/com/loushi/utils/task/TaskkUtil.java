package com.loushi.utils.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loushi.component.enumeration.task.TaskPubEnu;
import com.loushi.mapper.UserOrderCrawlCommentMapper;
import com.loushi.mapper.UserOrderCrawlForwardMapper;
import com.loushi.mapper.UserOrderCrawlLikeMapper;
import com.loushi.mapper.UserPlatformMapper;
import com.loushi.model.*;
import com.loushi.vo.order.DoTaskItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class TaskkUtil {

    @Resource
    private UserOrderCrawlCommentMapper commentMapper;
    @Resource
    private UserOrderCrawlForwardMapper forwardMapper;
    @Resource
    private UserOrderCrawlLikeMapper likeMapper;
    @Resource
    private UserPlatformMapper userPlatformMapper;


    /**
     * get平台类型
     */
    public StringBuilder getTaskForm(UserTask task) {
        JSONArray taskForms = JSONObject.parseArray(task.getTaskform());
        StringBuilder sb = new StringBuilder();

        for (Object taskForm : taskForms) {
            if (TaskPubEnu.comment.getValue() == (Integer) taskForm)
                sb.append(TaskPubEnu.comment.getName()).append(" ");

            if (TaskPubEnu.like.getValue() == (Integer) taskForm)
                sb.append(TaskPubEnu.like.getName()).append(" ");

            if (TaskPubEnu.forward.getValue() == (Integer) taskForm)
                sb.append(TaskPubEnu.forward.getName()).append(" ");
        }
        return sb;
    }


    public DoTaskItemVO getDoTaskItem(UserTask task, Users user) {
        List<UserPlatform> userPlatforms = userPlatformMapper.selectByUserId(user.getId());
        JSONArray taskForms = JSONObject.parseArray(task.getTaskform());

        List<Object> comments = new ArrayList<>();
        List<Object> likes = new ArrayList<>();
        List<Object> forwards = new ArrayList<>();

        for (UserPlatform userPlatform : userPlatforms) {
            for (Object taskForm : taskForms) {
                if (TaskPubEnu.comment.getValue() == (Integer) taskForm) {
                    List<UserOrderCrawlComment> list = commentMapper.selectByUserIdAndTaskId(userPlatform.getAccountUid(), task.getId());
                    if (list.size() > 0) {
                        comments.add(list);
                    }
                }
                if (TaskPubEnu.like.getValue() == (Integer) taskForm) {
                    List<UserOrderCrawlLike> list = likeMapper.selectByUserIdAndTaskId(userPlatform.getAccountUid(), task.getId());
                    if (list.size() > 0) {
                        likes.add(list);
                    }
                }
                if (TaskPubEnu.forward.getValue() == (Integer) taskForm) {
                    List<UserOrderCrawlForward> list = forwardMapper.selectByUserIdAndTaskId(userPlatform.getAccountUid(), task.getId());
                    if (list.size() > 0) {
                        forwards.add(list);
                    }
                }
            }
        }
        return new DoTaskItemVO(comments, likes, forwards);
    }

}
