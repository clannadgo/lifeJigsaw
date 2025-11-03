package com.life.jigsaw.controller.req.lifeuser;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddUserQo {

    @Schema(description = "用户名")
    @NotNull(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度在6-20之间")
    private String username;

    @Schema(description = "密码")
    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度在6-20之间")
    private String password;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    @NotNull(message = "邮箱不能为空")
    private String email;

    @Schema(description = "家庭昵称")
    private String familyName;
}
