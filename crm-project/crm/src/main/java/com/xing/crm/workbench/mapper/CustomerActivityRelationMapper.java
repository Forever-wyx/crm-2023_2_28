package com.xing.crm.workbench.mapper;

import com.xing.crm.workbench.domain.CustomerActivityRelation;

import java.util.List;

public interface CustomerActivityRelationMapper {

    int insertCustomerActivityRelation(List<CustomerActivityRelation> list);
}