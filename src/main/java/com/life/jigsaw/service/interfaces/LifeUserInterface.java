package com.life.jigsaw.service.interfaces;

import com.life.jigsaw.controller.req.lifeuser.AddUserQo;
import com.life.jigsaw.controller.req.lifeuser.LoginQo;
import com.life.jigsaw.controller.req.lifeuser.ChangePasswordQo;
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
}
