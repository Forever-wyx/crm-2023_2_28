package com.xing.crm.workbench.service.impl;

import com.xing.crm.commons.domain.ReturnObject;
import com.xing.crm.commons.utils.Constants;
import com.xing.crm.commons.utils.FormatDateUtils;
import com.xing.crm.commons.utils.HSSFUtils;
import com.xing.crm.commons.utils.UUIDUtils;
import com.xing.crm.settings.domain.User;
import com.xing.crm.workbench.domain.Activity;
import com.xing.crm.workbench.mapper.ActivityMapper;
import com.xing.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;


@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    @Qualifier("returnObject")
    ReturnObject returnObject;

    public Object saveCreateActivity(Activity activity,HttpSession httpSession) {
        //处理前端传过来的数据;以及验证数据的正确性->此处7验证在jsp里面处理了,但是最好在这里处理,以便前后端代码分离
        //处理前端数据,加入id,createTime,createBy
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(FormatDateUtils.formatDateTime(new Date()));
        User user = (User) httpSession.getAttribute(Constants.SESSION_USER);
        activity.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        //保存数据
        try{
            int i = activityMapper.saveCreateInsert(activity);
            if (i > 0){
                //保存成功
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }
        }catch (Exception e){
            e.printStackTrace();
            //保存失败
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙...");
        }
        return returnObject;
    }


    public Object deleteActivityByIds(String[] ids) {
        try{
            int i = activityMapper.deleteActivityByIds(ids);
            if (i > 0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙");
        }
        return returnObject;
    }

    public Map queryActivityByConditionForPage(Map map){
        List<Activity> activityList = activityMapper.selectActivityByConditionForPage(map);
        //封装参数，返回Map对象给controller，controller在返回json给前端页面
        Map<String,Object> activityMap = new HashMap<String, Object>();
        activityMap.put("activityList",activityList);
        //市场活动总条数
        Integer totalRows = activityMapper.selectCountsOfActivityByCondition(map);
        activityMap.put("totalRows",totalRows);
        return activityMap;
    }

    /**
     * 批量文件下载
     * 传统io流文件下载:将内存中的文件写入到硬盘,再从硬盘中读取文件输出到客户端
     * 这里是直接从内存将文件流输出到客户端,这样可以节省大量的资源,速度会比存到硬盘快很多
     * @param response
     * @return
     * @throws Exception
     */
    public List<Activity> queryAllActivities(HttpServletResponse response) throws Exception{
        //返回所有的市场活动
        List<Activity> activities = activityMapper.selectAllActivities();
        if (activities != null && activities.size() > 0){
            //创建一个excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            //在excel文件中创建一张表
            HSSFSheet sheet = wb.createSheet("市场活动表");
            //在表中创建一行
            HSSFRow row = sheet.createRow(0);
            //在行中创建一列,并且赋值; 重复此操作
            HSSFCell cell = row.createCell(0);
            cell.setCellValue("ID");
            cell = row.createCell(1);
            cell.setCellValue("所有者");
            cell = row.createCell(2);
            cell.setCellValue("名称");
            cell = row.createCell(3);
            cell.setCellValue("开始时间");
            cell = row.createCell(4);
            cell.setCellValue("结束时间");
            cell = row.createCell(5);
            cell.setCellValue("成本");
            cell = row.createCell(6);
            cell.setCellValue("简述");
            cell = row.createCell(7);
            cell.setCellValue("创建时间");
            cell = row.createCell(8);
            cell.setCellValue("创建者");
            cell = row.createCell(9);
            cell.setCellValue("修改时间");
            cell = row.createCell(10);
            cell.setCellValue("修改者");

            //写数据
            for (int i = 0;i < activities.size();i++){
                Activity activity = activities.get(i);
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
 /*            //将数据从内存写到硬盘中
           String filePath = "D:\\Homework\\idea\\Amazing\\Project\\source\\activityList.xls";
            OutputStream os = new FileOutputStream(filePath);
            wb.write(os);*/

            //浏览需接收到响应信息之后，默认情况下，直接在显示窗口中打开响应信息，即使打不开，也会调用应用程序打开，只有实在打不开，才会激活文件下载
            // 可以设置响应头信息，使浏览器接收到响应信息之后，直接激活文件下载窗口，即使能打开也不打开
            response.addHeader("Content-Disposition","attachment;filename=myStudentList.xls");

/*            //从硬盘读取数据
            InputStream is = new FileInputStream(filePath);
            //将数据输出到客户端,也就是输出到浏览器中
            ServletOutputStream servletOutputStream = response.getOutputStream();
            byte[] bytes = new byte[256];
            int len = 0;
            while ((len = (is.read(bytes))) != -1){
                servletOutputStream.write(bytes,0,len);
            }*/

            //将文件流输出到客户端
            wb.write(response.getOutputStream());

            //释放资源,释放资源有一个原则,就是谁开启的就由谁关闭;服务器开启的就应该由服务器自己关闭
            wb.close();
        }
        return null;
    }


    /**
     * 选择文件下载
     * @param ids
     * @param response
     * @return
     * @throws Exception
     */
    public List<Activity> queryActivitiesByIds(String[] ids,HttpServletResponse response) throws Exception {

        //返回选中的市场活动
        List<Activity> activities = activityMapper.selectActivitiesByIds(ids);
        if (activities != null && activities.size() > 0){
            //创建一个excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            //在excel文件中创建一张表
            HSSFSheet sheet = wb.createSheet("市场活动表");
            //在表中创建一行
            HSSFRow row = sheet.createRow(0);
            //在行中创建一列,并且赋值; 重复此操作
            HSSFCell cell = row.createCell(0);
            cell.setCellValue("ID");
            cell = row.createCell(1);
            cell.setCellValue("所有者");
            cell = row.createCell(2);
            cell.setCellValue("名称");
            cell = row.createCell(3);
            cell.setCellValue("开始时间");
            cell = row.createCell(4);
            cell.setCellValue("结束时间");
            cell = row.createCell(5);
            cell.setCellValue("成本");
            cell = row.createCell(6);
            cell.setCellValue("简述");
            cell = row.createCell(7);
            cell.setCellValue("创建时间");
            cell = row.createCell(8);
            cell.setCellValue("创建者");
            cell = row.createCell(9);
            cell.setCellValue("修改时间");
            cell = row.createCell(10);
            cell.setCellValue("修改者");

            //写数据
            for (int i = 0;i < activities.size();i++){
                Activity activity = activities.get(i);
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }

            //浏览需接收到响应信息之后，默认情况下，直接在显示窗口中打开响应信息，即使打不开，也会调用应用程序打开，只有实在打不开，才会激活文件下载
            // 可以设置响应头信息，使浏览器接收到响应信息之后，直接激活文件下载窗口，即使能打开也不打开
            response.addHeader("Content-Disposition","attachment;filename=Activity.xls");

            //将文件流输出到客户端
            wb.write(response.getOutputStream());
            wb.close();
        }
        return null;
    }

    /**
     * 上传文件
     * @param multipartFile
     * @return
     */
    public Object saveCreateActivityByList(MultipartFile multipartFile,HttpSession session){
        try {
            String filename = multipartFile.getOriginalFilename();
            String filePath = "D:\\Homework\\idea\\Amazing\\Project\\source";
            File file = new File(filePath,filename);
            multipartFile.transferTo(file);
            //将文件从硬盘中读取到内存中
            FileInputStream is = new FileInputStream(filePath + "\\" + filename);
            //读取excel文件
            HSSFWorkbook wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = null;
            HSSFCell cell = null;
            List<Activity> activityList = new ArrayList<Activity>();
            User user = (User) session.getAttribute(Constants.SESSION_USER);
            for (int i = 1;i <= sheet.getLastRowNum();i++){ //getLastRowNum()获取最后一行的下标
                row = sheet.getRow(i);
                Activity activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(FormatDateUtils.formatDateTime(new Date()));
                activity.setCreateBy(user.getId());
                for (int j = 0; j < row.getLastCellNum();j++){   //getLastCellNum()获取最后一列的下标 + 1
                    cell = row.getCell(j);
                    //读取数据
                    if (j == 0){
                        activity.setName(HSSFUtils.getCellValueForStr(cell));
                        System.out.println(HSSFUtils.getCellValueForStr(cell));
                    }else if (j == 1){
                        activity.setStartDate(HSSFUtils.getCellValueForStr(cell));
                    }else if (j == 2){
                        activity.setEndDate(HSSFUtils.getCellValueForStr(cell));
                    }else if (j == 3){
                        activity.setCost(HSSFUtils.getCellValueForStr(cell));
                    }else if (j == 4){
                        activity.setDescription(HSSFUtils.getCellValueForStr(cell));
                    }
                }
                activityList.add(activity);
            }
            int ret = activityMapper.insertActivityByList(activityList);
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(ret);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙,请稍后重试");
        }
        return returnObject;
    }

    public Activity queryActivityById(String id){
        return activityMapper.selectActivityById(id);
    }

    public Object updateActivityById(Activity activity, HttpSession session) {
        activity.setEditBy(session.getId());
        activity.setEditTime(FormatDateUtils.formatDateTime(new Date()));
        try {
            int ret = activityMapper.updateActivityById(activity);
            if (ret > 0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙,请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙,请稍后重试");
        }
        return returnObject;
    }

    public Activity queryActivityForDetailById(String id){
        return activityMapper.selectActivityForDetailById(id);
    }

    public List<Activity> queryActivityForDetailByClueId(String clueId){
        return activityMapper.selectActivityForDetailByClueId(clueId);
    }

    public List<Activity> queryActivityForDetailByNameClueId(Map map){
        return activityMapper.selectActivityForDetailByNameClueId(map);
    }

    public List<Activity> queryActivityForDetailByIds(String[] ids){
        return activityMapper.selectActivityForDetailByIds(ids);
    }

    public List<Activity> queryActivityForCovertByNameClueId(Map map){
        return activityMapper.selectActivityForDetailByNameClueId(map);
    }
}
