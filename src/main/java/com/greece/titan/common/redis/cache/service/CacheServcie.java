package com.greece.titan.common.redis.cache.service;

import com.greece.titan.common.redis.CacheConstants;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis Cache 서비스 인터페이스
 */
public interface CacheServcie {

    /**
     * Get Redis Operation Type
     * @param metaSet
     * @param redisType
     * @return
     */
    int getRedisOpType(String metaSet, CacheConstants.RedisType redisType);
    /**
     * Get key's entry name
     * @param entry
     * @return Name of the entry
     */
    String getEntryName(String entry);

    /**
     * Get Redis Template
     * @param redisType
     * @return Object of Redis Template
     */
    RedisTemplate<String, Object> getRedisTemplate(CacheConstants.RedisType redisType);

    /**
     * Get keys by entry name
     * @param metaSet
     * @return object type keys
     */
    Object getKeysByEntryName(String metaSet);

    /**
     * Get keys by entry name
     * @param metaSet
     * @param redisType(BackEnd/FrontEnd)
     * @return object type keys
     */
    Object getKeysByEntryName(String metaSet, CacheConstants.RedisType redisType);

    /**
     * Get keys by entry name
     * @param metaSet
     * @param clazz
     * @return Class<T> type keys
     */
    <T> T getKeysByEntryName(String metaSet, Class<T> clazz);

    /**
     * Get keys by entry name
     * @param metaSet
     * @param clazz
     * @param redisType(BackEnd/FrontEnd)
     * @return Class<T> type keys
     */
    <T> T getKeysByEntryName(String metaSet, Class<T> clazz, CacheConstants.RedisType redisType);

    /**
     * Get a key by ID and entry name
     * @param metaSet
     * @param id
     * @return Object type key/value
     */
    Object getKeyByEntryNameAndId(String metaSet, String id);

    /**
     * Get a key by ID and entry name
     * @param metaSet
     * @param id
     * @param redisType(BackEnd/FrontEnd)
     * @return Object type key/value
     */
    Object getKeyByEntryNameAndId(String metaSet, String id, CacheConstants.RedisType redisType);

    /**
     * Get a key by ID and entry name
     * @param metaSet
     * @param id
     * @param clazz
     * @return Class<T> type key/value
     */
    <T> T getKeyByEntryNameAndId(String metaSet, String id, Class<T> clazz);

    /**
     * Get a key by ID and entry name
     * @param metaSet
     * @param id
     * @param clazz
     * @param redisType(BackEnd/FrontEnd)
     * @return Class<T> type key/value
     */
    <T> T getKeyByEntryNameAndId(String metaSet, String id, Class<T> clazz, CacheConstants.RedisType redisType);



}
