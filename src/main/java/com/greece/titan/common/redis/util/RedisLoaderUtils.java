package com.greece.titan.common.redis.util;


import com.greece.titan.common.redis.cache.CacheConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisLoaderUtils {
    /**
     * @param value
     * @param redisSerializer
     * @return byte[] serialized data for I/O
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static byte[] serialize(final Object value, final RedisSerializer redisSerializer) {
        if (redisSerializer == null && value instanceof byte[]) {
            return (byte[]) value;
        }
        return redisSerializer.serialize(value);
    }

    /**
     * Convert an instance
     * @param <T>
     * @param o, object that for convert
     * @param clazz, class that to convert
     * @return converted object
     */
    public static <T> T convertInstanceOfObject(final Object o, final Class<T> T) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(o, T);
        } catch(final IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * @param type
     * @param template
     * @return
     */
    public static <K, V> RedisTemplate<?, ?> redisTemplateKeySerializer(final CacheConstants.RedisSerializerTypes type, final RedisTemplate<K, V> template) {
        if (CacheConstants.RedisSerializerTypes.String.equals(type)) {
            template.setKeySerializer(new StringRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());
        } else if (CacheConstants.RedisSerializerTypes.GenericJackson2Json.equals(type)) {
            template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
            template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        } else {
            template.setKeySerializer(new JdkSerializationRedisSerializer());
            template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        }
        return template;
    }

    /**
     * @param type
     * @param template
     * @return
     */
    public static <K, V> RedisTemplate<?, ?> redisTemplateValueSerializer(final CacheConstants.RedisSerializerTypes type, final RedisTemplate<K, V> template) {
        if (CacheConstants.RedisSerializerTypes.GenericJackson2Json.equals(type)) {
            template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
            template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        } else if (CacheConstants.RedisSerializerTypes.String.equals(type)) {
            template.setValueSerializer(new StringRedisSerializer());
            template.setHashValueSerializer(new StringRedisSerializer());
        } else {
            template.setValueSerializer(new JdkSerializationRedisSerializer());
            template.setHashValueSerializer(new JdkSerializationRedisSerializer());
        }
        return template;
    }
}
