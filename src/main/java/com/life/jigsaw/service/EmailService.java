package com.life.jigsaw.service;

/**
 * 邮件服务接口
 */
public interface EmailService {

    /**
     * 发送邮箱验证邮件
     * @param to 收件人邮箱
     * @param token 验证令牌
     */
    void sendVerificationEmail(String to, String token);
    
    /**
     * 发送验证码邮件
     * @param to 收件人邮箱
     * @param code 验证码
     */
    void sendVerificationCode(String to, String code);
}