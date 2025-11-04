package com.life.jigsaw.service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.life.jigsaw.domain.ExtendedActivity;
import com.life.jigsaw.mapper.ExtendedActivityMapper;
import com.life.jigsaw.service.ExtendedActivityInterface;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 扩展活动服务实现类
 */
@Service
public class ExtendedActivityImpl extends ServiceImpl<ExtendedActivityMapper, ExtendedActivity> implements ExtendedActivityInterface {
    
    @Override
    public ExtendedActivity getById(Long id) {
        return baseMapper.selectById(id);
    }
    
    @Override
    public List<ExtendedActivity> listAll() {
        return baseMapper.selectList(null);
    }
    
    @Override
    public List<ExtendedActivity> listActiveActivities() {
        QueryWrapper<ExtendedActivity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<ExtendedActivity> listByDateRange(Date startDate, Date endDate) {
        QueryWrapper<ExtendedActivity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("activity_date", startDate, endDate);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public boolean saveActivity(ExtendedActivity extendedActivity) {
        return this.save(extendedActivity);
    }
    
    @Override
    public boolean updateActivity(ExtendedActivity extendedActivity) {
        return this.updateById(extendedActivity);
    }
    
    @Override
    public boolean removeActivity(Long id) {
        return this.removeById(id);
    }
}
