package com.xing.crm.settings.mapper;

import com.xing.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueMapper {

    List<DicValue> selectDicValueByTypeCode(String typeCode);

}