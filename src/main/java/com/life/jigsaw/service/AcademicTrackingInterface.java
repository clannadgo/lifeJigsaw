package com.life.jigsaw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.life.jigsaw.domain.AcademicTracking;

import java.util.Date;
import java.util.List;

/**
 * 学业追踪服务接口
 */
public interface AcademicTrackingInterface extends IService<AcademicTracking> {
    
    /**
     * 根据ID获取学业追踪项目
     * @param id 项目ID
     * @return 学业追踪项目对象
     */
    AcademicTracking getById(Long id);
    
    /**
     * 获取所有学业追踪项目
     * @return 学业追踪项目列表
     */
    List<AcademicTracking> listAll();
    
    /**
     * 获取启用状态的学业追踪项目
     * @return 启用状态的学业追踪项目列表
     */
    List<AcademicTracking> listActiveTrackings();
    
    /**
     * 根据追踪类型获取学业追踪项目
     * @param trackingType 追踪类型
     * @return 学业追踪项目列表
     */
    List<AcademicTracking> listByType(String trackingType);
    
    /**
     * 获取截止日期前的学业追踪项目
     * @param deadline 截止日期
     * @return 学业追踪项目列表
     */
    List<AcademicTracking> listByDeadlineBefore(Date deadline);
    
    /**
     * 新增学业追踪项目
     * @param academicTracking 学业追踪项目对象
     * @return 是否新增成功
     */
    boolean saveTracking(AcademicTracking academicTracking);
    
    /**
     * 更新学业追踪项目
     * @param academicTracking 学业追踪项目对象
     * @return 是否更新成功
     */
    boolean updateTracking(AcademicTracking academicTracking);
    
    /**
     * 删除学业追踪项目（逻辑删除）
     * @param id 项目ID
     * @return 是否删除成功
     */
    boolean removeTracking(Long id);
}
