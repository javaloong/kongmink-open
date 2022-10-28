package org.javaloong.kongmink.open.service;

import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.model.user.query.UserQuery;
import org.javaloong.kongmink.open.service.dto.UserDTO;

import java.util.Map;
import java.util.Optional;

public interface UserService {

    Map<String, Object> getConfig(String userId);

    void updateConfig(String userId, Map<String, Object> configMap);

    void setConfig(String userId, Map<String, Object> configMap);

    Optional<UserDTO> findById(String userId);

    Page<UserDTO> findAll(UserQuery query, int page, int size);
}
