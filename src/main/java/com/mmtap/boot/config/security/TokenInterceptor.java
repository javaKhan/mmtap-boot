package com.mmtap.boot.config.security;

import cn.hutool.core.util.StrUtil;
import com.mmtap.boot.common.constant.SecurityConstant;
import com.mmtap.boot.common.utils.JwtUtil;
import com.mmtap.boot.common.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

//    @Autowired
//    private JwtUtil jwtUtil;

//    private SecurityUtil securityUtil;

    /**
     * 可以通行返回true
     * 不通行返回false
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("==========================");
        String header = request.getHeader(SecurityConstant.HEADER);
        if(StrUtil.isBlank(header)){
            header = request.getParameter(SecurityConstant.HEADER);
        }
        try {
            String userName = JwtUtil.getHeaderValue(header,"name");
            if (StringUtils.isEmpty(userName)){
                ResponseUtil.out(response, ResponseUtil.resultMap(false,401,"登录已失效，请重新登录"));
                return false;
            }
        }catch (Exception e){
            ResponseUtil.out(response, ResponseUtil.resultMap(false,401,"登录已失效，请重新登录"));
            return false;
        }
        return true;
    }
}
