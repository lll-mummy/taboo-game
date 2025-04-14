package edu.upc.lll.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080") // 必须与前端完全一致
                .allowedMethods("*")
                // 关键修改：header名称必须与前端完全匹配（全小写）
                .allowedHeaders("authorization", "content-type")
                .exposedHeaders("authorization")
                .allowCredentials(true)
                .maxAge(1800); // 预检缓存时间缩短便于调试
    }
}

