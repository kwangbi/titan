package com.greece.titan.common.redis.config;


import com.greece.titan.common.redis.CacheConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Redis Properties
 * @author P125030
 *
 */
@Configuration
@ConfigurationProperties(prefix = CacheConstants.PREFIX_DEFAULT_REDIS_PROPERTIES)
public class LoaderProperties {
    /** initialize option */
    private boolean initCache = true;

    /** initialize reset option */
    private boolean initReset = false;

    public boolean isInitCache() {
        return initCache;
    }
    public void setInitCache(final boolean initCache) {
        this.initCache = initCache;
    }
    public boolean isInitReset() {
        return initReset;
    }
    public void setInitReset(final boolean initReset) {
        this.initReset = initReset;
    }
}
