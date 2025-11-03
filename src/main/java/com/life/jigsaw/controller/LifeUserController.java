package com.life.jigsaw.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.life.jigsaw.common.response.Response;
import com.life.jigsaw.common.utils.JwtUtils;
import com.life.jigsaw.controller.req.lifeuser.AddUserQo;
import com.life.jigsaw.controller.req.lifeuser.LoginQo;
import com.life.jigsaw.controller.req.lifeuser.ChangePasswordQo;
import com.life.jigsaw.controller.req.lifeuser.UpdateUserQo;
import com.life.jigsaw.domain.LifeUser;
import com.life.jigsaw.mapper.LifeUserMapper;
import com.life.jigsaw.service.LifeUserInterface;
import com.life.jigsaw.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    private LifeUserMapper lifeUserMapper;
    
    @Resource
    private EmailVerificationService emailVerificationService;

    /**
     * 发送邮箱验证码
     */
    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/user/sendEmailCode")
    Response<String> sendEmailCode(@RequestParam @NotBlank(message = "邮箱不能为空") @Email(message = "邮箱格式不正确") String email){
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
                LifeUser user = lifeUserMapper.selectOne(new QueryWrapper<LifeUser>()
                        .eq("username", qo.getUsername()));
                
                // 生成JWT token
                String token = JwtUtils.generateToken(user.getId(), user.getUsername(), user.getFamilyName());
                
                // 构建响应数据
                Map<String, Object> data = new HashMap<>();
                data.put("token", token);
                data.put("user", user);
                
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
            // 根据令牌查找用户
            QueryWrapper<LifeUser> wrapper = new QueryWrapper<>();
            wrapper.eq("verification_token", token);
            LifeUser user = lifeUserMapper.selectOne(wrapper);
            
            if (user == null) {
                logger.warn("无效的验证令牌: {}", token);
                return Response.error("无效的验证链接，请重新注册");
            }
            
            if (user.getEmailVerified()) {
                logger.info("邮箱 {} 已经验证过", user.getEmail());
                return Response.success("邮箱已经验证过");
            }
            
            // 更新用户状态为已验证
            user.setEmailVerified(true);
            user.setVerificationToken(null); // 验证后清除令牌
            lifeUserMapper.updateById(user);
            
            logger.info("用户 {} 的邮箱 {} 验证成功", user.getUsername(), user.getEmail());
            return Response.success("邮箱验证成功，您现在可以登录了");
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
            String token = JwtUtils.generateToken(user.getId(), user.getUsername(), user.getFamilyName());
            
            // 构建响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", user);
            
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
