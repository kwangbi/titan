package com.greece.titan.common.redis.loader.service;

import com.greece.titan.common.exception.BusinessException;
import com.greece.titan.common.redis.CacheConstants;
import com.greece.titan.common.redis.cache.service.CacheServcie;


/**
 * Redis Cache 서비스 인터페이스
 */
public interface LoaderServcie extends CacheServcie {
    /**
     * Register new keys
     * @param metaSet
     * @return Number of registrations that success
     * @throws BusinessException
     */
    int createKeysWithEntryName(String metaSet) throws BusinessException;

    /**
     * Register new keys
     * @param metaSet
     * @param redisType(BackEnd/FrontEnd)
     * @return Number of registrations that success
     * @throws BusinessException
     */
    int createKeysWithEntryName(String metaSet, CacheConstants.RedisType redisType) throws BusinessException;

    /**
     * Modify keys in the entry name
     * @param metaSet
     * @param reset(Option to delete existing data, default: false)
     * @return Number of modifications that success
     * @throws BusinessException
     */
    int modifyKeysByEntryName(String metaSet, Boolean reset) throws BusinessException;

    /**
     * Modify keys in the entry name
     * @param metaSet
     * @param reset(Option to delete existing data, default: false)
     * @param redisType(BackEnd/FrontEnd)
     * @return Number of modifications that success
     * @throws BusinessException
     */
    int modifyKeysByEntryName(String metaSet, Boolean reset, CacheConstants.RedisType redisType) throws BusinessException;

    /**
     * Modify key's value in the entry name
     * @param metaSet
     * @param id(modify Target key)
     * @return Number of modifications that success
     * @throws BusinessException
     */
    int modifyKeyByEntryNameAndId(String metaSet, String id) throws BusinessException;

    /**
     * Modify key's value in the entry name
     * @param metaSet
     * @param id(modify Target key)
     * @param redisType(BackEnd/FrontEnd)
     * @return Number of modifications that success
     * @throws BusinessException
     */
    int modifyKeyByEntryNameAndId(String metaSet, String id, CacheConstants.RedisType redisType) throws BusinessException;

    /**
     * Delete keys
     * @param metaSet
     * @return Key deletion result
     */
    Boolean deleteKeysByEntryName(String metaSet);

    /**
     * Delete keys
     * @param metaSet
     * @param redisType(BackEnd/FrontEnd)
     * @return Key deletion result
     */
    Boolean deleteKeysByEntryName(String metaSet, CacheConstants.RedisType redisType);

    /**
     * Delete key
     * @param metaSet
     * @param id(delete Target key)
     * @return Key deletion result
     */
    Boolean deleteKeyByEntryNameAndId(String metaSet, String id);

    /**
     * Delete key
     * @param metaSet
     * @param id(delete Target key)
     * @param redisType(BackEnd/FrontEnd)
     * @return Key deletion result
     */
    Boolean deleteKeyByEntryNameAndId(String metaSet, String id, CacheConstants.RedisType redisType);
}
