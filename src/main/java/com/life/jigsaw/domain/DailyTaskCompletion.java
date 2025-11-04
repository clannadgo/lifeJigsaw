package com.life.jigsaw.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 用户每日任务完成记录
 * 记录用户完成每日任务的情况和获得的积分
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@TableName("daily_task_completion")
@Table(name = "daily_task_completion")
public class DailyTaskCompletion extends BaseEntity {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 任务ID
     */
    private Long taskId;
    
    /**
     * 获得的积分
     */
    private Integer earnedScore;
    
    /**
     * 完成日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date completionDate;
    
    /**
     * 完成状态：0-未完成，1-已完成
     */
    private Integer status = 1;
    
    /**
     * 备注信息
     */
    private String remark;
}
