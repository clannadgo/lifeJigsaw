package com.life.jigsaw.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@TableName("life_user")
public class LifeUser extends BaseEntity{

    private String username;
    private String password;
    private String phone;
    private String familyName;
    private String email;
    
    // 邮箱验证相关字段
    private Boolean emailVerified = false; // 邮箱是否已验证
    private String verificationToken; // 邮箱验证令牌
    
    // 权限相关字段
    private Boolean isAdmin = true; // 是否为管理员
}
