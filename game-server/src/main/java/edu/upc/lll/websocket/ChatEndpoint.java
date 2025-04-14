package edu.upc.lll.websocket;

import edu.upc.lll.common.SpringContextHolder;
import edu.upc.lll.common.utils.JwtUtils;
import edu.upc.lll.common.utils.MessageUtils;
import edu.upc.lll.config.GetHttpSessionConfig;
import edu.upc.lll.pojo.LoginUserInfo;
import edu.upc.lll.pojo.WordDict;
import edu.upc.lll.service.WordDictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfig.class)
@Component
public class ChatEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(ChatEndpoint.class);

    // 线程安全的在线用户集合
    private static final Map<Long, Session> onlineUsers = new ConcurrentHashMap<>();
    private static final Map<Long, LoginUserInfo> userInfoMap = new ConcurrentHashMap<>();

    private String username;

    private Long userId;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.username = (String) config.getUserProperties().get("username");
        this.userId = (Long) config.getUserProperties().get("userId");
        if (username == null || userId == null) {
            closeSession(session, "未提供用户名");
            return;
        }

        try {
            // 初始化用户信息（如果不存在）
            userInfoMap.computeIfAbsent(userId, k -> {
                WordDictService wordDictService = SpringContextHolder.getBean(WordDictService.class);
                List<WordDict> wordDictList = wordDictService.list();
                WordDict randomWordDict = wordDictList.get(new Random().nextInt(wordDictList.size()));
                return new LoginUserInfo(1L, username, randomWordDict.getWord(), 0);
            });

            // 添加新会话（如果已存在则替换旧会话）
            onlineUsers.put(userId, session);

            logger.info("用户[{}]连接建立，当前在线人数：{}", username, onlineUsers.size());

            // 广播更新后的用户列表
            broadcastUserList();
        } catch (Exception e) {
            logger.error("用户[{}]连接初始化失败", username, e);
            closeSession(session, "初始化失败");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            String username = (String) session.getUserProperties().get("username");
            Long userId = (Long) session.getUserProperties().get("userId");
            if (username == null || userId == null) {
                logger.error("无法获取用户名，关闭会话");
                closeSession(session, "身份验证失败");
                return;
            }
            renewToken(userId);
            if ("changeWord".equals(message)) {
                changeUserWord(userId);
                broadcastUserList();
            }

        } catch (Exception e) {
            logger.error("消息处理失败", e);
            closeSession(session, "消息处理错误");
        }
    }

    @OnClose
    public void onClose(Session session) {
        if (username != null) {
            onlineUsers.remove(userId);
            logger.info("用户[{}]断开连接，当前在线人数：{}", username, onlineUsers.size());
            broadcastUserList();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        if (username != null) {
            logger.error("用户[{}]连接异常", username, error);
        }
        closeSession(session, "系统错误");
    }

    private synchronized void broadcastUserList() {
        String message = MessageUtils.getMessage(true, null, getFriends());
        broadcastAllUsers(message);
    }

    private Collection<LoginUserInfo> getFriends() {
        return new ArrayList<>(userInfoMap.values());
    }

    private synchronized void broadcastAllUsers(String message) {
        Iterator<Map.Entry<Long, Session>> iterator = onlineUsers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Session> entry = iterator.next();
            Session session = entry.getValue();

            try {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(message);
                } else {
                    iterator.remove(); // 清理无效会话
                    logger.debug("清理无效会话：{}", entry.getKey());
                }
            } catch (IOException e) {
                iterator.remove(); // 发送失败时清理
                logger.warn("消息发送失败，清理用户[{}]会话", entry.getKey(), e);
            }
        }
    }

    private void closeSession(Session session, String reason) {
        try {
            if (session != null && session.isOpen()) {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, reason));
            }
        } catch (IOException e) {
            logger.error("关闭会话异常", e);
        }
    }



    private synchronized void changeUserWord(Long userId) {
        LoginUserInfo user = userInfoMap.get(userId);
        if (user == null) return;

        // 1. 从数据库重新获取随机词牌
        WordDictService wordDictService = SpringContextHolder.getBean(WordDictService.class);
        List<WordDict> wordDictList = wordDictService.list();
        WordDict newWordDict = wordDictList.get(new Random().nextInt(wordDictList.size()));

        // 2. 更新用户信息
        user.setWord(newWordDict.getWord());
        user.setCount(user.getCount() + 1); // 重生次数+1

        logger.info("用户[{}]更换词牌为：{}，当前重生次数：{}",
                username, newWordDict.getWord(), user.getCount());
    }

    public void removeUserFromMap(Long userId) {
        userInfoMap.remove(userId);
        broadcastUserList();
    }


    private void renewToken(Long userId) {
        StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
        String key = "LOGIN_TOKEN_" + userId;
        String token = redisTemplate.opsForValue().get(key);
        if (token != null) {
            // ✅ 重新设置 token 有效期为 15 分钟（与之前设定保持一致）
            redisTemplate.expire(key, 15, TimeUnit.MINUTES);
            logger.debug("为用户 [{}] 续期 Token", username);
        }
    }

}