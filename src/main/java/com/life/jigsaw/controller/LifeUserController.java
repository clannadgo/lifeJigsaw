package com.life.jigsaw.controller;

import com.life.jigsaw.common.response.Response;
import com.life.jigsaw.common.utils.JwtUtils;
import com.life.jigsaw.controller.req.lifeuser.AddUserQo;
import com.life.jigsaw.controller.req.lifeuser.LoginQo;
import com.life.jigsaw.controller.req.lifeuser.ChangePasswordQo;
import com.life.jigsaw.controller.req.lifeuser.UpdateUserQo;
import com.life.jigsaw.domain.LifeUser;
import com.life.jigsaw.service.LifeUserInterface;
import com.life.jigsaw.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * user controller
 *
 */
@RestController
@Tag(name = "用户管理接口")
@Validated
public class LifeUserController {
    private static final Logger logger = LoggerFactory.getLogger(LifeUserController.class);
    
    @Resource
    private LifeUserInterface service;
    
    @Resource
    private EmailVerificationService emailVerificationService;
    
    @Resource
    private JwtUtils jwtUtils;

    /**
     * 发送邮箱验证码
     */
    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/user/sendEmailCode")
    Response<String> sendEmailCode(@RequestBody Map<String, String> requestBody){
        String email = requestBody.get("email");
        if (email == null || email.trim().isEmpty()) {
            return Response.error("邮箱不能为空");
        }
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return Response.error("邮箱格式不正确");
        }
        
        // 校验邮箱是否已注册
        LifeUser existingUser = service.findByEmail(email);
        if (existingUser != null) {
            return Response.error("该邮箱已被注册");
        }
        
        try {
            emailVerificationService.sendVerificationCode(email);
            return Response.success("验证码已发送，请查收邮件");
        } catch (Exception e) {
            logger.error("发送验证码失败: {}", e.getMessage());
            return Response.error("发送验证码失败，请稍后重试");
        }
    }

    /**
     * 新增用户
     */
    @Operation(summary = "新增用户")
    @PostMapping("/user/add")
    Response<Map<String, Object>> addUser(@RequestBody @Validated AddUserQo qo){
        try {
            Integer result = service.addUser(qo);
            if (result > 0) {
                // 查询新注册的用户信息
                LifeUser user = service.findByUsername(qo.getUsername());
                
                // 生成JWT token
                String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getFamilyName(), user.getIsAdmin());
                
                // 构建响应数据，确保isAdmin字段被正确包含
                Map<String, Object> data = new HashMap<>();
                data.put("token", token);
                
                // 创建一个包含所有必要信息的Map
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", user.getId());
                userInfo.put("username", user.getUsername());
                userInfo.put("familyName", user.getFamilyName());
                userInfo.put("isAdmin", user.getIsAdmin() != null ? user.getIsAdmin() : false);
                userInfo.put("emailVerified", user.getEmailVerified());
                
                data.put("user", userInfo);
                
                return Response.success(data, "注册成功");
            } else {
                return Response.error("用户名或家庭名称已存在");
            }
        } catch (Exception e) {
            logger.error("注册失败: {}", e.getMessage());
            return Response.error(e.getMessage());
        }
    }
    
    /**
     * 验证邮箱
     */
    @Operation(summary = "邮箱验证")
    @GetMapping("/user/verify-email")
    Response<String> verifyEmail(@RequestParam String token) {
        try {
            String result = service.verifyEmail(token);
            return Response.success(result);
        } catch (RuntimeException e) {
            logger.error("邮箱验证失败: {}", e.getMessage());
            return Response.error(e.getMessage());
        } catch (Exception e) {
            logger.error("邮箱验证失败: {}", e.getMessage());
            return Response.error("邮箱验证失败，请稍后重试");
        }
    }
    
    /**
     * 用户登录
     */
    @Operation(summary = "用户登录")
    @PostMapping("/user/login")
    Response<Map<String, Object>> login(@RequestBody @Validated LoginQo loginQo){
        LifeUser user = service.login(loginQo);
        if (user != null) {
            // 生成JWT token
            String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getFamilyName(), user.getIsAdmin());
            
            // 构建响应数据，确保isAdmin字段被正确包含
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            
            // 创建一个包含所有必要信息的Map
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("familyName", user.getFamilyName());
            userInfo.put("isAdmin", user.getIsAdmin() != null ? user.getIsAdmin() : false);
            userInfo.put("emailVerified", user.getEmailVerified());
            
            data.put("user", userInfo);
            
            return Response.success(data);
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
    
    /**
     * 修改用户信息
     */
    @Operation(summary = "修改用户信息")
    @PostMapping("/user/update")
    Response<Boolean> updateUser(@RequestBody @Validated UpdateUserQo updateUserQo){
        boolean result = service.updateUser(updateUserQo);
        if (result) {
            return Response.success(true);
        } else {
            return Response.error("用户不存在或用户名已被使用");
        }
    }
}
