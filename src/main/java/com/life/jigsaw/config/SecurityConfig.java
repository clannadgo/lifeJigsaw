package com.life.jigsaw.config;

import com.life.jigsaw.common.utils.JwtUtils;
import com.life.jigsaw.domain.LifeUser;
import com.life.jigsaw.mapper.LifeUserMapper;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Spring Security配置类
 * 提供JWT认证支持
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // 禁用CSRF保护
            .authorizeHttpRequests(authorize -> authorize
                // 允许公开访问的接口
                .requestMatchers("/user/login", "/user/add", "/user/sendEmailCode", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // 其他接口需要认证
                .anyRequest().authenticated()
            )
            // 添加JWT认证过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    /**
     * JWT认证过滤器
     */
    @Service
    public static class JwtAuthenticationFilter extends OncePerRequestFilter {

        @Resource
        private LifeUserMapper lifeUserMapper;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            // 获取Authorization头
            String authorizationHeader = request.getHeader("Authorization");
            
            // 检查Authorization头是否存在且以Bearer开头
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                
                // 验证token
                if (JwtUtils.validateToken(token)) {
                    try {
                        // 获取token中的用户信息
                        Long userId = JwtUtils.getUserIdFromToken(token);
                        
                        // 从数据库中获取用户信息
                        LifeUser user = lifeUserMapper.selectById(userId);
                        if (user != null) {
                            // 创建认证对象
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    user, null, new ArrayList<>());
                            
                            // 设置认证信息到SecurityContext
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    } catch (Exception e) {
                        // token解析失败，不影响请求继续
                    }
                }
            }
            
            // 继续执行过滤器链
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 用户详情服务
     */
    @Service
    public static class JwtUserDetailsService implements UserDetailsService {

        @Resource
        private LifeUserMapper lifeUserMapper;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            LifeUser user = lifeUserMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<LifeUser>()
                    .eq("username", username));
            
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
            
            // 这里简化处理，实际应用中可以根据需要返回更完整的用户权限信息
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    new ArrayList<>());
        }
    }
}