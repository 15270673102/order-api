package com.loushi.mapper;

import com.loushi.model.UserTask;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface UserTaskMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserTask record);

    int insertSelective(UserTask record);

    UserTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserTask record);

    int updateByPrimaryKey(UserTask record);

    List<UserTask> selectTaskAll(Integer taskStatus);

    UserTask selectByPaublishTaskId(@Param("uid") Integer uid,
                                    @Param("taskId") Integer taskId);

    List<UserTask> selectBozhuTaskList(@Param("uid") Integer uid,
                                       @Param("taskStatus") Integer taskStatus,
                                       @Param("filterTime") String filterTime);

    List<UserTask> selectByUserNameTime(@Param("userId") Integer userId,
                                        @Param("startTime") String startTime,
                                        @Param("endTime") String endTime);
}
