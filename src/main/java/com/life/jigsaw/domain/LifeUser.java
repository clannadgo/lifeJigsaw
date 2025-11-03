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
}
