package com.life.jigsaw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.life.jigsaw.domain.AcademicTrackingCompletion;

import java.util.Date;
import java.util.List;

/**
 * 学业追踪完成记录服务接口
 */
public interface AcademicTrackingCompletionInterface extends IService<AcademicTrackingCompletion> {
    
    /**
     * 根据ID获取完成记录
     * @param id 记录ID
     * @return 完成记录对象
     */
    AcademicTrackingCompletion getById(Long id);
    
    /**
     * 根据用户ID获取完成记录
     * @param userId 用户ID
     * @return 完成记录列表
     */
    List<AcademicTrackingCompletion> listByUserId(Long userId);
    
    /**
     * 根据学业追踪项目ID获取完成记录
     * @param trackingId 学业追踪项目ID
     * @return 完成记录列表
     */
    List<AcademicTrackingCompletion> listByTrackingId(Long trackingId);
    
    /**
     * 获取用户在指定学业追踪项目的完成记录
     * @param userId 用户ID
     * @param trackingId 学业追踪项目ID
     * @return 完成记录列表
     */
    List<AcademicTrackingCompletion> listByUserAndTracking(Long userId, Long trackingId);
    
    /**
     * 获取用户在指定日期范围内的完成记录
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 完成记录列表
     */
    List<AcademicTrackingCompletion> listByUserAndDateRange(Long userId, Date startDate, Date endDate);
    
    /**
     * 新增完成记录
     * @param completion 完成记录对象
     * @return 是否新增成功
     */
    boolean saveCompletion(AcademicTrackingCompletion completion);
    
    /**
     * 更新完成记录
     * @param completion 完成记录对象
     * @return 是否更新成功
     */
    boolean updateCompletion(AcademicTrackingCompletion completion);
    
    /**
     * 删除完成记录（逻辑删除）
     * @param id 记录ID
     * @return 是否删除成功
     */
    boolean removeCompletion(Long id);
}
