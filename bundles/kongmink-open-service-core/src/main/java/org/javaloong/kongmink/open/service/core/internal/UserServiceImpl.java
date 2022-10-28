package org.javaloong.kongmink.open.service.core.internal;

import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.model.user.query.UserQuery;
import org.javaloong.kongmink.open.data.repository.UserRepository;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.javaloong.kongmink.open.service.UserService;
import org.javaloong.kongmink.open.service.dto.UserDTO;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.transaction.control.TransactionControl;

import java.util.*;
import java.util.stream.Collectors;

@Component(service = UserService.class)
public class UserServiceImpl implements UserService {

    @Reference
    TransactionControl transactionControl;
    @Reference
    UserRepository userRepository;

    @Override
    public Map<String, Object> getConfig(String userId) {
        return transactionControl.supports(() ->
                userRepository.findById(userId).map(UserEntity::getConfigData)
                        .orElse(Collections.emptyMap()));
    }

    @Override
    public void updateConfig(String userId, Map<String, Object> configMap) {
        transactionControl.required(() -> {
            userRepository.findById(userId).ifPresent(entity -> {
                if (entity.getConfigData() == null) {
                    entity.setConfigData(new HashMap<>());
                }
                configMap.forEach(entity.getConfigData()::put);
                userRepository.update(entity);
            });
            return null;
        });
    }

    @Override
    public void setConfig(String userId, Map<String, Object> configMap) {
        transactionControl.required(() -> {
            userRepository.findById(userId).ifPresent(entity -> {
                entity.setConfigData(configMap);
                userRepository.update(entity);
            });
            return null;
        });
    }

    @Override
    public Optional<UserDTO> findById(String userId) {
        return transactionControl.supports(() -> userRepository.findById(userId).map(this::mapToUserDTO));
    }

    @Override
    public Page<UserDTO> findAll(UserQuery query, int page, int size) {
        return transactionControl.notSupported(() -> {
            Page<UserEntity> result = userRepository.findAll(query, page, size);
            Collection<UserDTO> items = mapToUserDTOList(result.getData());
            return new Page<>(items, result.getTotalCount());
        });
    }

    private UserDTO mapToUserDTO(UserEntity entity) {
        UserDTO user = new UserDTO();
        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());
        return user;
    }

    private Collection<UserDTO> mapToUserDTOList(Collection<UserEntity> entities) {
        return entities.stream().map(this::mapToUserDTO).collect(Collectors.toList());
    }
}
