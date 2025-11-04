package com.life.jigsaw.service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.life.jigsaw.domain.DailyTask;
import com.life.jigsaw.mapper.DailyTaskMapper;
import com.life.jigsaw.service.DailyTaskInterface;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 每日任务服务实现类
 */
@Service
public class DailyTaskImpl extends ServiceImpl<DailyTaskMapper, DailyTask> implements DailyTaskInterface {
    
    @Override
    public DailyTask getById(Long id) {
        return baseMapper.selectById(id);
    }
    
    @Override
    public List<DailyTask> listAll() {
        return baseMapper.selectList(null);
    }
    
    @Override
    public List<DailyTask> listActiveTasks() {
        QueryWrapper<DailyTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public boolean saveTask(DailyTask dailyTask) {
        return this.save(dailyTask);
    }
    
    @Override
    public boolean updateTask(DailyTask dailyTask) {
        return this.updateById(dailyTask);
    }
    
    @Override
    public boolean removeTask(Long id) {
        return this.removeById(id);
    }
}
