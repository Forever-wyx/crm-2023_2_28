package com.xing.crm.workbench.web.controller;

import com.xing.crm.workbench.domain.FunnelVO;
import com.xing.crm.workbench.mapper.TranMapper;
import com.xing.crm.workbench.service.impl.TranServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ChartController {

    @Autowired
    private TranServiceImpl tranService;

    @RequestMapping("/workbench/chart/transaction/toTransactionIndex.do")
    public String toTransactionIndex(){
        return "workbench/chart/transaction/index";
    }

    @RequestMapping("/workbench/chart/transaction/queryCountOfTranGroupByStage.do")
    @ResponseBody
    public Object queryCountOfTranGroupByStage(){
        List<FunnelVO> funnelVOS = tranService.queryCountOfTranGroupByStage();
        return funnelVOS;
    }
}
