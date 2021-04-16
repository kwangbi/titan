package com.greece.titan.common.redis.loader.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.greece.titan.common.exception.BusinessException;
import com.greece.titan.common.redis.CacheConstants;
import com.greece.titan.common.redis.CacheConstants.RedisType;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import com.greece.titan.common.redis.cache.service.BaseCacheService;

public abstract class BaseLoaderService extends BaseCacheService implements LoaderServcie {

    @Override
    public int modifyKeysByEntryName(final String metaSet, final Boolean reset, final RedisType redisType) throws BusinessException {
        if (reset) {
            deleteKeysByEntryName(metaSet, redisType);
        }
        return createKeysWithEntryName(metaSet, redisType);
    };

    @Override
    public int modifyKeyByEntryNameAndId(final String metaSet, final String id, final RedisType redisType) throws BusinessException {
        throw new BusinessException("BRL0008");
    }

    @Override
    public Boolean deleteKeyByEntryNameAndId(final String metaSet, final String id, final RedisType redisType) {
        int type = getRedisOpType(metaSet, redisType);
        if (type == 1 || type == 3) {
            return getRedisTemplate(redisType).delete(metaSet + ":" + id);
        } else if (type == 2) {
            return getRedisTemplate(redisType).delete(getEntryName(metaSet));
        } else if (type == 4) {
            getRedisTemplate(redisType).opsForHash().delete(getEntryName(metaSet), id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deleteKeysByEntryName(final String metaSet, final RedisType redisType) {
        int type = getRedisOpType(metaSet, redisType);
        if (type == 1 || type == 3) {
            List<byte[]> list = new ArrayList<>();
            Cursor<byte[]> cursor = getRedisTemplate(redisType)
                    .getConnectionFactory()
                    .getConnection()
                    .scan(ScanOptions.scanOptions().match(getEntryName(metaSet) + ":*").count(CacheConstants.SCAN_LIMITS).build());
            while (cursor.hasNext()) {
                list.add(cursor.next());
            }
            byte[][] keys = Arrays.copyOf(new byte[][] {}, list.size());
            int index = 0;
            for (byte[] key: list) {
                keys[index] = key;
                index++;
            }
            if (keys == null || keys.length == 0) {
                return true;
            }
            List<Object> delSize = getRedisTemplate(redisType).executePipelined(
                    new RedisCallback<Object>() {
                        @Override
                        public Object doInRedis(final RedisConnection connection) throws DataAccessException {
                            try {
                                connection.del(keys);
                            } catch (final Exception e) {
                                throw e;
                            }
                            return null;
                        }
                    });
            return delSize.size() > 0 ? true : false;
        } else {
            return getRedisTemplate(redisType).delete(getEntryName(metaSet));
        }
    }
}
