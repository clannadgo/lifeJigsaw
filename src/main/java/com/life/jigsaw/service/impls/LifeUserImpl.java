package com.life.jigsaw.service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.life.jigsaw.common.utils.PasswordUtils;
import com.life.jigsaw.controller.req.lifeuser.AddUserQo;
import com.life.jigsaw.controller.req.lifeuser.LoginQo;
import com.life.jigsaw.controller.req.lifeuser.ChangePasswordQo;
import com.life.jigsaw.controller.req.lifeuser.UpdateUserQo;
import com.life.jigsaw.domain.LifeUser;
import com.life.jigsaw.mapper.LifeUserMapper;
import com.life.jigsaw.service.LifeUserInterface;
import com.life.jigsaw.service.EmailVerificationService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LifeUserImpl implements LifeUserInterface {

    private static final Logger logger = LoggerFactory.getLogger(LifeUserImpl.class);
    
    @Resource
    LifeUserMapper lifeUserMapper;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Transactional
    @Override
    public Integer addUser(AddUserQo qo) {
        // 验证验证码
        if (!emailVerificationService.verifyCode(qo.getEmail(), qo.getVerificationCode())) {
            logger.warn("用户 {} 验证码验证失败", qo.getUsername());
            throw new RuntimeException("验证码错误或已过期");
        }
        
        // 检查用户名是否已存在
        if (findByUsername(qo.getUsername()) != null) {
            return 0; // 用户名已存在
        }
        
        // 检查家庭名称是否已存在
        String familyName = generateFamilyName(qo.getFamilyName(), qo.getUsername());
        QueryWrapper<LifeUser> familyNameWrapper = new QueryWrapper<>();
        familyNameWrapper.eq("family_name", familyName);
        if (lifeUserMapper.selectOne(familyNameWrapper) != null) {
            return 0; // 家庭名称已存在
        }
        
        // 创建用户，邮箱直接设为已验证
        LifeUser user = new LifeUser();
        user.setUsername(qo.getUsername());
        // 使用PasswordUtils对密码进行加密
        user.setPassword(PasswordUtils.encode(qo.getPassword()));
        user.setEmail(qo.getEmail());
        user.setPhone(qo.getPhone());
        user.setFamilyName(familyName);
        user.setEmailVerified(true); // 验证码验证成功，直接设为已验证
        
        int result = lifeUserMapper.insert(user);
        
        if (result > 0) {
            logger.info("用户 {} 注册成功", qo.getUsername());
        }
        
        return result;
    }

    @Override
    public LifeUser login(LoginQo loginQo) {
        // 根据用户名或家庭名称查询用户
        QueryWrapper<LifeUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginQo.getUsername()).or().eq("family_name", loginQo.getUsername());
        LifeUser user = lifeUserMapper.selectOne(queryWrapper);
        
        // 如果用户不存在，返回null
        if (user == null) {
            return null;
        }
        
        // 检查邮箱是否已验证
        if (!user.getEmailVerified()) {
            logger.info("用户 {} 尝试登录，但邮箱未验证", loginQo.getUsername());
            throw new RuntimeException("请先验证您的邮箱后再登录");
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
        LifeUser user = findByUsername(changePasswordQo.getUsername());
        
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
            LifeUser existingUser = findByUsername(updateUserQo.getUsername());
            if (existingUser != null) {
                // 用户名已存在，返回false
                return false;
            }
        }
        
        // 生成新的家庭名称
        String newFamilyName = generateFamilyName(updateUserQo.getFamilyName(), updateUserQo.getUsername());
        
        // 检查家庭名称是否重复（如果家庭名称变更了）
        if (!user.getFamilyName().equals(newFamilyName)) {
            QueryWrapper<LifeUser> familyNameWrapper = new QueryWrapper<>();
            familyNameWrapper.eq("family_name", newFamilyName);
            LifeUser existingUser = lifeUserMapper.selectOne(familyNameWrapper);
            if (existingUser != null) {
                // 家庭名称已存在，返回false
                return false;
            }
        }
        
        // 更新用户信息
        user.setUsername(updateUserQo.getUsername());
        user.setPhone(updateUserQo.getPhone());
        user.setFamilyName(newFamilyName);
        user.setEmail(updateUserQo.getEmail());
        
        // 执行更新操作
        int result = lifeUserMapper.updateById(user);
        
        // 返回更新是否成功
        return result > 0;
    }
    
    @Transactional
    @Override
    public String verifyEmail(String token) {
        // 根据令牌查找用户
        QueryWrapper<LifeUser> wrapper = new QueryWrapper<>();
        wrapper.eq("verification_token", token);
        LifeUser user = lifeUserMapper.selectOne(wrapper);
        
        if (user == null) {
            logger.warn("无效的验证令牌: {}", token);
            throw new RuntimeException("无效的验证链接，请重新注册");
        }
        
        if (user.getEmailVerified()) {
            logger.info("邮箱 {} 已经验证过", user.getEmail());
            return "邮箱已经验证过";
        }
        
        // 更新用户状态为已验证
        user.setEmailVerified(true);
        user.setVerificationToken(null); // 验证后清除令牌
        lifeUserMapper.updateById(user);
        
        logger.info("用户 {} 的邮箱 {} 验证成功", user.getUsername(), user.getEmail());
        return "邮箱验证成功，您现在可以登录了";
    }
    
    @Override
    public LifeUser findByUsername(String username) {
        QueryWrapper<LifeUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return lifeUserMapper.selectOne(wrapper);
    }
    
    @Override
    public LifeUser findByEmail(String email) {
        QueryWrapper<LifeUser> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        return lifeUserMapper.selectOne(wrapper);
    }
    
    @Override
    public LifeUser findById(Long id) {
        return lifeUserMapper.selectById(id);
    }
}
