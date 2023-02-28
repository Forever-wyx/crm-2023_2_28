package com.xing.crm.workbench.mapper;

import com.xing.crm.workbench.domain.Clue;

public interface ClueMapper {

    int insertClue(Clue clue);

    /**
     * 根据id查询线索显示到详情页面；Sql语句有联表查询
     * @param id
     * @return
     */
    Clue selectClueForDetailById(String id);

    /**
     * 根据id查询线索；sql语句没有联表查询
     * @param id
     * @return
     */
    Clue selectClueById(String id);

    int deleteClueById(String id);
}