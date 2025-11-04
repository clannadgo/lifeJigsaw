package com.life.jigsaw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.life.jigsaw.domain.DailyTask;

import java.util.List;

/**
 * 每日任务服务接口
 */
public interface DailyTaskInterface extends IService<DailyTask> {
    
    /**
     * 根据ID获取每日任务
     * @param id 任务ID
     * @return 每日任务对象
     */
    DailyTask getById(Long id);
    
    /**
     * 获取所有每日任务
     * @return 每日任务列表
     */
    List<DailyTask> listAll();
    
    /**
     * 获取启用状态的每日任务
     * @return 启用状态的每日任务列表
     */
    List<DailyTask> listActiveTasks();
    
    /**
     * 新增每日任务
     * @param dailyTask 每日任务对象
     * @return 是否新增成功
     */
    boolean saveTask(DailyTask dailyTask);
    
    /**
     * 更新每日任务
     * @param dailyTask 每日任务对象
     * @return 是否更新成功
     */
    boolean updateTask(DailyTask dailyTask);
    
    /**
     * 删除每日任务（逻辑删除）
     * @param id 任务ID
     * @return 是否删除成功
     */
    boolean removeTask(Long id);
}
