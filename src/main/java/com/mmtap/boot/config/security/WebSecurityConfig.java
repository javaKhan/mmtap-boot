package com.mmtap.boot.config.security;

import com.mmtap.boot.config.properties.IgnoredUrlsProperties;
import com.mmtap.boot.config.security.jwt.JWTFilter;
import com.mmtap.boot.modules.account.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Security 核心配置类
 * 开启控制权限至Controller
 * @author mmtap
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private IgnoredUrlsProperties ignoredUrlsProperties;
    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private AccountService accountService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        accountService.initArea();

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();

        // 除配置文件忽略路径其它所有请求都需经过认证和授权
        for(String url:ignoredUrlsProperties.getUrls()){
            registry.antMatchers(url).permitAll();
        }
        registry.and().csrf().disable().cors().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        registry.anyRequest().authenticated().and().addFilter(new JWTFilter(authenticationManager(),securityUtil));
    }

    @Bean
    public SecurityUtil getSecurityUtil(){
        return new SecurityUtil();
    }
}
