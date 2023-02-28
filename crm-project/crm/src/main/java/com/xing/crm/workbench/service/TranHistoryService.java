package com.xing.crm.workbench.service;

import com.xing.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryService {

    int saveTranHistory(TranHistory tranHistory);

    List<TranHistory> queryTranHistoryByTranId(String tranId);
}
