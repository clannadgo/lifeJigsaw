package com.life.jigsaw.service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.life.jigsaw.common.utils.PasswordUtils;
import com.life.jigsaw.controller.req.lifeuser.AddUserQo;
import com.life.jigsaw.controller.req.lifeuser.LoginQo;
import com.life.jigsaw.controller.req.lifeuser.ChangePasswordQo;
import com.life.jigsaw.controller.req.lifeuser.UpdateUserQo;
import com.life.jigsaw.domain.LifeUser;
import com.life.jigsaw.mapper.LifeUserMapper;
import com.life.jigsaw.service.interfaces.LifeUserInterface;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LifeUserImpl implements LifeUserInterface {

    @Resource
    LifeUserMapper lifeUserMapper;

    @Override
    public Integer addUser(AddUserQo qo) {
        LifeUser user = new LifeUser();
        user.setUsername(qo.getUsername());
        // 使用PasswordUtils对密码进行加密
        user.setPassword(PasswordUtils.encode(qo.getPassword()));
        user.setEmail(qo.getEmail());
        user.setPhone(qo.getPhone());
        user.setFamilyName(generateFamilyName(qo.getFamilyName(), qo.getUsername()));
        return lifeUserMapper.insert(user);
    }

    @Override
    public LifeUser login(LoginQo loginQo) {
        // 根据用户名查询用户
        QueryWrapper<LifeUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginQo.getUsername());
        LifeUser user = lifeUserMapper.selectOne(queryWrapper);
        
        // 如果用户不存在，返回null
        if (user == null) {
            return null;
        }
        
        // 使用PasswordUtils验证密码
        if (PasswordUtils.matches(loginQo.getPassword(), user.getPassword())) {
            // 登录成功，返回用户信息（注意：这里可以考虑不返回密码字段）
            user.setPassword(null); // 清空密码，避免返回给前端
            return user;
        }
        
        // 密码不匹配，返回null
        return null;
    }

    private String generateFamilyName(String familyName, String username){
        if(null != familyName){
            return familyName;
        }
        return username + "的家";
    }
    
    @Override
    public boolean changePassword(ChangePasswordQo changePasswordQo) {
        // 根据用户名查询用户
        QueryWrapper<LifeUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", changePasswordQo.getUsername());
        LifeUser user = lifeUserMapper.selectOne(queryWrapper);
        
        // 如果用户不存在，返回false
        if (user == null) {
            return false;
        }
        
        // 验证旧密码是否正确
        if (!PasswordUtils.matches(changePasswordQo.getOldPassword(), user.getPassword())) {
            // 旧密码不正确，返回false
            return false;
        }
        
        // 使用PasswordUtils对新密码进行加密
        String encryptedNewPassword = PasswordUtils.encode(changePasswordQo.getNewPassword());
        
        // 更新用户密码
        user.setPassword(encryptedNewPassword);
        int result = lifeUserMapper.updateById(user);
        
        // 返回更新是否成功
        return result > 0;
    }
    
    @Override
    public boolean updateUser(UpdateUserQo updateUserQo) {
        // 根据用户ID查询用户
        LifeUser user = lifeUserMapper.selectById(updateUserQo.getId());
        
        // 如果用户不存在，返回false
        if (user == null) {
            return false;
        }
        
        // 检查用户名是否重复（如果用户名变更了）
        if (!user.getUsername().equals(updateUserQo.getUsername())) {
            QueryWrapper<LifeUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", updateUserQo.getUsername());
            LifeUser existingUser = lifeUserMapper.selectOne(queryWrapper);
            if (existingUser != null) {
                // 用户名已存在，返回false
                return false;
            }
        }
        
        // 更新用户信息
        user.setUsername(updateUserQo.getUsername());
        user.setPhone(updateUserQo.getPhone());
        user.setFamilyName(generateFamilyName(updateUserQo.getFamilyName(), updateUserQo.getUsername()));
        user.setEmail(updateUserQo.getEmail());
        
        // 执行更新操作
        int result = lifeUserMapper.updateById(user);
        
        // 返回更新是否成功
        return result > 0;
    }
}
