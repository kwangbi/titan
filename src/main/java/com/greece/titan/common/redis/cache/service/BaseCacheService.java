package com.greece.titan.common.redis.cache.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.greece.titan.common.exception.BusinessException;
import com.greece.titan.common.redis.CacheConstants;
import com.greece.titan.common.redis.CacheConstants.BACKING_REDIS_CACHE;
import com.greece.titan.common.redis.CacheConstants.RedisCommands;
import com.greece.titan.common.redis.CacheConstants.RedisOpType;
import com.greece.titan.common.redis.CacheConstants.RedisType;
import com.greece.titan.common.redis.util.RedisLoaderUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;

public abstract class BaseCacheService implements CacheServcie {

    @Override
    public String getEntryName(final String entry) {
        Optional<BACKING_REDIS_CACHE> cache = BACKING_REDIS_CACHE.of(entry);
        if (cache.isPresent()) {
            return cache.get().name();
        } else {
            throw new BusinessException("BRL0003");
        }
    }

    @Override
    public Object getKeysByEntryName(final String metaSet, final RedisType redisType) {
        int type = getRedisOpType(metaSet, redisType);
        if (type == 1 || type == 3) {
            List<String> keys = new ArrayList<>();
            Map<String, Object> result = new HashMap<>();
            Cursor<byte[]> cursor = getRedisTemplate(redisType)
                    .getConnectionFactory()
                    .getConnection()
                    .scan(ScanOptions.scanOptions().match(getEntryName(metaSet) + ":*").count(CacheConstants.SCAN_LIMITS).build());
            List<Object> rs = getRedisTemplate(redisType).executePipelined(
                    new RedisCallback<Object>() {
                        @Override
                        public Object doInRedis(final RedisConnection connection) throws DataAccessException {
                            while (cursor.hasNext()) {
                                try {
                                    byte[] key = cursor.next();
                                    keys.add(new String(key));
                                    connection.get(key);
                                } catch (final Exception e) {
                                    throw e;
                                }
                            }
                            return null;
                        }
                    });
            int index = 0;
            for (String key: keys) {
                result.put(key, rs.get(index));
                index++;
            }
            return result;
        } else {
            if (type == 2) {
                return getRedisTemplate(redisType).opsForValue().get(getEntryName(metaSet));
            } else if (type == 4) {
                return getRedisTemplate(redisType).opsForHash().entries(getEntryName(metaSet));
            } else {
                return null;
            }
        }
    }

    @Override
    public <T> T getKeysByEntryName(final String metaSet, final Class<T> clazz, final RedisType redisType) {
        final Object result = getKeysByEntryName(metaSet, redisType);
        return RedisLoaderUtils.convertInstanceOfObject(result, clazz);
    }

    @Override
    public Object getKeyByEntryNameAndId(final String metaSet, final String id, final RedisType redisType) {
        int type = getRedisOpType(metaSet, redisType);
        if (type == 1) {
            return getRedisTemplate(redisType).opsForValue().get(getEntryName(metaSet) + ":" + id);
        } else if (type == 2) {
            return getRedisTemplate(redisType).opsForValue().get(id);
        } else if (type == 3) {
            return getRedisTemplate(redisType).opsForHash().get(getEntryName(metaSet), getEntryName(metaSet) + ":" + id);
        } else if (type == 4) {
            return getRedisTemplate(redisType).opsForHash().get(getEntryName(metaSet), id);
        } else {
            return null;
        }
    }

    @Override
    public <T> T getKeyByEntryNameAndId(final String metaSet, final String id, final Class<T> clazz, final RedisType redisType) {
        final Object result = getKeyByEntryNameAndId(metaSet, id, redisType);
        return RedisLoaderUtils.convertInstanceOfObject(result, clazz);
    }

    @Override
    public int getRedisOpType(final String entry, final RedisType redisType) {
        Optional<BACKING_REDIS_CACHE> cache = BACKING_REDIS_CACHE.of(entry);
        if (!cache.isPresent()) {
            throw new BusinessException("BRL0003");
        }
        RedisOpType op = null;
        if (cache.get().isMultiLoad()) {
            op = cache.get().getOpTypes()
                    .stream()
                    .filter(o -> o.getRedisType().equals(redisType))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("BRL0007"));
        } else {
            op = cache.get().getOpTypes().get(0);
        }

        if (RedisCommands.SET.equals(op.getRedisCmd()) && op.isUseNamespace()) {
            return 1;
        } else if (RedisCommands.SET.equals(op.getRedisCmd()) && !op.isUseNamespace()) {
            return 2;
        } else if (RedisCommands.HSET.equals(op.getRedisCmd()) && op.isUseNamespace()) {
            return 3;
        } else if (RedisCommands.HSET.equals(op.getRedisCmd()) && !op.isUseNamespace()) {
            return 4;
        } else {
            throw new BusinessException("BRL0007");
        }
    }

}
