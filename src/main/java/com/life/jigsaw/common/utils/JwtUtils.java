package com.life.jigsaw.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 提供JWT token的生成和验证功能
 */
@Component
public class JwtUtils {

    // JWT过期时间（24小时）
    private final long expireTime = 24 * 60 * 60 * 1000;
    
    // 密钥实例变量
    private final SecretKey secretKey;
    
    // 构造函数初始化密钥
    public JwtUtils() {
        // 使用Keys.secretKeyFor方法生成符合HS512算法要求的安全密钥（至少512位）
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
    
    /**
     * 生成JWT token
     * @param userId 用户ID
     * @param username 用户名
     * @param familyName 家庭名称
     * @param isAdmin 是否为管理员
     * @return JWT token
     */
    public String generateToken(Long userId, String username, String familyName, Boolean isAdmin) {
        // 创建JWT声明
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("familyName", familyName);
        claims.put("isAdmin", isAdmin != null ? isAdmin : false);
        
        // 生成JWT token
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireTime);
        
        return Jwts.builder()
                // Using individual claim methods instead of setClaims and deprecated methods
                .claim("userId", userId)
                .claim("username", username)
                .claim("familyName", familyName)
                .claim("isAdmin", isAdmin != null ? isAdmin : false)
                .claim("iat", now) // Issued at time
                .claim("exp", expireDate) // Expiration time
                .signWith(secretKey)
                .compact();
    }
    
    /**
     * 解析JWT token
     * @param token JWT token
     * @return 解析后的声明
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                // Using the modern method to set signing key
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * 验证JWT token是否有效
     * @param token JWT token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 从JWT token中获取用户名
     * @param token JWT token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }
    
    /**
     * 从JWT token中获取用户ID
     * @param token JWT token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }
    
    /**
     * 从JWT token中获取家庭名称
     * @param token JWT token
     * @return 家庭名称
     */
    public String getFamilyNameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("familyName", String.class);
    }
    
    /**
     * 从JWT token中获取是否为管理员
     * @param token JWT token
     * @return 是否为管理员
     */
    public Boolean getIsAdminFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("isAdmin", Boolean.class) != null ? claims.get("isAdmin", Boolean.class) : false;
    }
}