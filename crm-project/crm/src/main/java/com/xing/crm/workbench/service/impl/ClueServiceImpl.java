package com.xing.crm.workbench.service.impl;

import com.xing.crm.commons.utils.Constants;
import com.xing.crm.commons.utils.FormatDateUtils;
import com.xing.crm.commons.utils.UUIDUtils;
import com.xing.crm.settings.domain.DicValue;
import com.xing.crm.settings.domain.User;
import com.xing.crm.settings.mapper.DicValueMapper;
import com.xing.crm.workbench.domain.*;
import com.xing.crm.workbench.mapper.*;
import com.xing.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {

    @Autowired
    private DicValueMapper dicValueMapper;

    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private CustomerActivityRelationMapper customerActivityRelationMapper;

    @Autowired
    private TranMapper tranMapper;

    public List<DicValue> queryDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectDicValueByTypeCode(typeCode);
    }

    public int insertClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    public Clue queryClueForDetailById(String id){
        return clueMapper.selectClueForDetailById(id);
    }

    public void saveCoverClue(Map map){
        //取参数，调用mapper层
        String clueId = (String) map.get("clueId");
        User user = (User) map.get(Constants.SESSION_USER);


        //1.根据id查询线索的信息
        Clue clue = clueMapper.selectClueById(clueId);

        //2.把该线索中有关公司的信息保存到客户表中
        Customer customer = new Customer();
        customer.setId(UUIDUtils.getUUID());
        customer.setOwner(clue.getOwner());
        customer.setName(clue.getFullname());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(FormatDateUtils.formatDateTime(new Date()));
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setDescription(clue.getDescription());
        customer.setAddress(clue.getAddress());
        customerMapper.insertCustomer(customer);

        //3.把该线索中有关个人的信息转换到联系人表中
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtils.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAddress(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(FormatDateUtils.formatDateTime(new Date()));
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());
        contactsMapper.insertContacts(contacts);

        //4.根据clueId查询该线索下的所有备注
        List<ClueRemark> clueRemarkList = clueRemarkMapper.selectClueRemark(clueId);

        //5.把该线索下的备注转换到客户备注表中
        //6.把该线索下的备注转换到联系人备注表中
        //判断该线索是否有备注
        if (clueRemarkList != null && clueRemarkList.size() > 0){
            CustomerRemark customerRemark = null;
            List<CustomerRemark> customerRemarkList = new ArrayList<CustomerRemark>();

            ContactsRemark contactsRemark = null;
            List<ContactsRemark> contactsRemarkList = new ArrayList<ContactsRemark>();
            for(ClueRemark clueRemark:clueRemarkList){
                customerRemark = new CustomerRemark();
                customerRemark.setId(UUIDUtils.getUUID());
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                customerRemark.setCreateBy(clueRemark.getCreateBy());
                customerRemark.setCreateTime(clueRemark.getCreateTime());
                customerRemark.setEditBy(clueRemark.getEditBy());
                customerRemark.setEditTime(clueRemark.getEditTime());
                customerRemark.setEditFlag(clueRemark.getEditFlag());
                customerRemark.setCustomerId(customer.getId());
                customerRemarkList.add(customerRemark);

                contactsRemark = new ContactsRemark();
                contactsRemark.setId(UUIDUtils.getUUID());
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                contactsRemark.setCreateBy(clueRemark.getCreateBy());
                contactsRemark.setCreateTime(clueRemark.getCreateTime());
                contactsRemark.setEditBy(clueRemark.getEditBy());
                contactsRemark.setEditTime(clueRemark.getEditTime());
                contactsRemark.setEditFlag(clueRemark.getEditFlag());
                contactsRemark.setContactsId(contacts.getId());
                contactsRemarkList.add(contactsRemark);
            }

            customerRemarkMapper.insertCustomerRemark(customerRemarkList);
            contactsRemarkMapper.insertContactsRemark(contactsRemarkList);
        }

        //7.根据clueId查询该线索与市场活动的关联关系
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);

        //8.把该线索和市场活动的关联关系转换到联系人和市场活动的关联关系表中
        if (clueActivityRelationList != null && clueActivityRelationList.size() > 0){
            ContactsActivityRelation car = null;
            List<ContactsActivityRelation> carList = new ArrayList<ContactsActivityRelation>();
            CustomerActivityRelation cuar = null;
            List<CustomerActivityRelation> cuarList = new ArrayList<CustomerActivityRelation>();
            for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
                car = new ContactsActivityRelation();
                car.setId(UUIDUtils.getUUID());
                car.setActivityId(clueActivityRelation.getId());
                car.setContactsId(contacts.getId());
                carList.add(car);

                cuar = new CustomerActivityRelation();
                cuar.setId(UUIDUtils.getUUID());
                cuar.setActivityId(clueActivityRelation.getId());
                cuar.setCustomerId(customer.getId());
                cuarList.add(cuar);
            }
            contactsActivityRelationMapper.insertContactsActivityRelation(carList);
            customerActivityRelationMapper.insertCustomerActivityRelation(cuarList);
        }

        //9.判断是否为客户创建简易的交易（只有关键字段）
        String money = (String) map.get("money");
        String name = (String) map.get("name");
        String expectedDate = (String) map.get("expectedDate");
        String stage = (String) map.get("stage");
        String activityId = (String) map.get("activityId");
        String isCreateTransaction = (String) map.get("isCreateTransaction");
        if (isCreateTransaction.equals("true")){
            //创建简易的交易
            Tran tran = new Tran();
            tran.setId(UUIDUtils.getUUID());
            tran.setOwner(user.getId());
            tran.setMoney(money);
            tran.setName(name);
            tran.setExpectedDate(expectedDate);
            tran.setCustomerId(customer.getId());
            tran.setStage(stage);
            tran.setActivityId(activityId);
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(FormatDateUtils.formatDateTime(new Date()));
            tranMapper.insertTran(tran);
        }

        //10.删除该线索下的所有备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);

        //11.删除该线索下所有市场关联活动
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);

        //12.删除该线索
        clueMapper.deleteClueById(clueId);

    }



}
