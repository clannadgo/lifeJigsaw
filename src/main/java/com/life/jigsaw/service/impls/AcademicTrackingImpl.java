package com.life.jigsaw.service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.life.jigsaw.domain.AcademicTracking;
import com.life.jigsaw.mapper.AcademicTrackingMapper;
import com.life.jigsaw.service.AcademicTrackingInterface;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 学业追踪服务实现类
 */
@Service
public class AcademicTrackingImpl extends ServiceImpl<AcademicTrackingMapper, AcademicTracking> implements AcademicTrackingInterface {
    
    @Override
    public AcademicTracking getById(Long id) {
        return baseMapper.selectById(id);
    }
    
    @Override
    public List<AcademicTracking> listAll() {
        return baseMapper.selectList(null);
    }
    
    @Override
    public List<AcademicTracking> listActiveTrackings() {
        QueryWrapper<AcademicTracking> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<AcademicTracking> listByType(String trackingType) {
        QueryWrapper<AcademicTracking> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tracking_type", trackingType);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<AcademicTracking> listByDeadlineBefore(Date deadline) {
        QueryWrapper<AcademicTracking> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("deadline", deadline);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public boolean saveTracking(AcademicTracking academicTracking) {
        return this.save(academicTracking);
    }
    
    @Override
    public boolean updateTracking(AcademicTracking academicTracking) {
        return this.updateById(academicTracking);
    }
    
    @Override
    public boolean removeTracking(Long id) {
        return this.removeById(id);
    }
}
