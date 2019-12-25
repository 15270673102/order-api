package com.loushi.mapper;

import com.loushi.model.Cdkey;

public interface CdkeyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cdkey record);

    int insertSelective(Cdkey record);

    Cdkey selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cdkey record);

    int updateByPrimaryKey(Cdkey record);

    /**
     * 获取激活码
     */
    Cdkey selectByCode(String authCode);

}