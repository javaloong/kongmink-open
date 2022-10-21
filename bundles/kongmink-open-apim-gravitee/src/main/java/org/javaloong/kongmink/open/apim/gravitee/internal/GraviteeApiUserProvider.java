package org.javaloong.kongmink.open.apim.gravitee.internal;

import com.nimbusds.jwt.SignedJWT;
import org.javaloong.kongmink.open.apim.ApiUserProvider;
import org.javaloong.kongmink.open.apim.exception.TokenException;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.TokenEntity;
import org.javaloong.kongmink.open.common.model.ApiUser;
import org.javaloong.kongmink.open.common.model.User;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

@Component(service = ApiUserProvider.class)
public class GraviteeApiUserProvider implements ApiUserProvider {

    private static final Logger log = LoggerFactory.getLogger(GraviteeApiUserProvider.class);

    private final GraviteePortalClient client;

    @Activate
    public GraviteeApiUserProvider(@Reference GraviteePortalClient client) {
        this.client = client;
    }

    @Override
    public ApiUser connectUser(User user) {
        try {
            TokenEntity tokenEntity = client.tokenExchange();
            SignedJWT decodedJWT = SignedJWT.parse(tokenEntity.getToken());
            return mapToApiUser(user, decodedJWT);
        } catch (ParseException e) {
            log.error("Invalid token!", e);
            throw TokenException.invalidToken(e);
        }
    }

    private ApiUser mapToApiUser(User user, SignedJWT decodedJWT) throws ParseException {
        ApiUser apiUser = new ApiUser();
        apiUser.setId(decodedJWT.getJWTClaimsSet().getSubject());
        apiUser.setSource(client.getSource());
        apiUser.setSourceId(user.getId());
        return apiUser;
    }
}
