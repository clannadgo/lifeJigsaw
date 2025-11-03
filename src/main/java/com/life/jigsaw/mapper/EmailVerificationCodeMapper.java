package com.life.jigsaw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.life.jigsaw.domain.EmailVerificationCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 邮箱验证码Mapper
 */
@Mapper
public interface EmailVerificationCodeMapper extends BaseMapper<EmailVerificationCode> {
    
    /**
     * 根据邮箱和验证码查询有效验证码
     * @param email 邮箱地址
     * @param code 验证码
     * @param currentTime 当前时间
     * @return 验证码实体
     */
    default EmailVerificationCode findValidCodeByEmailAndCode(String email, String code, java.time.LocalDateTime currentTime) {
        QueryWrapper<EmailVerificationCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email)
                   .eq("code", code)
                   .eq("used", false)
                   .gt("expire_time", currentTime);
        return selectOne(queryWrapper);
    }
    
    /**
     * 更新验证码为已使用
     * @param id 验证码ID
     * @return 更新结果
     */
    default int markAsUsed(Long id) {
        EmailVerificationCode code = new EmailVerificationCode();
        code.setId(id);
        code.setUsed(true);
        return updateById(code);
    }
    
    /**
     * 删除指定邮箱的所有验证码
     * @param email 邮箱地址
     * @return 删除结果
     */
    default int deleteByEmail(String email) {
        QueryWrapper<EmailVerificationCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return delete(queryWrapper);
    }
}
