package com.loushi.service.util;


import com.loushi.mapper.SysDeptMapper;
import com.loushi.model.SysDept;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional(noRollbackFor = Exception.class)
public class DeptServiceImpl implements IDeptService {

    @Resource
    private SysDeptMapper deptMapper;


    @Override
    public List<SysDept> all() {
        return deptMapper.selectAll();
    }
}
