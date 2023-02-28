package com.xing.crm.workbench.service;

import com.xing.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerService {

    int saveCustomer(Customer customer);

    List<String> queryAllCustomerName(String name);

}
