package com.life.jigsaw.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.YearMonth;

/**
 * 积分记录实体类
 * 记录用户的积分变动情况，支持按月份统计
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@TableName("score_record")
public class ScoreRecord extends BaseEntity {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 月份，格式：yyyy-MM，用于统计
     */
    private String month;
    
    /**
     * 积分变动数量
     * 正数表示增加，负数表示减少
     */
    private BigDecimal changeScore;
    
    /**
     * 变动后剩余积分
     */
    private BigDecimal remainingScore;
    
    /**
     * 积分类型
     * 关联ScoreType枚举
     */
    private String scoreType;
    
    /**
     * 变动方向
     * 1: 增加，-1: 减少
     * 关联ScoreDirection枚举
     */
    private Integer direction;
    
    /**
     * 变动原因描述
     */
    private String reason;
    
    /**
     * 关联的业务ID（如任务ID、活动ID等）
     * 可选字段
     */
    private Long relatedId;
    
    /**
     * 备注信息
     */
    private String remark;
    
    /**
     * 设置月份为当前年月
     * 可以在保存前调用此方法
     */
    public void setCurrentMonth() {
        this.month = YearMonth.now().toString();
    }
}
