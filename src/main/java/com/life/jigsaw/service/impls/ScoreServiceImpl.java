package com.life.jigsaw.service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.life.jigsaw.domain.ScoreRecord;
import com.life.jigsaw.domain.ScoreStatistics;
import com.life.jigsaw.enums.ScoreDirection;
import com.life.jigsaw.mapper.ScoreRecordMapper;
import com.life.jigsaw.mapper.ScoreStatisticsMapper;
import com.life.jigsaw.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * 积分服务实现类
 */
@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreRecordMapper, ScoreRecord> implements ScoreService {

    private static final Logger logger = LoggerFactory.getLogger(ScoreServiceImpl.class);

    @Autowired
    private ScoreRecordMapper scoreRecordMapper;

    @Autowired
    private ScoreStatisticsMapper scoreStatisticsMapper;

    @Override
    @Transactional
    public boolean addScore(Long userId, Integer score, String scoreType, String reason, Long relatedId) {
        if (userId == null || score == null || score <= 0) {
            logger.error("Invalid parameters for adding score: userId={}, score={}", userId, score);
            return false;
        }

        try {
            // 获取或创建当月积分统计
            String currentMonth = YearMonth.now().toString();
            ScoreStatistics statistics = getOrCreateScoreStatistics(userId, currentMonth);

            // 计算剩余积分
            BigDecimal currentScore = statistics.getEndBalance();
            BigDecimal changeScore = BigDecimal.valueOf(score);
            BigDecimal remainingScore = currentScore.add(changeScore);

            // 创建积分记录
            ScoreRecord record = new ScoreRecord();
            record.setUserId(userId);
            record.setMonth(currentMonth);
            record.setChangeScore(changeScore);
            record.setRemainingScore(remainingScore);
            record.setScoreType(scoreType);
            record.setDirection(ScoreDirection.INCREASE);
            record.setReason(reason);
            record.setRelatedId(relatedId);

            // 保存积分记录
            boolean saveResult = scoreRecordMapper.insert(record) > 0;
            if (!saveResult) {
                logger.error("Failed to save score record for user {}", userId);
                return false;
            }

            // 更新积分统计
            statistics.addScore(changeScore);
            statistics.setTaskCompleteCount(statistics.getTaskCompleteCount() + 1);
            boolean updateResult = scoreStatisticsMapper.updateById(statistics) > 0;
            if (!updateResult) {
                logger.error("Failed to update score statistics for user {}", userId);
                return false;
            }

            logger.info("Successfully added {} points to user {}, type: {}, relatedId: {}", 
                        score, userId, scoreType, relatedId);
            return true;
        } catch (Exception e) {
            logger.error("Error adding score for user {}", userId, e);
            throw new RuntimeException("添加积分失败", e);
        }
    }

    @Override
    @Transactional
    public boolean reduceScore(Long userId, Integer score, String reason, Long relatedId) {
        if (userId == null || score == null || score <= 0) {
            logger.error("Invalid parameters for reducing score: userId={}, score={}", userId, score);
            return false;
        }

        try {
            // 获取当月积分统计
            String currentMonth = YearMonth.now().toString();
            ScoreStatistics statistics = getOrCreateScoreStatistics(userId, currentMonth);

            // 检查积分是否足够
            BigDecimal currentScore = statistics.getEndBalance();
            BigDecimal changeScore = BigDecimal.valueOf(score);
            if (currentScore.compareTo(changeScore) < 0) {
                logger.error("Insufficient points for user {}, required: {}, available: {}", 
                            userId, changeScore, currentScore);
                return false;
            }

            // 计算剩余积分
            BigDecimal remainingScore = currentScore.subtract(changeScore);

            // 创建积分记录
            ScoreRecord record = new ScoreRecord();
            record.setUserId(userId);
            record.setMonth(currentMonth);
            record.setChangeScore(changeScore.negate()); // 负数表示减少
            record.setRemainingScore(remainingScore);
            record.setDirection(ScoreDirection.DECREASE);
            record.setReason(reason);
            record.setRelatedId(relatedId);

            // 保存积分记录
            boolean saveResult = scoreRecordMapper.insert(record) > 0;
            if (!saveResult) {
                logger.error("Failed to save score record for user {}", userId);
                return false;
            }

            // 更新积分统计
            statistics.reduceScore(changeScore);
            boolean updateResult = scoreStatisticsMapper.updateById(statistics) > 0;
            if (!updateResult) {
                logger.error("Failed to update score statistics for user {}", userId);
                return false;
            }

            logger.info("Successfully reduced {} points from user {}, reason: {}, relatedId: {}", 
                        score, userId, reason, relatedId);
            return true;
        } catch (Exception e) {
            logger.error("Error reducing score for user {}", userId, e);
            throw new RuntimeException("减少积分失败", e);
        }
    }

    @Override
    public ScoreStatistics getScoreStatistics(Long userId, String month) {
        QueryWrapper<ScoreStatistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("month", month);
        return scoreStatisticsMapper.selectOne(queryWrapper);
    }

    @Override
    public List<ScoreRecord> getScoreRecords(Long userId, String month) {
        QueryWrapper<ScoreRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        if (month != null) {
            queryWrapper.eq("month", month);
        }
        queryWrapper.orderByDesc("create_time");
        return scoreRecordMapper.selectList(queryWrapper);
    }

    @Override
    public BigDecimal getCurrentTotalScore(Long userId) {
        String currentMonth = YearMonth.now().toString();
        ScoreStatistics statistics = getScoreStatistics(userId, currentMonth);
        return statistics != null ? statistics.getEndBalance() : BigDecimal.ZERO;
    }

    /**
     * 获取或创建当月积分统计
     */
    private ScoreStatistics getOrCreateScoreStatistics(Long userId, String month) {
        ScoreStatistics statistics = getScoreStatistics(userId, month);
        if (statistics == null) {
            // 创建新的积分统计记录
            statistics = new ScoreStatistics();
            statistics.setUserId(userId);
            statistics.setMonth(month);
            statistics.setStartBalance(BigDecimal.ZERO);
            statistics.setTotalIncrease(BigDecimal.ZERO);
            statistics.setTotalDecrease(BigDecimal.ZERO);
            statistics.setEndBalance(BigDecimal.ZERO);
            statistics.setChangeCount(0);
            statistics.setSignInCount(0);
            statistics.setTaskCompleteCount(0);
            statistics.setIsSettled(false);
            
            // 设置月初余额为上月末余额
            YearMonth currentMonth = YearMonth.parse(month);
            YearMonth lastMonth = currentMonth.minusMonths(1);
            ScoreStatistics lastMonthStatistics = getScoreStatistics(userId, lastMonth.toString());
            if (lastMonthStatistics != null) {
                statistics.setStartBalance(lastMonthStatistics.getEndBalance());
                statistics.setEndBalance(lastMonthStatistics.getEndBalance());
            }
            
            scoreStatisticsMapper.insert(statistics);
        }
        return statistics;
    }
}