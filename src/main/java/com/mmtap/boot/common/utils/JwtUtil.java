package com.mmtap.boot.common.utils;

import cn.hutool.core.date.DateUtil;
import com.mmtap.boot.common.constant.SecurityConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    public static String createToken(Map claimMap) {
        Date expDate = DateUtil.offsetDay(new Date(), 1);
        String token = Jwts.builder()
                .setClaims(claimMap)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SIGN_KEY)
                .compact();
        return token;
    }

    public boolean checkToken(String token){
        return true;
    }
}
