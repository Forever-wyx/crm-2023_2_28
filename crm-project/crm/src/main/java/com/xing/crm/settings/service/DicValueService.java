package com.xing.crm.settings.service;

import com.xing.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueService {

    List<DicValue> queryDicValueByTypeCode(String typeCode);
}
