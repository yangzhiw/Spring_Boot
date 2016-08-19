package com.juzi.config;

import com.juzi.domain.p.User;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by yzw on 2016/8/18 0018.
 */
@Configuration
public class RedisConfig {

    @Bean
    @ConfigurationProperties(prefix="spring.redis")
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory connFactory = new JedisConnectionFactory();

 //       connFactory.setHostName("10.1.6.182");
        connFactory.setHostName("192.168.16.103");
        connFactory.setPort(6379);
        connFactory.setUsePool(true);//使用连接池
        return connFactory;
    }

    @Bean
    public RedisTemplate<String, User> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, User> template = new RedisTemplate<String, User>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new RedisObjectSerializer());
        return template;
    }
}
