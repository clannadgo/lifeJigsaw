package com.life.jigsaw.service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.life.jigsaw.domain.DailyTask;
import com.life.jigsaw.domain.DailyTaskCompletion;
import com.life.jigsaw.enums.ScoreType;
import com.life.jigsaw.mapper.DailyTaskCompletionMapper;
import com.life.jigsaw.service.DailyTaskCompletionInterface;
import com.life.jigsaw.service.DailyTaskInterface;
import com.life.jigsaw.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * 每日任务完成记录服务实现类
 */
@Service
public class DailyTaskCompletionImpl extends ServiceImpl<DailyTaskCompletionMapper, DailyTaskCompletion> implements DailyTaskCompletionInterface {

    private static final Logger logger = LoggerFactory.getLogger(DailyTaskCompletionImpl.class);

    @Autowired
    private DailyTaskInterface dailyTaskService;

    @Autowired
    private ScoreService scoreService;
    
    @Override
    public DailyTaskCompletion getById(Long id) {
        return baseMapper.selectById(id);
    }
    
    @Override
    public List<DailyTaskCompletion> listByUserId(Long userId) {
        QueryWrapper<DailyTaskCompletion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<DailyTaskCompletion> listByTaskId(Long taskId) {
        QueryWrapper<DailyTaskCompletion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public DailyTaskCompletion getByUserTaskAndDate(Long userId, Long taskId, Date completionDate) {
        QueryWrapper<DailyTaskCompletion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("task_id", taskId)
                   .eq("completion_date", completionDate);
        return baseMapper.selectOne(queryWrapper);
    }
    
    @Override
    public List<DailyTaskCompletion> listByUserAndDate(Long userId, Date completionDate) {
        QueryWrapper<DailyTaskCompletion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("completion_date", completionDate);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    @Transactional
    public boolean saveCompletion(DailyTaskCompletion completion) {
        try {
            // 保存完成记录
            boolean saveResult = this.save(completion);
            if (!saveResult) {
                logger.error("Failed to save daily task completion for user {}", completion.getUserId());
                return false;
            }

            // 如果任务已完成且有积分奖励，更新积分
            if (completion.getStatus() == 1 && completion.getEarnedScore() != null && completion.getEarnedScore() > 0) {
                // 获取任务信息
                DailyTask dailyTask = dailyTaskService.getById(completion.getTaskId());
                String taskName = dailyTask != null ? dailyTask.getTaskName() : "未知任务";
                
                // 更新积分
                boolean scoreResult = scoreService.addScore(
                    completion.getUserId(),
                    completion.getEarnedScore(),
                    ScoreType.DAILY_TASK,
                    "完成每日任务：" + taskName,
                    completion.getTaskId()
                );
                
                if (!scoreResult) {
                    logger.error("Failed to add score for daily task completion, userId: {}, taskId: {}", 
                                completion.getUserId(), completion.getTaskId());
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            logger.error("Error saving daily task completion", e);
            throw new RuntimeException("保存任务完成记录失败", e);
        }
    }
    
    @Override
    public boolean updateCompletion(DailyTaskCompletion completion) {
        return this.updateById(completion);
    }
    
    @Override
    public boolean removeCompletion(Long id) {
        return this.removeById(id);
    }
}
