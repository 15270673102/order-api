package com.loushi.mapper;

import com.loushi.model.UserOrderCrawlComment;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface UserOrderCrawlCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserOrderCrawlComment record);

    int insertSelective(UserOrderCrawlComment record);

    UserOrderCrawlComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserOrderCrawlComment record);

    int updateByPrimaryKey(UserOrderCrawlComment record);

    List<UserOrderCrawlComment> selectByTaskIdAndUid(@Param("taskId") Integer taskId,
                                                     @Param("accountUid") String accountUid);


    UserOrderCrawlComment selectByMsgId(String commentId);

    List<UserOrderCrawlComment> selectByUserIdAndTaskId(@Param("userId") String userId,
                                                        @Param("taskId") Integer taskId);

}