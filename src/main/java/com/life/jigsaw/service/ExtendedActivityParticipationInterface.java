package com.life.jigsaw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.life.jigsaw.domain.ExtendedActivityParticipation;

import java.util.Date;
import java.util.List;

/**
 * 扩展活动参与记录服务接口
 */
public interface ExtendedActivityParticipationInterface extends IService<ExtendedActivityParticipation> {
    
    /**
     * 根据ID获取参与记录
     * @param id 记录ID
     * @return 参与记录对象
     */
    ExtendedActivityParticipation getById(Long id);
    
    /**
     * 根据用户ID获取参与记录
     * @param userId 用户ID
     * @return 参与记录列表
     */
    List<ExtendedActivityParticipation> listByUserId(Long userId);
    
    /**
     * 根据活动ID获取参与记录
     * @param activityId 活动ID
     * @return 参与记录列表
     */
    List<ExtendedActivityParticipation> listByActivityId(Long activityId);
    
    /**
     * 获取用户在指定活动的参与记录
     * @param userId 用户ID
     * @param activityId 活动ID
     * @return 参与记录列表
     */
    List<ExtendedActivityParticipation> listByUserAndActivity(Long userId, Long activityId);
    
    /**
     * 获取用户在指定日期范围内的参与记录
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 参与记录列表
     */
    List<ExtendedActivityParticipation> listByUserAndDateRange(Long userId, Date startDate, Date endDate);
    
    /**
     * 新增参与记录
     * @param participation 参与记录对象
     * @return 是否新增成功
     */
    boolean saveParticipation(ExtendedActivityParticipation participation);
    
    /**
     * 更新参与记录
     * @param participation 参与记录对象
     * @return 是否更新成功
     */
    boolean updateParticipation(ExtendedActivityParticipation participation);
    
    /**
     * 删除参与记录（逻辑删除）
     * @param id 记录ID
     * @return 是否删除成功
     */
    boolean removeParticipation(Long id);
}
