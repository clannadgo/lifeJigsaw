package com.life.jigsaw.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.YearMonth;

/**
 * 积分统计实体类
 * 按月汇总用户的积分情况
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@TableName("score_statistics")
public class ScoreStatistics extends BaseEntity {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 月份，格式：yyyy-MM
     */
    private String month;
    
    /**
     * 月初积分余额
     */
    private BigDecimal startBalance;
    
    /**
     * 本月增加的总积分
     */
    private BigDecimal totalIncrease;
    
    /**
     * 本月减少的总积分
     */
    private BigDecimal totalDecrease;
    
    /**
     * 月末积分余额
     */
    private BigDecimal endBalance;
    
    /**
     * 本月积分变动次数
     */
    private Integer changeCount;
    
    /**
     * 本月签到次数
     */
    private Integer signInCount;
    
    /**
     * 本月完成任务数
     */
    private Integer taskCompleteCount;
    
    /**
     * 是否已结算
     * true: 已结算，false: 未结算
     */
    private Boolean isSettled = false;
    
    /**
     * 设置月份为当前年月
     */
    public void setCurrentMonth() {
        this.month = YearMonth.now().toString();
    }
    
    /**
     * 更新月末余额
     */
    public void updateEndBalance() {
        this.endBalance = this.startBalance.add(this.totalIncrease).subtract(this.totalDecrease);
    }
    
    /**
     * 增加积分
     * @param score 要增加的积分数量
     */
    public void addScore(BigDecimal score) {
        this.totalIncrease = this.totalIncrease.add(score);
        updateEndBalance();
        this.changeCount++;
    }
    
    /**
     * 减少积分
     * @param score 要减少的积分数量
     */
    public void reduceScore(BigDecimal score) {
        this.totalDecrease = this.totalDecrease.add(score);
        updateEndBalance();
        this.changeCount++;
    }
}
