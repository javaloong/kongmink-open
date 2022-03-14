package org.javaloong.kongmink.open.apim.gravitee.internal;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.javaloong.kongmink.open.apim.exception.TokenException;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.TokenEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.function.Function;

public class GraviteeTokenManager {

    private static final Logger log = LoggerFactory.getLogger(GraviteeTokenManager.class);

    private final Cache<String, TokenEntity> cache;

    public GraviteeTokenManager(GraviteePortalClientConfiguration config) {
        this.cache = createCache(config);
    }

    private Cache<String, TokenEntity> createCache(GraviteePortalClientConfiguration config) {
        return Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMillis(config.cacheExpireAfterAccess()))
                .maximumSize(config.cacheMaximumSize())
                .build();
    }

    public TokenEntity getToken(String accessToken, Function<String, TokenEntity> tokenFunc) {
        log.debug("Retrieving token from cache");
        if (accessToken != null) {
            return cache.get(accessToken, tokenFunc);
        }
        throw TokenException.tokenNotExits();
    }
}
