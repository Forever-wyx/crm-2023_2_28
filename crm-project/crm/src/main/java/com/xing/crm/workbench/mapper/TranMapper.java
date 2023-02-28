package com.xing.crm.workbench.mapper;

import com.xing.crm.workbench.domain.FunnelVO;
import com.xing.crm.workbench.domain.Tran;

import java.util.List;

public interface TranMapper {

    int insertTran(Tran tran);

    Tran selectTranById(String id);

    List<FunnelVO> selectCountOfTranGroupByStage();
}