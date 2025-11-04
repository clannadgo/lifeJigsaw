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
 * 扩展活动实体类
 * 存储各类扩展活动信息和对应的积分奖励
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@TableName("extended_activity")
@Table(name = "extended_activity")
public class ExtendedActivity extends BaseEntity {
    
    /**
     * 活动名称
     */
    private String activityName;
    
    /**
     * 活动描述
     */
    private String description;
    
    /**
     * 积分奖励
     */
    private Integer scoreValue;
    
    /**
     * 活动状态：0-未开始，1-进行中，2-已结束
     */
    private Integer status = 0;
    
    /**
     * 活动开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    
    /**
     * 活动结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    
    /**
     * 每人可参与次数
     */
    private Integer maxParticipations = 1;
    
    /**
     * 活动图标（可选）
     */
    private String icon;
}
