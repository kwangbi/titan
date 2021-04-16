package com.greece.titan.common.redis.util;

import javax.annotation.PostConstruct;

import com.greece.titan.common.redis.CacheConstants;
import com.greece.titan.common.redis.cache.service.CommonCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheUtils {

    @Autowired
    CommonCacheService commonCacheService;

    private static CommonCacheService cacheService;

    @PostConstruct
    private void init() {
        CacheUtils.cacheService = commonCacheService;
    }

    private static CommonCacheService getCacheService() {
        return CacheUtils.cacheService;
    }

    /**
     * 메타데이터 조회 (Only MS)
     *
     * @param metaSet : 메타데이터 명
     * @param id : Key
     * @return Object(<Map>) 메타 데이터
     */
    public static Object getCacheData(final String metaSet, final String id) {
        return getCacheData(metaSet, id, CacheConstants.RedisType.MS);
    }

    /**
     * 메타데이터 조회 (Only MS)
     *
     * @param metaSet : 메타데이터 명
     * @param id : Key
     * @param clazz : Return Model
     * @return <class> 메타 데이터 (1건)
     */
    public static <T> T getCacheData(final String metaSet, final String id, final Class<T> clazz) {
        return getCacheData(metaSet, id, clazz, CacheConstants.RedisType.MS);
    }

    /**
     * 메타데이터 조회 (MS/BFF)
     *
     * @param metaSet : 메타데이터 명
     * @param key : Key
     * @param type : RedisType (MS/BFF)
     * @return Object(<Map>) 메타 데이터 (1건)
     */
    public static Object getCacheData(final String metaSet, final String key, final CacheConstants.RedisType redisType) {
        return getCacheService().getKeyByEntryNameAndId(metaSet, key, redisType);
    }

    /**
     * 메타데이터 조회 (MS/BFF)
     *
     * @param metaSet : 메타데이터 명
     * @param id : Key
     * @param clazz : Return Model
     * @param type : RedisType (MS/BFF)
     * @return <class> 메타 데이터 (1건)
     */
    public static <T> T getCacheData(final String metaSet, final String id, final Class<T> clazz, final CacheConstants.RedisType redisType) {
        return getCacheService().getKeyByEntryNameAndId(metaSet, id, clazz, redisType);
    }
}
