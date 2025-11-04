package com.life.jigsaw.service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.life.jigsaw.domain.ExtendedActivity;
import com.life.jigsaw.domain.ExtendedActivityParticipation;
import com.life.jigsaw.enums.ScoreType;
import com.life.jigsaw.mapper.ExtendedActivityParticipationMapper;
import com.life.jigsaw.service.ExtendedActivityInterface;
import com.life.jigsaw.service.ExtendedActivityParticipationInterface;
import com.life.jigsaw.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * 扩展活动参与记录服务实现类
 */
@Service
public class ExtendedActivityParticipationImpl extends ServiceImpl<ExtendedActivityParticipationMapper, ExtendedActivityParticipation> implements ExtendedActivityParticipationInterface {

    private static final Logger logger = LoggerFactory.getLogger(ExtendedActivityParticipationImpl.class);

    @Autowired
    private ExtendedActivityInterface extendedActivityService;

    @Autowired
    private ScoreService scoreService;
    
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
    @Transactional
    public boolean saveParticipation(ExtendedActivityParticipation participation) {
        try {
            // 保存参与记录
            boolean saveResult = this.save(participation);
            if (!saveResult) {
                logger.error("Failed to save extended activity participation for user {}", participation.getUserId());
                return false;
            }

            // 如果活动参与有积分奖励，更新积分
            if (participation.getEarnedScore() != null && participation.getEarnedScore() > 0) {
                // 获取扩展活动信息
                ExtendedActivity activity = extendedActivityService.getById(participation.getActivityId());
                String activityName = activity != null ? activity.getActivityName() : "未知活动";
                
                // 更新积分
                boolean scoreResult = scoreService.addScore(
                    participation.getUserId(),
                    participation.getEarnedScore(),
                    ScoreType.EXTENDED_ACTIVITY,
                    "参与扩展活动：" + activityName,
                    participation.getActivityId()
                );
                
                if (!scoreResult) {
                    logger.error("Failed to add score for extended activity participation, userId: {}, activityId: {}", 
                                participation.getUserId(), participation.getActivityId());
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            logger.error("Error saving extended activity participation", e);
            throw new RuntimeException("保存扩展活动参与记录失败", e);
        }
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
