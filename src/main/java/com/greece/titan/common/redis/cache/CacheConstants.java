package com.greece.titan.common.redis.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CacheConstants {

    public static final int SCAN_LIMITS = 10000;

    public static enum RedisSerializerTypes {
        String,
        GenericJackson2Json,
        JdkSerialization
    };

    public static enum RedisType {BFF, MS, Node};

    public static enum BACKING_REDIS_CACHE {

        /** 요금제 변경 메시지 */
        ProductChgGuidMsg(new RedisOpType(RedisType.Node, RedisCommands.SET, true)),
        /** STORE 상품관리 */
        StoreProduct(new RedisOpType(RedisType.Node, RedisCommands.SET, true));

        private List<RedisOpType> opTypes;//Redis Operation 유형
        public List<RedisOpType> getOpTypes() {
            return this.opTypes;
        }

        public void setRedisOpTypes(final List<RedisOpType> redisOpType) {
            this.opTypes = redisOpType;
        }
        public boolean isMultiLoad() {
            return this.opTypes != null && this.opTypes.size() > 1 ? true : false;
        }

        public static Optional<BACKING_REDIS_CACHE> of(final String value) {
            return Arrays.asList(values())
                    .stream()
                    .filter(x -> x.name().equalsIgnoreCase(value))
                    .findFirst();
        }

        BACKING_REDIS_CACHE(final RedisOpType... args) {
            if (args != null && args.length > 0) {
                List<RedisOpType> list = new ArrayList<>();
                for(RedisOpType opType: args) {
                    list.add(opType);
                }
                this.setRedisOpTypes(list);
            }
        }

    }

    /**
     * Redis Operate 유형
     * @author P125030
     *
     */
    public static class RedisOpType {
        private RedisType redisType;
        private RedisCommands redisCmd;
        private boolean useNamespace;

        public RedisOpType(final RedisType redisType, final RedisCommands opCommand, final boolean useNamespace) {
            super();
            this.redisType = redisType;
            this.redisCmd = opCommand;
            this.useNamespace = useNamespace;
        }
        /**
         * Redis 유형{MS, BFF, Node}
         */
        public RedisType getRedisType() {
            return redisType;
        }
        /**
         * Redis 데이터 입출력 유형{HSET, SET...}
         */
        public RedisCommands getRedisCmd() {
            return redisCmd;
        }
        /**
         * 데이터 Key 유형
         * false: 키의 형태가 단순한 형식 e.g. 'NA00004797'
         * true: 키의 형태가 결합한 형식 e.g. 'ProductLedger:MA00000006'
         */
        public boolean isUseNamespace() {
            return useNamespace;
        }
    }

    /** 지원하는 Redis 명령어 */
    public enum RedisCommands {
        HSET,
        SET
    }

}
