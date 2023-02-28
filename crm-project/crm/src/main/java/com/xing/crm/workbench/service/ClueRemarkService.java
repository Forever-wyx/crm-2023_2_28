package com.xing.crm.workbench.service;

import com.xing.crm.workbench.domain.Clue;
import com.xing.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {

    List<ClueRemark> queryClueRemarkForDetailByClueId(String id);

}
