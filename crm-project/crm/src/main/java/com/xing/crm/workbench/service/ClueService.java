package com.xing.crm.workbench.service;

import com.xing.crm.settings.domain.DicValue;
import com.xing.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {

    List<DicValue> queryDicValueByTypeCode(String typeCode);

    int insertClue(Clue clue);

    Clue queryClueForDetailById(String id);

    void saveCoverClue(Map map);
}
