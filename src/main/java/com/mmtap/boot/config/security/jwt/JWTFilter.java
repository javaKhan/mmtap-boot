package com.mmtap.boot.config.security.jwt;

import cn.hutool.core.util.StrUtil;
import com.mmtap.boot.common.constant.SecurityConstant;
import com.mmtap.boot.common.utils.JwtUtil;
import com.mmtap.boot.common.utils.ResponseUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Slf4j
public class JWTFilter extends BasicAuthenticationFilter {

    private SecurityUtil securityUtil;

    public JWTFilter(AuthenticationManager authenticationManager, SecurityUtil securityUtil){
        super(authenticationManager);
        this.securityUtil = securityUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(SecurityConstant.HEADER);
        if(StrUtil.isBlank(header)){
            header = request.getParameter(SecurityConstant.HEADER);
        }
        try {
            String userName = JwtUtil.getHeaderValue(header,"name");
            if (StringUtils.isEmpty(userName)){
                ResponseUtil.out(response, ResponseUtil.resultMap(false,401,"登录已失效，请重新登录"));
            }
            Authentication authentication = new MyAuthentication();
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception e){
            ResponseUtil.out(response, ResponseUtil.resultMap(false,400,"用户认证失败,请重新登录!"));
        }

        chain.doFilter(request, response);
    }

    private class MyAuthentication implements Authentication {
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return null;
        }

        /**
         * 有此步骤断定登录有效性
         * 自己实现到默认都为成功，失败的没有此对象
         * @return
         */
        @Override
        public boolean isAuthenticated() {
            return true;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

        }

        @Override
        public String getName() {
            return null;
        }
    }
}
