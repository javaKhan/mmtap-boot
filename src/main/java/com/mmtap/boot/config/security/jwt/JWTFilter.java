package com.mmtap.boot.config.security.jwt;

import cn.hutool.core.util.StrUtil;
import com.mmtap.boot.common.constant.SecurityConstant;
import com.mmtap.boot.common.utils.ResponseUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
//        Boolean notValid = StrUtil.isBlank(header) || !header.startsWith(SecurityConstant.TOKEN_SPLIT);
//        Boolean notValid = StrUtil.isBlank(header);
        //Token有效性校验
        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(header, response);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception e){
            e.toString();
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String header, HttpServletResponse response) {
        // 用户名
        String username = null;

        // JWT
        try {
            // 解析token
            Claims claims = Jwts.parser()
                    .setSigningKey(SecurityConstant.JWT_SIGN_KEY)
                    .parseClaimsJws(header.replace(SecurityConstant.TOKEN_SPLIT, ""))
                    .getBody();

            // 获取用户名
            username = claims.get("name").toString();
//            username = claims.getSubject();
        } catch (ExpiredJwtException e) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false,401,"登录已失效，请重新登录"));
        } catch (Exception e){
            log.error(e.toString());
            ResponseUtil.out(response, ResponseUtil.resultMap(false,500,"解析token错误"));
        }

        if(StrUtil.isNotBlank(username)) {
            //此处password不能为null
            User principal = new User(username, "", null);
            return new UsernamePasswordAuthenticationToken(principal, null);
        }
        return null;
    }
}
