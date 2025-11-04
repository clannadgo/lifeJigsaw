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
 * 用户学业追踪完成记录
 * 记录用户完成学业追踪项目的情况和获得的积分
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@TableName("academic_tracking_completion")
@Table(name = "academic_tracking_completion")
public class AcademicTrackingCompletion extends BaseEntity {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 学业追踪项目ID
     */
    private Long trackingId;
    
    /**
     * 获得的积分
     */
    private Integer earnedScore;
    
    /**
     * 完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date completionTime;
    
    /**
     * 完成状态：0-未完成，1-已完成
     */
    private Integer status = 0;
    
    /**
     * 完成内容描述或结果
     */
    private String completionContent;
    
    /**
     * 备注信息
     */
    private String remark;
}
