package com.life.jigsaw.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 邮箱验证码实体类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@TableName("email_verification_code")
public class EmailVerificationCode extends BaseEntity{

    private String email;
    private String code;
    private LocalDateTime expireTime;
    private Boolean used;
}
