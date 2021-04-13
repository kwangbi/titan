package com.greece.titan.common.redis.cache;

public class CacheConstants {

    public static enum RedisSerializerTypes {
        String,
        GenericJackson2Json,
        JdkSerialization
    };
}
