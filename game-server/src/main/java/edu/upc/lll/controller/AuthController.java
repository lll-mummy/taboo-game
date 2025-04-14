package edu.upc.lll.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.upc.lll.common.exception.BusinessException;
import edu.upc.lll.common.exception.ErrorCodeEnum;
import edu.upc.lll.common.utils.JwtUtils;
import edu.upc.lll.common.ResponseEntity;
import edu.upc.lll.common.utils.UserContext;
import edu.upc.lll.dto.request.AuthRequest;
import edu.upc.lll.dto.response.AuthResponse;
import edu.upc.lll.pojo.User;
import edu.upc.lll.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private UserService userService;

    @Value("${jwt.redis-expiration-time}")
    private long redisExpirationTime;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, authRequest.getUsername()));
        if (user == null || !authRequest.getPassword().equals(user.getPassword())) {
            throw new BusinessException(ErrorCodeEnum.LOGIN_FAILED);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("userId", user.getId());
        String token = jwtUtils.generateToken(claims);


        AuthResponse authResponse = new AuthResponse();
        authResponse.setUsername(user.getUsername());
        authResponse.setToken(token);
        authResponse.setMessage("登录成功");

        // 5. 保存登录状态到 Redis，设置过期时间
        redisTemplate.opsForValue().set("LOGIN_TOKEN_" + user.getId(), token, redisExpirationTime, TimeUnit.MILLISECONDS);

        return ResponseEntity.success(authResponse);
    }

    @GetMapping("/username")
    public ResponseEntity<?> getUsername() {
        String username = UserContext.getUsername();
        return ResponseEntity.success(username);
    }

    @GetMapping("/api/auth/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String token) {
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
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }
        return ResponseEntity.success(ErrorCodeEnum.SUCCESS);
    }
}
