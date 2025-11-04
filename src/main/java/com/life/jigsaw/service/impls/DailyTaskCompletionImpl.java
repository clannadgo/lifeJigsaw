package com.life.jigsaw.service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.life.jigsaw.domain.DailyTaskCompletion;
import com.life.jigsaw.mapper.DailyTaskCompletionMapper;
import com.life.jigsaw.service.DailyTaskCompletionInterface;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 每日任务完成记录服务实现类
 */
@Service
public class DailyTaskCompletionImpl extends ServiceImpl<DailyTaskCompletionMapper, DailyTaskCompletion> implements DailyTaskCompletionInterface {
    
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
    public boolean saveCompletion(DailyTaskCompletion completion) {
        return this.save(completion);
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
