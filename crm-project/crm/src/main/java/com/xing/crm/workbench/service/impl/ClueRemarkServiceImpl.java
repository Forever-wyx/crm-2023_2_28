package com.xing.crm.workbench.service.impl;

import com.xing.crm.workbench.domain.ClueRemark;
import com.xing.crm.workbench.mapper.ClueRemarkMapper;
import com.xing.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("clueRemarkService")
public class ClueRemarkServiceImpl implements ClueRemarkService {

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    public List<ClueRemark> queryClueRemarkForDetailByClueId(String id) {
        return clueRemarkMapper.selectClueRemarkForDetailByClueId(id);
    }
}
