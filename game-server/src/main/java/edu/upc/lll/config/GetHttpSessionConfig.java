package edu.upc.lll.config;

import edu.upc.lll.common.SpringContextHolder;
import edu.upc.lll.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;

@Component
public class GetHttpSessionConfig extends ServerEndpointConfig.Configurator {


    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        try {
            // 1. 获取并严格验证token
            Map<String, List<String>> params = request.getParameterMap();
            List<String> tokenList = params.get("token");

            if (tokenList == null || tokenList.isEmpty()) {
                throw new RuntimeException("未提供Token");
            }

            String token = tokenList.get(0);

            // 2. 检查token是否有效
            if (token == null || token.trim().isEmpty() || "null".equalsIgnoreCase(token)) {
                throw new RuntimeException("无效的Token格式");
            }

            // 3. 解析前检查基本格式
            if (token.chars().filter(ch -> ch == '.').count() != 2) {
                throw new RuntimeException("Token格式不正确，应包含两个点分隔符");
            }

            // 剩余原有代码...
            JwtUtils jwtUtils = SpringContextHolder.getBean(JwtUtils.class);
            StringRedisTemplate redisTemplate = (StringRedisTemplate) SpringContextHolder.getBean("stringRedisTemplate");

            Claims claims = jwtUtils.parseToken(token);
            Long userId = Long.parseLong(claims.get("userId").toString());
            String username = claims.get("username").toString();

            String redisToken = redisTemplate.opsForValue().get("LOGIN_TOKEN_" + userId);
            if (redisToken == null) {
                throw new RuntimeException("Token已失效，请重新登录");
            }
            sec.getUserProperties().put("username", username);
            sec.getUserProperties().put("userId", userId);
        } catch (Exception e) {
            throw new RuntimeException("WebSocket握手失败: " + e.getMessage());
        }
    }
}
