package org.javaloong.kongmink.open.am.keycloak.internal;

import org.apache.commons.lang3.StringUtils;
import org.javaloong.kongmink.open.am.UserProvider;
import org.javaloong.kongmink.open.am.exception.UserException;
import org.javaloong.kongmink.open.am.keycloak.internal.exception.NotImplementedException;
import org.javaloong.kongmink.open.am.keycloak.internal.mapper.UserMapper;
import org.javaloong.kongmink.open.am.keycloak.internal.resource.UserResource;
import org.javaloong.kongmink.open.am.keycloak.internal.resource.UsersResource;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.common.user.UserEmail;
import org.javaloong.kongmink.open.common.user.UserPassword;
import org.javaloong.kongmink.open.common.user.UserProfile;
import org.keycloak.representations.idm.UserRepresentation;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Component(service = UserProvider.class)
public class KeycloakUserProvider implements UserProvider {

    private static final Logger log = LoggerFactory.getLogger(KeycloakUserProvider.class);

    private final KeycloakUserAdminClient adminClient;

    @Activate
    public KeycloakUserProvider(@Reference KeycloakUserAdminClient adminClient) {
        this.adminClient = adminClient;
    }

    @Override
    public Optional<User> findById(String userId) {
        Optional<UserResource> userResource = getUserResource(userId);
        return userResource.flatMap(
                resource -> getUserRepresentation(resource)
                        .map(UserMapper::mapToUser));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        UsersResource usersResource = adminClient.getUsersResource();
        List<UserRepresentation> userList = usersResource.search(username, true);
        Optional<User> result = Optional.empty();
        if (userList.size() > 0) {
            result = Optional.of(UserMapper.mapToUser(userList.get(0)));
        }
        return result;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        UsersResource usersResource = adminClient.getUsersResource();
        List<UserRepresentation> userList = usersResource.search(null, null, null, email, 0, 1);
        Optional<User> result = Optional.empty();
        if (userList.size() > 0) {
            result = Optional.of(UserMapper.mapToUser(userList.get(0)));
        }
        return result;
    }

    @Override
    public User create(User user) {
        verifyUsername(user.getUsername());
        if (StringUtils.isNotEmpty(user.getEmail())) {
            verifyEmail(user.getEmail());
        }
        UsersResource usersResource = adminClient.getUsersResource();
        UserRepresentation userRepresentation = UserMapper.mapToUserRepresentation(user);
        Response response = usersResource.create(userRepresentation);
        log.info("Response Code: " + response.getStatusInfo());
        if (response.getStatusInfo().equals(Response.Status.CREATED)) {
            log.info("Response Location: " + response.getLocation());
        }
        String userId = JAXRSClientUtils.getCreatedId(response);
        user.setId(userId);
        return user;
    }

    @Override
    public void updateProfile(UserProfile userProfile) {
        Optional<UserResource> userResource = getUserResource(userProfile.getUserId());
        userResource.ifPresent(resource -> {
            UserRepresentation userRepresentation = resource.toRepresentation();
            userRepresentation.setAttributes(UserMapper.mapToUserAttributes(userProfile));
            resource.update(userRepresentation);
        });
    }

    @Override
    public void updatePassword(UserPassword userPassword) {
        throw new NotImplementedException();
    }

    @Override
    public void updateEmail(UserEmail userEmail) {
        Optional<UserResource> userResource = getUserResource(userEmail.getUserId());
        userResource.ifPresent(resource -> {
            UserRepresentation userRepresentation = resource.toRepresentation();
            if (!userEmail.getEmail().equalsIgnoreCase(userRepresentation.getEmail())) {
                verifyEmail(userEmail.getEmail());
                userRepresentation.setEmail(userEmail.getEmail());
                resource.update(userRepresentation);
            }
        });
    }

    @Override
    public void delete(String userId) {
        Optional<UserResource> userResource = getUserResource(userId);
        userResource.ifPresent(UserResource::remove);
    }

    private Optional<UserResource> getUserResource(String id) {
        return Optional.ofNullable(adminClient.getUsersResource().get(id));
    }

    private Optional<UserRepresentation> getUserRepresentation(UserResource resource) {
        try {
            return Optional.ofNullable(resource.toRepresentation());
        } catch (NotFoundException ex) {
            return Optional.empty();
        }
    }

    private void verifyUsername(String username) {
        Optional<User> result = findByUsername(username);
        if (result.isPresent()) {
            throw UserException.usernameExistsException();
        }
    }

    private void verifyEmail(String email) {
        Optional<User> result = findByEmail(email);
        if (result.isPresent()) {
            throw UserException.emailExistsException();
        }
    }
}
