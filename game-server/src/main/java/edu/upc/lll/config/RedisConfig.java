package edu.upc.lll.config;

import edu.upc.lll.listener.RedisExpireListener;
import edu.upc.lll.websocket.ChatEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用 StringRedisSerializer 序列化键
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 使用 Jackson2JsonRedisSerializer 序列化值
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        // 其他序列化方式可以根据需求调整
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        return redisTemplate;
    }

    // 配置 Redis 的消息监听容器
    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisExpireListener redisExpireListener;

    public RedisConfig(RedisConnectionFactory redisConnectionFactory,
                       RedisExpireListener redisExpireListener) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.redisExpireListener = redisExpireListener;
    }

    @Bean
    public RedisMessageListenerContainer messageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(new MessageListenerAdapter(redisExpireListener),
                new PatternTopic("__keyevent@0__:expired"));
        return container;
    }
}