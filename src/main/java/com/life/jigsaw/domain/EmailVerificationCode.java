package com.life.jigsaw.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 邮箱验证码实体类
 */
@Data
public class EmailVerificationCode {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 邮箱地址
     */
    private String email;
    
    /**
     * 验证码
     */
    private String code;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
    
    /**
     * 是否已使用
     */
    private Boolean used;
}
