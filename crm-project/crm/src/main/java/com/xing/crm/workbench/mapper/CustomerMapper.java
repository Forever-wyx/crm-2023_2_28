package com.xing.crm.workbench.mapper;

import com.xing.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerMapper {

    int insertCustomer(Customer customer);

    List<String> selectAllCustomerName(String name);

    Customer selectCustomerByName(String name);
}