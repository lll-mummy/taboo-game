package edu.upc.lll.listener;



import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import edu.upc.lll.websocket.ChatEndpoint;

import javax.annotation.Resource;

@Component
public class RedisExpireListener implements MessageListener {

    @Resource
    private ChatEndpoint chatEndpoint;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody());
        if (expiredKey.startsWith("LOGIN_TOKEN_")) {
            String userId = expiredKey.replace("LOGIN_TOKEN_", "");
            chatEndpoint.removeUserFromMap(Long.parseLong(userId));
        }
    }
}

