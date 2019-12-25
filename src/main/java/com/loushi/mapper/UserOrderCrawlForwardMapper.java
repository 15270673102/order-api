package com.loushi.mapper;

import com.loushi.model.UserOrderCrawlForward;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserOrderCrawlForwardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserOrderCrawlForward record);

    int insertSelective(UserOrderCrawlForward record);

    UserOrderCrawlForward selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserOrderCrawlForward record);

    int updateByPrimaryKey(UserOrderCrawlForward record);

    List<UserOrderCrawlForward> selectByTaskIdAndUid(@Param("taskId") Integer taskId,
                                                     @Param("accountUid") String accountUid);

    UserOrderCrawlForward selectByMsgId(String forwordId);

    List<UserOrderCrawlForward> selectByUserIdAndTaskId(@Param("userId") String accountUid,
                                                        @Param("taskId") Integer taskId);
}