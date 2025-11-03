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
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;
    
    // 使用Keys.secretKeyFor方法生成符合HS512算法要求的安全密钥（至少512位）
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    
    /**
     * 生成JWT token
     * @param userId 用户ID
     * @param username 用户名
     * @param familyName 家庭名称
     * @return JWT token
     */
    public static String generateToken(Long userId, String username, String familyName) {
        // 创建JWT声明
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("familyName", familyName);
        
        // 生成JWT token
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + EXPIRE_TIME);
        
        // 直接使用预先生成的安全密钥
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * 解析JWT token
     * @param token JWT token
     * @return 解析后的声明
     */
    public static Claims parseToken(String token) {
        // 直接使用预先生成的安全密钥
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * 验证JWT token是否有效
     * @param token JWT token
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
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
    public static String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }
    
    /**
     * 从JWT token中获取用户ID
     * @param token JWT token
     * @return 用户ID
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }
    
    /**
     * 从JWT token中获取家庭名称
     * @param token JWT token
     * @return 家庭名称
     */
    public static String getFamilyNameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("familyName", String.class);
    }
}