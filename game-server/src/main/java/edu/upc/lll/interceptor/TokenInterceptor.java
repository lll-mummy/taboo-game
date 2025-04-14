package edu.upc.lll.interceptor;

import edu.upc.lll.common.exception.BusinessException;
import edu.upc.lll.common.exception.ErrorCodeEnum;
import edu.upc.lll.common.utils.JwtUtils;
import edu.upc.lll.common.utils.UserContext;
import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            Claims claims = jwtUtils.parseToken(token);
            String userId = claims.get("userId").toString();
            String username = claims.get("username").toString();
            String redisToken = redisTemplate.opsForValue().get("LOGIN_TOKEN_" + userId);
            if (!token.equals(redisToken)) {
                throw new BusinessException(ErrorCodeEnum.TOKEN_EXPIRED);
            }
            UserContext.setUsername(username);
            return true;
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
