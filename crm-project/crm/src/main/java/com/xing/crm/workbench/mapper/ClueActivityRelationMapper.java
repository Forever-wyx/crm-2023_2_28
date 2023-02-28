package com.xing.crm.workbench.mapper;


import com.xing.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationMapper {

    int insertClueActivityRelation(List<ClueActivityRelation> clueActivityRelationList);

    int deleteClueActivityRelation(ClueActivityRelation relation);

    List<ClueActivityRelation> selectClueActivityRelationByClueId(String clueId);

    int deleteClueActivityRelationByClueId(String clueId);
}