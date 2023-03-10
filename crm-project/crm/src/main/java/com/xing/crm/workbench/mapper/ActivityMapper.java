package com.xing.crm.workbench.mapper;

import com.xing.crm.workbench.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Wed Nov 23 18:04:12 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Wed Nov 23 18:04:12 CST 2022
     */
    int insertSelective(Activity record);


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Wed Nov 23 18:04:12 CST 2022
     */
    int updateByPrimaryKeySelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Wed Nov 23 18:04:12 CST 2022
     */
    int updateByPrimaryKey(Activity record);

    /**
     * 保存新建的市场活动
     * @param activity
     * @return
     */
    int saveCreateInsert(Activity activity);

    /**
     * 通过条件分页查询所有市场活动
     * @param map
     * @return
     */
    List<Activity> selectActivityByConditionForPage(Map map);

    /**
     * 通过条件查询市场活动的总条数
     * @param map
     * @return
     */
    Integer selectCountsOfActivityByCondition(Map map);

    /**
     * 通过ids删除市场活动
     * @param ids
     * @return
     */
    int deleteActivityByIds(String[] ids);

    /**
     * 返回所有的市场活动
     * @return
     */
    List<Activity> selectAllActivities();

    /**
     * 通过id查询市场活动
     * @return
     */
    List<Activity> selectActivitiesByIds(String[] ids);

    int insertActivityByList(List<Activity> activityList);


    /**
     * 通过id查询一条数据
     * @param id
     * @return
     */
    Activity selectActivityById(String id);


    /**
     * 通过activity修改数据
     * @param activity
     * @return
     */
    int updateActivityById(Activity activity);

    /**
     * 通过id返回活动的明细
     * @param id
     * @return
     */
    Activity selectActivityForDetailById(String id);

    List<Activity> selectActivityForDetailByClueId(String clueId);

    List<Activity> selectActivityForDetailByNameClueId(Map map);

    /**
     * 返回Activity列表给detail.jsp页面
     * @param ids
     * @return
     */
    List<Activity> selectActivityForDetailByIds(String[] ids);

    List<Activity> selectActivityForCovertByNameClueId(Map map);

}