package com.xing.crm.workbench.service.impl;

import com.xing.crm.workbench.domain.ClueActivityRelation;
import com.xing.crm.workbench.mapper.ClueActivityRelationMapper;
import com.xing.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("clueActivityRelationService")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    public int insertClueActivityRelationService(List<ClueActivityRelation> clueActivityRelationList) {
        return clueActivityRelationMapper.insertClueActivityRelation(clueActivityRelationList);
    }

    public int deleteClueActivityRelation(ClueActivityRelation relation){
        return clueActivityRelationMapper.deleteClueActivityRelation(relation);
    }
}
