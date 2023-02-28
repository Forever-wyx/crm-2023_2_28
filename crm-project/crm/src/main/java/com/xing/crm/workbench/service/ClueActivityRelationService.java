package com.xing.crm.workbench.service;

import com.xing.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {

    int insertClueActivityRelationService(List<ClueActivityRelation> clueActivityRelationList);

    int deleteClueActivityRelation(ClueActivityRelation relation);
}
