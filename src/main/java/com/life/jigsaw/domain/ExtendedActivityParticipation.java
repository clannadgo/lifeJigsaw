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
 * 用户扩展活动参与记录
 * 记录用户参与扩展活动的情况和获得的积分
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@TableName("extended_activity_participation")
@Table(name = "extended_activity_participation")
public class ExtendedActivityParticipation extends BaseEntity {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 活动ID
     */
    private Long activityId;
    
    /**
     * 获得的积分
     */
    private Integer earnedScore;
    
    /**
     * 参与时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date participationTime;
    
    /**
     * 参与状态：0-未完成，1-已完成
     */
    private Integer status = 0;
    
    /**
     * 备注信息
     */
    private String remark;
}
