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
 * 学业追踪实体类
 * 存储学业相关的追踪项目和对应的积分奖励
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@TableName("academic_tracking")
@Table(name = "academic_tracking")
public class AcademicTracking extends BaseEntity {
    
    /**
     * 项目名称
     */
    private String projectName;
    
    /**
     * 项目描述
     */
    private String description;
    
    /**
     * 积分奖励
     */
    private Integer scoreValue;
    
    /**
     * 项目状态：0-未启用，1-已启用
     */
    private Integer status = 1;
    
    /**
     * 追踪类型：如考试、作业、阅读等
     */
    private String trackingType;
    
    /**
     * 截止日期（可选）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deadline;
    
    /**
     * 是否可重复完成
     */
    private Boolean repeatable = false;
    
    /**
     * 项目图标（可选）
     */
    private String icon;
}
