package com.xing.crm.workbench.service.impl;

import com.xing.crm.workbench.domain.TranHistory;
import com.xing.crm.workbench.mapper.TranHistoryMapper;
import com.xing.crm.workbench.service.TranHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tranHistoryService")
public class TranHistoryServiceImpl implements TranHistoryService {

    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    public int saveTranHistory(TranHistory tranHistory) {
        return tranHistoryMapper.insert(tranHistory);
    }

    public List<TranHistory> queryTranHistoryByTranId(String tranId) {
        return tranHistoryMapper.selectTranHistoryByTranId(tranId);
    }
}
