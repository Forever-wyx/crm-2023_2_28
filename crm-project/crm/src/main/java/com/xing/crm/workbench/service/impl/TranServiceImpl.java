package com.xing.crm.workbench.service.impl;

import com.xing.crm.commons.utils.FormatDateUtils;
import com.xing.crm.commons.utils.UUIDUtils;
import com.xing.crm.settings.domain.User;
import com.xing.crm.workbench.domain.Customer;
import com.xing.crm.workbench.domain.FunnelVO;
import com.xing.crm.workbench.domain.Tran;
import com.xing.crm.workbench.domain.TranHistory;
import com.xing.crm.workbench.mapper.CustomerMapper;
import com.xing.crm.workbench.mapper.TranHistoryMapper;
import com.xing.crm.workbench.mapper.TranMapper;
import com.xing.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("tranService")
public class TranServiceImpl implements TranService {

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    public void saveTran(Map<String,Object> map) {
        //获取参数
        User user =(User) map.get("user");
        String owner = (String) map.get("owner");
        String money = (String) map.get("money");
        String name = (String) map.get("name");
        String expectedDate = (String) map.get("expectedDate");
        String stage = (String) map.get("stage");
        String type = (String) map.get("type");
        String source = (String) map.get("source");
        String description = (String) map.get("description");
        String contactSummary = (String) map.get("contactSummary");
        String nextContactTime = (String) map.get("nextContactTime");
        String activityId = (String) map.get("activityId");
        String contactsId = (String) map.get("contactsId");
        String customerName = (String) map.get("customerName");

        //创建时间
        String nowDateTime = FormatDateUtils.formatDateTime(new Date());

        //判断客户是否存在，若不存在则新建
        Customer customer = customerMapper.selectCustomerByName(customerName);
        if (customer == null){
            //新建客户
            customer = new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(owner);
            customer.setName(customerName);
            customer.setCreateBy(user.getId());
            customer.setCreateTime(nowDateTime);
            customer.setContactSummary(contactSummary);
            customer.setNextContactTime(nextContactTime);
            customer.setDescription(description);
            //创建一个简易的新客户
            customerMapper.insertCustomer(customer);
        }

        //插入交易
        Tran tran = new Tran();
        tran.setCreateBy(user.getId());
        tran.setCreateTime(nowDateTime);
        tran.setId(UUIDUtils.getUUID());
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);
        tran.setContactsId(contactsId);
        tran.setActivityId(activityId);
        tran.setCustomerId(customer.getId());
        tranMapper.insertTran(tran);

        //插入交易历史记录
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setStage(stage);
        tranHistory.setMoney(money);
        tranHistory.setExpectedDate(expectedDate);
        tranHistory.setCreateTime(nowDateTime);
        tranHistory.setCreateBy(user.getId());
        tranHistory.setTranId(tran.getId());
        tranHistoryMapper.insertTranHistory(tranHistory);
    }


    public Tran queryTranById(String id) {
        return tranMapper.selectTranById(id);
    }

    public List<FunnelVO> queryCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }
}
