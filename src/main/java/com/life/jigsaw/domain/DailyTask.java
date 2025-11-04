package com.life.jigsaw.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 每日任务实体类
 * 存储每日可完成的任务信息和对应的积分奖励
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@TableName("daily_task")
@Table(name = "daily_task")
public class DailyTask extends BaseEntity {
    
    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 任务描述
     */
    private String description;
    
    /**
     * 积分奖励
     */
    private Integer scoreValue;
    
    /**
     * 任务状态：0-未启用，1-已启用
     */
    private Integer status = 1;
    
    /**
     * 每日可完成次数
     */
    private Integer maxDailyCompletions = 1;
    
    /**
     * 任务图标（可选）
     */
    private String icon;
}
