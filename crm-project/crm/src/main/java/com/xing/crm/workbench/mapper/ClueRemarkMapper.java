package com.xing.crm.workbench.mapper;

import com.xing.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkMapper {

    /**
     * 根据clueId查询备注;sql语句有联表查询为了显示给用户看
     * @param clueId
     * @return
     */
    List<ClueRemark> selectClueRemarkForDetailByClueId(String clueId);

    /**
     * 根据clueId查询备注;sql语句没有联表查询为了保存到数据库中
     * @param clueId
     * @return
     */
    List<ClueRemark> selectClueRemark(String clueId);

    int deleteClueRemarkByClueId(String clueId);
}