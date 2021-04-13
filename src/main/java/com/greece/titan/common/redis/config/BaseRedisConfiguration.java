package com.greece.titan.common.redis.config;

import java.util.HashSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;
import org.springframework.data.redis.core.RedisTemplate;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ClientOptions.DisconnectedBehavior;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;

public abstract class BaseRedisConfiguration {
    private boolean isCluster = false;
    private boolean isSentinel = false;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(ClientResources.class)
    public DefaultClientResources lettuceClientResources() {
        return DefaultClientResources.create();
    }

    public abstract RedisProperties redisProperties();

    /**
     * Redis Properties를 이용한 LettuceConnectionFactory 생성
     * @param properties
     * @return
     */
    public abstract LettuceConnectionFactory redisConnectionFactory(RedisProperties properties);

    /**
     * RedisConnectionFactory를 이용한 RedisTemplate 생성
     * @param connectionFactory
     * @return
     */
    public abstract RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory);

    /**
     * Redis Properties를 이용한 LettuceConnectionFactory 생성
     * @param clientResources
     * @param properties
     * @return
     */
    public LettuceConnectionFactory createLettuceConnectionFactory(final DefaultClientResources clientResources,final RedisProperties properties) {
        isCluster = this.isCluster(properties);
        isSentinel = this.isSentinel(properties);
        final LettuceClientConfiguration clientConfig = getLettuceClientConfiguration(clientResources, properties.getLettuce().getPool(), properties);
        final Object serverConfig = getRedisConfiguration(properties);
        LettuceConnectionFactory connectionFactory = null;
        if (serverConfig instanceof RedisClusterConfiguration) {
            connectionFactory = new LettuceConnectionFactory((RedisClusterConfiguration) serverConfig, clientConfig);
        } else if (serverConfig instanceof RedisSentinelConfiguration) {
            connectionFactory = new LettuceConnectionFactory((RedisSentinelConfiguration) serverConfig, clientConfig);
        } else {
            connectionFactory = new LettuceConnectionFactory((RedisStandaloneConfiguration) serverConfig, clientConfig);
        }
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }

    /**
     * Redis Properties를 이용한 Redis ServerConfiguration 생성 (RedisStandaloneConfiguration/RedisSentinelConfiguration/RedisClusterConfiguration)
     * @param properties
     * @return
     */
    private Object getRedisConfiguration(final RedisProperties properties) {
        if (isCluster) {
            final RedisClusterConfiguration serverConfig = new RedisClusterConfiguration(properties.getCluster().getNodes());
            serverConfig.setPassword(StringUtils.isBlank(properties.getPassword())?"":properties.getPassword());
            return serverConfig;
        } else if (isSentinel) {
            final RedisSentinelConfiguration serverConfig = new RedisSentinelConfiguration(properties.getSentinel().getMaster(), new HashSet<>(properties.getSentinel().getNodes()));
            serverConfig.setDatabase(properties.getDatabase());
            serverConfig.setPassword(StringUtils.isBlank(properties.getPassword())?"":properties.getPassword());
            return serverConfig;
        } else {
            RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(properties.getHost(), properties.getPort());
            if (StringUtils.isNotBlank(properties.getHost())) {
                serverConfig = new RedisStandaloneConfiguration(properties.getHost(), properties.getPort());
            } else {
                serverConfig = new RedisStandaloneConfiguration();
            }
            serverConfig.setDatabase(properties.getDatabase());
            serverConfig.setPassword(StringUtils.isBlank(properties.getPassword())?"":properties.getPassword());
            return serverConfig;
        }
    }

    /**
     * LettuceClientConfiguration 생성
     * @param clientResources
     * @param pool
     * @param properties
     * @return Lettuce Client Configuration 객체
     */
    private LettuceClientConfiguration getLettuceClientConfiguration(final ClientResources clientResources, final RedisProperties.Pool pool, final RedisProperties properties) {
        final LettuceClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder()
                .poolConfig(getPoolConfig(pool))
                .clientOptions(getClientOptions());
        if (properties.isSsl()) {
            builder.useSsl();
        }
        if (properties.getTimeout() != null) {
            builder.commandTimeout(properties.getTimeout());
        }
        if (properties.getLettuce() != null) {
            RedisProperties.Lettuce lettuce = properties.getLettuce();
            if (lettuce.getShutdownTimeout() != null && !lettuce.getShutdownTimeout().isZero()) {
                builder.shutdownTimeout(properties.getLettuce().getShutdownTimeout());
            }
        }
        if (isSentinel || isCluster) {
            builder.readFrom(ReadFrom.SLAVE_PREFERRED);
        }
        builder.clientResources(clientResources);
        return builder.build();
    }

    /**
     * Redis Pool 설정
     * @param properties
     * @return (GenericObjectPoolConfig) Redis Pool 설정 객체
     */
    private GenericObjectPoolConfig getPoolConfig(final RedisProperties.Pool properties) {
        final GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        if (properties == null) {
            return config;
        }
        config.setMaxTotal(properties.getMaxActive());
        config.setMaxIdle(properties.getMaxIdle());
        config.setMinIdle(properties.getMinIdle());
        if (properties.getMaxWait() != null) {
            config.setMaxWaitMillis(properties.getMaxWait().toMillis());
        }
        return config;
    }

    /**
     * Redis Properties 설정 값이 Sentinel인지 확인
     * @param properties
     * @return (Boolean) 판경 결과
     */
    private boolean isSentinel(final RedisProperties properties) {
        return (properties.getSentinel() == null || CollectionUtils.isEmpty(properties.getSentinel().getNodes())) ? false : true;
    }

    /**
     * Redis Properties 설정 값이 Cluster인지 확인
     * 작성자: 김태형
     * 작성일: 2018. 4. 11
     *
     * @param properties
     * @return (Boolean) 판경 결과
     */
    private boolean isCluster(final RedisProperties properties) {
        return (properties.getCluster() == null || CollectionUtils.isEmpty(properties.getCluster().getNodes())) ? false : true;
    }

    private ClientOptions getClientOptions() {
        if (isCluster) {
            return getClusterClientOptions();
        }
        return ClientOptions.builder()
                .autoReconnect(true)
                .cancelCommandsOnReconnectFailure(true)
                .disconnectedBehavior(DisconnectedBehavior.REJECT_COMMANDS)
                .build();
    }

    /**
     * Redis Cluster Client 설정
     * @return
     */
    private ClusterClientOptions getClusterClientOptions() {
        return ClusterClientOptions.builder()
                .topologyRefreshOptions(ClusterTopologyRefreshOptions.builder()
                        .enableAllAdaptiveRefreshTriggers()
                        .enablePeriodicRefresh(true)
                        .build())
                .autoReconnect(false)
                .cancelCommandsOnReconnectFailure(true)
                .disconnectedBehavior(DisconnectedBehavior.REJECT_COMMANDS)
                .build();
    }
}
