package com.greece.titan.common.redis.config;

import com.greece.titan.common.redis.CacheConstants;
import com.greece.titan.common.redis.util.RedisLoaderUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class MainRedisAutoConfiguration extends BaseRedisConfiguration {

    @Bean("mainRedisProperties")
    @ConditionalOnMissingBean(name = "mainRedisProperties")
    @ConfigurationProperties(prefix = CacheConstants.PREFIX_TITAN_REDIS_PROPERTIES)
    public RedisProperties redisProperties() {
        return new RedisProperties();
    };


    @Override
    @Bean("mainConnectionFactory")
    @DependsOn(value = {"mainRedisProperties"})
    @ConditionalOnMissingBean(name = "mainConnectionFactory")
    public LettuceConnectionFactory redisConnectionFactory(@Qualifier("mainRedisProperties") final RedisProperties properties) {
        return createLettuceConnectionFactory(lettuceClientResources(), properties);
    }

    @Override
    @Bean("mainRedisTemplate")
    @DependsOn(value = {"mainConnectionFactory"})
    @ConditionalOnMissingBean(name = "mainRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("mainConnectionFactory") final RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        RedisLoaderUtils.redisTemplateKeySerializer(CacheConstants.RedisSerializerTypes.String, template);
        RedisLoaderUtils.redisTemplateValueSerializer(CacheConstants.RedisSerializerTypes.GenericJackson2Json, template);
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
