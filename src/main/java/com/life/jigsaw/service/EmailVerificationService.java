package com.life.jigsaw.service;

import com.life.jigsaw.domain.EmailVerificationCode;
import com.life.jigsaw.mapper.EmailVerificationCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * 邮箱验证码服务类
 */
@Service
public class EmailVerificationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailVerificationService.class);
    
    @Autowired
    private EmailVerificationCodeMapper codeMapper;
    
    @Autowired
    private EmailService emailService;
    
    // 验证码有效期（分钟）
    private static final int CODE_EXPIRATION_MINUTES = 5;
    
    // 验证码长度
    private static final int CODE_LENGTH = 6;
    
    /**
     * 生成6位数字验证码
     * @return 6位数字验证码
     */
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
    
    /**
     * 发送验证码到邮箱
     * @param email 邮箱地址
     */
    @Transactional
    public void sendVerificationCode(String email) {
        // 删除该邮箱之前的所有验证码
        codeMapper.deleteByEmail(email);
        
        // 生成新的验证码
        String code = generateVerificationCode();
        
        // 计算过期时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusMinutes(CODE_EXPIRATION_MINUTES);
        
        // 保存验证码
        EmailVerificationCode verificationCode = new EmailVerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setCreateTime(now);
        verificationCode.setExpireTime(expireTime);
        verificationCode.setUsed(false);
        
        codeMapper.insert(verificationCode);
        
        // 发送邮件
        emailService.sendVerificationCode(email, code);
        
        logger.info("已为邮箱 {} 生成并发送验证码", email);
    }
    
    /**
     * 验证验证码
     * @param email 邮箱地址
     * @param code 验证码
     * @return 是否验证成功
     */
    @Transactional
    public boolean verifyCode(String email, String code) {
        // 检查验证码格式
        if (code == null || code.length() != CODE_LENGTH || !code.matches("\\d{6}")) {
            logger.warn("邮箱 {} 验证码格式错误: {}", email, code);
            return false;
        }
        
        // 查询有效的验证码
        LocalDateTime now = LocalDateTime.now();
        EmailVerificationCode verificationCode = codeMapper.findValidCodeByEmailAndCode(email, code, now);
        
        if (verificationCode == null) {
            logger.warn("邮箱 {} 验证码无效或已过期: {}", email, code);
            return false;
        }
        
        // 标记验证码为已使用
        codeMapper.markAsUsed(verificationCode.getId());
        
        logger.info("邮箱 {} 验证码验证成功", email);
        return true;
    }
}