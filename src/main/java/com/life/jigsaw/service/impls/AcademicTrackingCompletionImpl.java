package com.life.jigsaw.service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.life.jigsaw.domain.AcademicTrackingCompletion;
import com.life.jigsaw.mapper.AcademicTrackingCompletionMapper;
import com.life.jigsaw.service.AcademicTrackingCompletionInterface;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 学业追踪完成记录服务实现类
 */
@Service
public class AcademicTrackingCompletionImpl extends ServiceImpl<AcademicTrackingCompletionMapper, AcademicTrackingCompletion> implements AcademicTrackingCompletionInterface {
    
    @Override
    public AcademicTrackingCompletion getById(Long id) {
        return baseMapper.selectById(id);
    }
    
    @Override
    public List<AcademicTrackingCompletion> listByUserId(Long userId) {
        QueryWrapper<AcademicTrackingCompletion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<AcademicTrackingCompletion> listByTrackingId(Long trackingId) {
        QueryWrapper<AcademicTrackingCompletion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tracking_id", trackingId);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<AcademicTrackingCompletion> listByUserAndTracking(Long userId, Long trackingId) {
        QueryWrapper<AcademicTrackingCompletion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("tracking_id", trackingId);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<AcademicTrackingCompletion> listByUserAndDateRange(Long userId, Date startDate, Date endDate) {
        QueryWrapper<AcademicTrackingCompletion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .between("completion_time", startDate, endDate);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public boolean saveCompletion(AcademicTrackingCompletion completion) {
        return this.save(completion);
    }
    
    @Override
    public boolean updateCompletion(AcademicTrackingCompletion completion) {
        return this.updateById(completion);
    }
    
    @Override
    public boolean removeCompletion(Long id) {
        return this.removeById(id);
    }
}
