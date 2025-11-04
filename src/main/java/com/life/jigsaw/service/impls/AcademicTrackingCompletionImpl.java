package com.life.jigsaw.service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.life.jigsaw.domain.AcademicTracking;
import com.life.jigsaw.domain.AcademicTrackingCompletion;
import com.life.jigsaw.enums.ScoreType;
import com.life.jigsaw.mapper.AcademicTrackingCompletionMapper;
import com.life.jigsaw.service.AcademicTrackingCompletionInterface;
import com.life.jigsaw.service.AcademicTrackingInterface;
import com.life.jigsaw.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * 学业追踪完成记录服务实现类
 */
@Service
public class AcademicTrackingCompletionImpl extends ServiceImpl<AcademicTrackingCompletionMapper, AcademicTrackingCompletion> implements AcademicTrackingCompletionInterface {

    private static final Logger logger = LoggerFactory.getLogger(AcademicTrackingCompletionImpl.class);

    @Autowired
    private AcademicTrackingInterface academicTrackingService;

    @Autowired
    private ScoreService scoreService;
    
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
    @Transactional
    public boolean saveCompletion(AcademicTrackingCompletion completion) {
        try {
            // 保存完成记录
            boolean saveResult = this.save(completion);
            if (!saveResult) {
                logger.error("Failed to save academic tracking completion for user {}", completion.getUserId());
                return false;
            }

            // 如果项目已完成且有积分奖励，更新积分
            if (completion.getStatus() == 1 && completion.getEarnedScore() != null && completion.getEarnedScore() > 0) {
                // 获取学业追踪项目信息
                AcademicTracking tracking = academicTrackingService.getById(completion.getTrackingId());
                String trackingName = tracking != null ? tracking.getProjectName() : "未知学业项目";
                
                // 更新积分
                boolean scoreResult = scoreService.addScore(
                    completion.getUserId(),
                    completion.getEarnedScore(),
                    ScoreType.ACADEMIC_TRACKING,
                    "完成学业追踪项目：" + trackingName,
                    completion.getTrackingId()
                );
                
                if (!scoreResult) {
                    logger.error("Failed to add score for academic tracking completion, userId: {}, trackingId: {}", 
                                completion.getUserId(), completion.getTrackingId());
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            logger.error("Error saving academic tracking completion", e);
            throw new RuntimeException("保存学业追踪完成记录失败", e);
        }
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
