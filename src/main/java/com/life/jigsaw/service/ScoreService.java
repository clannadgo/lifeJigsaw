package com.life.jigsaw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.life.jigsaw.domain.ScoreRecord;
import com.life.jigsaw.domain.ScoreStatistics;

import java.math.BigDecimal;
import java.util.List;

/**
 * 积分服务接口
 * 处理用户积分的增加、减少和统计
 */
public interface ScoreService extends IService<ScoreRecord> {

    /**
     * 为用户增加积分
     * @param userId 用户ID
     * @param score 积分数量
     * @param scoreType 积分类型（DAILY_TASK, EXTENDED_ACTIVITY, ACADEMIC_TRACKING）
     * @param reason 积分变动原因
     * @param relatedId 关联业务ID
     * @return 是否增加成功
     */
    boolean addScore(Long userId, Integer score, String scoreType, String reason, Long relatedId);

    /**
     * 为用户减少积分
     * @param userId 用户ID
     * @param score 积分数量
     * @param reason 积分变动原因
     * @param relatedId 关联业务ID
     * @return 是否减少成功
     */
    boolean reduceScore(Long userId, Integer score, String reason, Long relatedId);

    /**
     * 获取用户的积分统计信息
     * @param userId 用户ID
     * @param month 月份，格式：yyyy-MM
     * @return 积分统计对象
     */
    ScoreStatistics getScoreStatistics(Long userId, String month);

    /**
     * 获取用户的积分记录
     * @param userId 用户ID
     * @param month 月份，格式：yyyy-MM，可选参数
     * @return 积分记录列表
     */
    List<ScoreRecord> getScoreRecords(Long userId, String month);

    /**
     * 获取用户当前总积分
     * @param userId 用户ID
     * @return 当前总积分
     */
    BigDecimal getCurrentTotalScore(Long userId);
}