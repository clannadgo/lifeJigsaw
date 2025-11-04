package com.life.jigsaw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.life.jigsaw.domain.DailyTaskCompletion;

import java.util.Date;
import java.util.List;

/**
 * 每日任务完成记录服务接口
 */
public interface DailyTaskCompletionInterface extends IService<DailyTaskCompletion> {
    
    /**
     * 根据ID获取任务完成记录
     * @param id 记录ID
     * @return 任务完成记录对象
     */
    DailyTaskCompletion getById(Long id);
    
    /**
     * 根据用户ID获取任务完成记录
     * @param userId 用户ID
     * @return 任务完成记录列表
     */
    List<DailyTaskCompletion> listByUserId(Long userId);
    
    /**
     * 根据任务ID获取任务完成记录
     * @param taskId 任务ID
     * @return 任务完成记录列表
     */
    List<DailyTaskCompletion> listByTaskId(Long taskId);
    
    /**
     * 根据用户ID和任务ID获取指定日期的完成记录
     * @param userId 用户ID
     * @param taskId 任务ID
     * @param completionDate 完成日期
     * @return 任务完成记录
     */
    DailyTaskCompletion getByUserTaskAndDate(Long userId, Long taskId, Date completionDate);
    
    /**
     * 获取用户在指定日期的任务完成记录
     * @param userId 用户ID
     * @param completionDate 完成日期
     * @return 任务完成记录列表
     */
    List<DailyTaskCompletion> listByUserAndDate(Long userId, Date completionDate);
    
    /**
     * 新增任务完成记录
     * @param completion 任务完成记录对象
     * @return 是否新增成功
     */
    boolean saveCompletion(DailyTaskCompletion completion);
    
    /**
     * 更新任务完成记录
     * @param completion 任务完成记录对象
     * @return 是否更新成功
     */
    boolean updateCompletion(DailyTaskCompletion completion);
    
    /**
     * 删除任务完成记录（逻辑删除）
     * @param id 记录ID
     * @return 是否删除成功
     */
    boolean removeCompletion(Long id);
}
