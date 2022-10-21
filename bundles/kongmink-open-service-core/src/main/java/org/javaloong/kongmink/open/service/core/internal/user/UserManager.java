package org.javaloong.kongmink.open.service.core.internal.user;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.javaloong.kongmink.open.common.model.User;
import org.javaloong.kongmink.open.data.UserRepository;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.transaction.control.TransactionControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;

@Component(service = UserManager.class)
@Designate(ocd = UserManager.Config.class)
public class UserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

    @ObjectClassDefinition(name = "User Manager Service Configuration")
    public @interface Config {

        long cacheExpireAfterAccess() default 600_000;

        long cacheMaximumSize() default 10_000;
    }

    private final Cache<String, User> cache;

    @Reference
    TransactionControl transactionControl;
    @Reference
    UserRepository userRepository;

    @Activate
    public UserManager(Config config) {
        this.cache = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMillis(config.cacheExpireAfterAccess()))
                .maximumSize(config.cacheMaximumSize())
                .build();
    }

    public User loadByUser(User user, boolean useCache) {
        if (useCache) {
            return cache.get(user.getId(), key -> mergeToUser(user, saveEntity(user)));
        }
        return mergeToUser(user, saveEntity(user));
    }

    private UserEntity createEntity(User user) {
        UserEntity entity = userRepository.create(mapToUserEntity(user));
        logger.debug("User[id={}, username={}] successfully added to repository",
                entity.getId(), entity.getUsername());
        return entity;
    }

    private UserEntity updateEntity(User user, UserEntity entity) {
        if (!user.getUsername().equals(entity.getUsername())) {
            entity.setUsername(user.getUsername());
            entity.setUpdatedAt(LocalDateTime.now());
            userRepository.update(entity);
            logger.debug("User[id={}, username={}] successfully updated to repository",
                    entity.getId(), entity.getUsername());
        }
        return entity;
    }

    private UserEntity saveEntity(User user) {
        return transactionControl.required(() -> userRepository.findById(user.getId())
                .map(entity -> updateEntity(user, entity))
                .orElseGet(() -> createEntity(user)));
    }

    private UserEntity mapToUserEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    private User mergeToUser(User user, UserEntity entity) {
        user.setCreatedAt(entity.getCreatedAt());
        return user;
    }
}
