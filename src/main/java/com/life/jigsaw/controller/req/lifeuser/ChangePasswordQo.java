package com.life.jigsaw.controller.req.lifeuser;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 修改密码请求参数
 */
@Data
public class ChangePasswordQo {
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    
    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}