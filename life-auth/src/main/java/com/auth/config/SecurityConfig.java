package com.auth.config;

import com.core.utils.TokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/captcha/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(csrf -> csrf.disable())
                .addFilter(authenticationManager(http, userDetailsService()));

        return http.build();
    }



    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // 数据库查询
            String role = determineUserRole(username);
            return User.withUsername(username)
                    .password("") // 实际密码由登录接口验证
                    .authorities(AuthorityUtils.createAuthorityList(role))
                    .build();
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private JwtAuthenticationFilter authenticationManager(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        return new JwtAuthenticationFilter(userDetailsService);
    }

    private String determineUserRole(String username) {
        // TODO: 替换为真实的数据库查询逻辑（待补充）
        return "USER"; // 默认返回 USER
    }
    @Bean
    public TokenUtil tokenUtil() {
        return new TokenUtil();
    }
}
