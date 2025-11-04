package com.life.jigsaw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.life.jigsaw.domain.ExtendedActivity;

import java.util.Date;
import java.util.List;

/**
 * 扩展活动服务接口
 */
public interface ExtendedActivityInterface extends IService<ExtendedActivity> {
    
    /**
     * 根据ID获取扩展活动
     * @param id 活动ID
     * @return 扩展活动对象
     */
    ExtendedActivity getById(Long id);
    
    /**
     * 获取所有扩展活动
     * @return 扩展活动列表
     */
    List<ExtendedActivity> listAll();
    
    /**
     * 获取进行中的扩展活动
     * @return 进行中的扩展活动列表
     */
    List<ExtendedActivity> listActiveActivities();
    
    /**
     * 根据日期范围获取扩展活动
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 扩展活动列表
     */
    List<ExtendedActivity> listByDateRange(Date startDate, Date endDate);
    
    /**
     * 新增扩展活动
     * @param extendedActivity 扩展活动对象
     * @return 是否新增成功
     */
    boolean saveActivity(ExtendedActivity extendedActivity);
    
    /**
     * 更新扩展活动
     * @param extendedActivity 扩展活动对象
     * @return 是否更新成功
     */
    boolean updateActivity(ExtendedActivity extendedActivity);
    
    /**
     * 删除扩展活动（逻辑删除）
     * @param id 活动ID
     * @return 是否删除成功
     */
    boolean removeActivity(Long id);
}
