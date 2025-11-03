package com.life.jigsaw.controller.req.lifeuser;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 修改用户信息请求参数
 */
@Data
public class UpdateUserQo implements Serializable {
    
    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    private String id;
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    /**
     * 电话
     */
    private String phone;
    
    /**
     * 家庭名称
     */
    private String familyName;
    
    /**
     * 邮箱
     */
    private String email;
}