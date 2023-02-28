package com.xing.crm.workbench.service.impl;

import com.xing.crm.workbench.domain.Customer;
import com.xing.crm.workbench.mapper.CustomerMapper;
import com.xing.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    public int saveCustomer(Customer customer) {
        return customerMapper.insertCustomer(customer);
    }

    public List<String> queryAllCustomerName(String name) {
        return customerMapper.selectAllCustomerName(name);
    }

}
