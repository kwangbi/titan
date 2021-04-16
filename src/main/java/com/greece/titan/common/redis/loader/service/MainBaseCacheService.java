package com.greece.titan.common.redis.loader.service;

import javax.annotation.Resource;

import com.greece.titan.common.exception.BusinessException;
import com.greece.titan.common.redis.CacheConstants.RedisType;
import org.springframework.data.redis.core.RedisTemplate;

public abstract class MainBaseCacheService extends BaseLoaderService {

    @Resource(name = "mainRedisTemplate")
    private RedisTemplate<String, Object> template;

    @Override
    public RedisTemplate<String, Object> getRedisTemplate(final RedisType redisType) {
        return this.template;
    }
    @Override
    public int createKeysWithEntryName(final String metaSet) throws BusinessException {
        return createKeysWithEntryName(metaSet, RedisType.MS);
    }

    @Override
    public int modifyKeysByEntryName(final String metaSet, final Boolean reset) throws BusinessException {
        if (reset) {
            deleteKeysByEntryName(metaSet, RedisType.MS);
        }
        return createKeysWithEntryName(metaSet, RedisType.MS);
    }

    @Override
    public int modifyKeyByEntryNameAndId(final String metaSet, final String id) throws BusinessException {
        return modifyKeyByEntryNameAndId(metaSet, id, RedisType.MS);
    }

    @Override
    public Object getKeysByEntryName(final String metaSet) {
        return getKeysByEntryName(metaSet, RedisType.MS);
    };

    @Override
    public <T> T getKeysByEntryName(final String metaSet, final Class<T> clazz) {
        return getKeysByEntryName(metaSet, clazz, RedisType.MS);
    }

    @Override
    public Object getKeyByEntryNameAndId(final String metaSet, final String id) {
        return getKeyByEntryNameAndId(metaSet, id, RedisType.MS);
    }

    @Override
    public <T> T getKeyByEntryNameAndId(final String metaSet, final String id, final Class<T> clazz) {
        return getKeyByEntryNameAndId(metaSet, id, clazz, RedisType.MS);
    }

    @Override
    public Boolean deleteKeysByEntryName(final String metaSet) {
        return deleteKeysByEntryName(metaSet, RedisType.MS);
    }

    @Override
    public Boolean deleteKeyByEntryNameAndId(final String metaSet, final String id) {
        return deleteKeyByEntryNameAndId(metaSet, id, RedisType.MS);
    }

}
