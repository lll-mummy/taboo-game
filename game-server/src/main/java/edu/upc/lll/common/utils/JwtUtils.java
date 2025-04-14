package edu.upc.lll.common.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.expiration-time}")
    private long EXPIRATION_TIME;

    @Value("${jwt.enable-expiration:true}") // 默认开启
    private boolean ENABLE_EXPIRATION;

    /**
     * 生成 JWT Token
     */
    public String generateToken(Map<String, Object> claims) {
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY);

        if (ENABLE_EXPIRATION) {
            builder.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
            builder.setIssuedAt(new Date());
        }

        return builder.compact();
    }

    /**
     * 解析 Token 获取 Claims
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 判断 Token 是否过期
     */
    public boolean isTokenExpired(String token) {
        if (!ENABLE_EXPIRATION) {
            return false; // 配置中禁用了过期校验
        }
        Date expiration = parseToken(token).getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 是否启用了 Token 过期时间校验
     */
    public boolean isEnableExpiration() {
        return ENABLE_EXPIRATION;
    }
}
