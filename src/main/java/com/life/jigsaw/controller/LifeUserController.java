package com.life.jigsaw.controller;

import com.life.jigsaw.common.response.Response;
import com.life.jigsaw.common.utils.PasswordUtils;
import com.life.jigsaw.controller.req.lifeuser.AddUserQo;
import com.life.jigsaw.controller.req.lifeuser.LoginQo;
import com.life.jigsaw.controller.req.lifeuser.ChangePasswordQo;
import com.life.jigsaw.domain.LifeUser;
import com.life.jigsaw.service.interfaces.LifeUserInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * user controller
 *
 */
@RestController
@Tag(name = "用户管理接口")
public class LifeUserController {
    @Resource
    private LifeUserInterface service;

    /**
     * 新增用户
     */
    @Operation(summary = "新增用户")
    @PostMapping("/user/add")
    Response<Integer> addUser(@RequestBody @Validated AddUserQo qo){
        return Response.success(service.addUser(qo));
    }
    
    /**
     * 用户登录
     */
    @Operation(summary = "用户登录")
    @PostMapping("/user/login")
    Response<LifeUser> login(@RequestBody @Validated LoginQo loginQo){
        LifeUser user = service.login(loginQo);
        if (user != null) {
            return Response.success(user);
        } else {
            return Response.error("用户名或密码错误");
        }
    }
    
    /**
     * 修改密码
     */
    @Operation(summary = "修改密码")
    @PostMapping("/user/changePassword")
    Response<Boolean> changePassword(@RequestBody @Validated ChangePasswordQo changePasswordQo){
        boolean result = service.changePassword(changePasswordQo);
        if (result) {
            return Response.success(true);
        } else {
            return Response.error("用户名不存在或旧密码错误");
        }
    }
}
