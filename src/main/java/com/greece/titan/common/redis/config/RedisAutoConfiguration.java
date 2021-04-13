package com.greece.titan.common.redis.config;


import com.greece.titan.common.redis.cache.CacheConstants;
import com.greece.titan.common.redis.util.RedisLoaderUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 업무명 : Redis 자동 설정
 * 설  명 : Redis 자동 설정
 */
@Configuration
public class RedisAutoConfiguration extends BaseRedisConfiguration {
    @Override
    @Primary
    @Bean("redisProperties")
    @ConditionalOnMissingBean(name = "redisProperties")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    };

    @Override
    @Primary
    @Bean("redisConnectionFactory")
    @DependsOn(value = {"redisProperties"})
    @ConditionalOnMissingBean(name = "redisConnectionFactory")
    public LettuceConnectionFactory redisConnectionFactory(@Qualifier("redisProperties") final RedisProperties properties) {
        return createLettuceConnectionFactory(lettuceClientResources(), properties);
    }

    /**
     * Redis 템플릿 RedisSerializer 설정
     * @param connectionFactory
     * @return Redis Template
     */
    @Override
    @Primary
    @Bean("redisTemplate")
    @DependsOn(value = {"redisConnectionFactory"})
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("redisConnectionFactory")final RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        RedisLoaderUtils.redisTemplateKeySerializer(CacheConstants.RedisSerializerTypes.String, template);
        RedisLoaderUtils.redisTemplateValueSerializer(CacheConstants.RedisSerializerTypes.GenericJackson2Json, template);
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
