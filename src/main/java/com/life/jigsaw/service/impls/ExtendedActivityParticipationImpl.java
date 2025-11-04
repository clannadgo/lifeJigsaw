package com.life.jigsaw.service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.life.jigsaw.domain.ExtendedActivityParticipation;
import com.life.jigsaw.mapper.ExtendedActivityParticipationMapper;
import com.life.jigsaw.service.ExtendedActivityParticipationInterface;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 扩展活动参与记录服务实现类
 */
@Service
public class ExtendedActivityParticipationImpl extends ServiceImpl<ExtendedActivityParticipationMapper, ExtendedActivityParticipation> implements ExtendedActivityParticipationInterface {
    
    @Override
    public ExtendedActivityParticipation getById(Long id) {
        return baseMapper.selectById(id);
    }
    
    @Override
    public List<ExtendedActivityParticipation> listByUserId(Long userId) {
        QueryWrapper<ExtendedActivityParticipation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<ExtendedActivityParticipation> listByActivityId(Long activityId) {
        QueryWrapper<ExtendedActivityParticipation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<ExtendedActivityParticipation> listByUserAndActivity(Long userId, Long activityId) {
        QueryWrapper<ExtendedActivityParticipation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("activity_id", activityId);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<ExtendedActivityParticipation> listByUserAndDateRange(Long userId, Date startDate, Date endDate) {
        QueryWrapper<ExtendedActivityParticipation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .between("participation_time", startDate, endDate);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public boolean saveParticipation(ExtendedActivityParticipation participation) {
        return this.save(participation);
    }
    
    @Override
    public boolean updateParticipation(ExtendedActivityParticipation participation) {
        return this.updateById(participation);
    }
    
    @Override
    public boolean removeParticipation(Long id) {
        return this.removeById(id);
    }
}
