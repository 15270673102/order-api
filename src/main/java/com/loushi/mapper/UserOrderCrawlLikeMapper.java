package com.loushi.mapper;

import com.loushi.model.UserOrderCrawlLike;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserOrderCrawlLikeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserOrderCrawlLike record);

    int insertSelective(UserOrderCrawlLike record);

    UserOrderCrawlLike selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserOrderCrawlLike record);

    int updateByPrimaryKey(UserOrderCrawlLike record);

    List<UserOrderCrawlLike> selectByTaskIdAndUid(@Param("taskId") Integer taskId,
                                                  @Param("accountUid") String accountUid);

    UserOrderCrawlLike selectByMsgId(String likeId);

    List<UserOrderCrawlLike> selectByUserIdAndTaskId(@Param("userId") String userId,
                                                     @Param("taskId") Integer taskId);
}