package com.life.jigsaw.service.impls;

import com.life.jigsaw.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件服务实现类
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${server.port}")
    private String port;

    @Value("${application.name:life_jigsaw}")
    private String applicationName;

    @Override
    public void sendVerificationEmail(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject("【" + applicationName + "】邮箱验证");
            
            // 构建验证链接（这里使用本地地址，实际应用中应该配置域名）
            String verificationUrl = "http://localhost:" + port + "/api/puzzle/v2/user/verify-email?token=" + token;
            
            String content = "尊敬的用户：\n\n" +
                            "请点击下方链接完成邮箱验证：\n" +
                            verificationUrl + "\n\n" +
                            "如果您没有注册我们的服务，请忽略此邮件。\n\n" +
                            "--\n" + applicationName + " 团队";
            
            message.setText(content);
            mailSender.send(message);
            logger.info("邮箱验证邮件已发送至: {}", to);
        } catch (Exception e) {
            logger.error("发送验证邮件失败: {}, 错误: {}", to, e.getMessage());
            throw new RuntimeException("发送验证邮件失败", e);
        }
    }
    
    @Override
    public void sendVerificationCode(String to, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject("【" + applicationName + "】注册验证码");
            
            String content = "尊敬的用户：\n\n" +
                            "您的注册验证码是：" + code + "\n\n" +
                            "请在5分钟内完成注册，验证码将自动失效。\n\n" +
                            "请勿将验证码泄露给他人。\n\n" +
                            "--\n" + applicationName + " 团队";
            
            message.setText(content);
            mailSender.send(message);
            logger.info("验证码邮件已发送至: {}", to);
        } catch (Exception e) {
            logger.error("发送验证码邮件失败: {}, 错误: {}", to, e.getMessage());
            throw new RuntimeException("发送验证码邮件失败", e);
        }
    }
}