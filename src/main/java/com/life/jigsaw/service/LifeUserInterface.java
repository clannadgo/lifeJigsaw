package com.life.jigsaw.service;

import com.life.jigsaw.controller.req.lifeuser.AddUserQo;
import com.life.jigsaw.controller.req.lifeuser.LoginQo;
import com.life.jigsaw.controller.req.lifeuser.ChangePasswordQo;
import com.life.jigsaw.controller.req.lifeuser.UpdateUserQo;
import com.life.jigsaw.domain.LifeUser;

public interface LifeUserInterface {

    Integer addUser(AddUserQo qo);
    
    /**
     * 用户登录验证
     * @param loginQo 登录请求参数
     * @return 登录成功返回用户信息，失败返回null
     */
    LifeUser login(LoginQo loginQo);
    
    /**
     * 修改用户密码
     * @param changePasswordQo 修改密码请求参数
     * @return 修改成功返回true，失败返回false
     */
    boolean changePassword(ChangePasswordQo changePasswordQo);
    
    /**
     * 修改用户信息
     * @param updateUserQo 修改用户信息请求参数
     * @return 修改成功返回true，失败返回false
     */
    boolean updateUser(UpdateUserQo updateUserQo);
    
    /**
     * 验证邮箱
     * @param token 验证令牌
     * @return 验证结果消息
     * @throws RuntimeException 验证失败时抛出异常
     */
    String verifyEmail(String token);
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    LifeUser findByUsername(String username);
    
    /**
     * 根据邮箱查询用户
     * @param email 邮箱地址
     * @return 用户信息，如果不存在返回null
     */
    LifeUser findByEmail(String email);
    
    /**
     * 根据用户ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    LifeUser findById(Long id);
}
