package org.javaloong.kongmink.open.account.keycloak.internal;

import org.javaloong.kongmink.open.account.UserProvider;
import org.javaloong.kongmink.open.account.exception.UserException;
import org.javaloong.kongmink.open.account.keycloak.internal.exception.NotImplementedException;
import org.javaloong.kongmink.open.account.keycloak.internal.mapper.UserMapper;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.common.user.UserEmail;
import org.javaloong.kongmink.open.common.user.UserPassword;
import org.javaloong.kongmink.open.common.user.UserProfile;
import org.keycloak.representations.account.UserRepresentation;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

@Component(service = UserProvider.class)
public class KeycloakUserProvider implements UserProvider {

    private final KeycloakAccountClient accountClient;

    @Activate
    public KeycloakUserProvider(@Reference KeycloakAccountClient accountClient) {
        this.accountClient = accountClient;
    }

    @Override
    public User getUser() {
        return UserMapper.mapToUser(getUserRepresentation());
    }

    @Override
    public void updateProfile(UserProfile userProfile) {
        UserRepresentation userRepresentation = getUserRepresentation();
        userRepresentation.setAttributes(UserMapper.mapToUserAttributes(userProfile));
        accountClient.getAccountRestService().updateAccount(userRepresentation);
    }

    @Override
    public void updatePassword(UserPassword userPassword) {
        throw new NotImplementedException();
    }

    @Override
    public void updateEmail(UserEmail userEmail) {
        UserRepresentation userRepresentation = getUserRepresentation();
        if (!userEmail.getEmail().equalsIgnoreCase(userRepresentation.getEmail())) {
            userRepresentation.setEmail(userEmail.getEmail());
            userRepresentation.setEmailVerified(false);
            try {
                accountClient.getAccountRestService().updateAccount(userRepresentation);
            } catch (ClientErrorException ex) {
                if (ex.getResponse().getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                    throw UserException.emailExistsException();
                }
                throw ex;
            }
        }
    }

    private UserRepresentation getUserRepresentation() {
        return accountClient.getAccountRestService().account(false);
    }
}
