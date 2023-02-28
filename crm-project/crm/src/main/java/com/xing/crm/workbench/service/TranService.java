package com.xing.crm.workbench.service;

import com.xing.crm.workbench.domain.FunnelVO;
import com.xing.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {

    void saveTran(Map<String,Object> map);

    Tran queryTranById(String id);

    List<FunnelVO> queryCountOfTranGroupByStage();
}
