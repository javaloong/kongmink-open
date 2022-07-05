package org.javaloong.kongmink.open.rest.auth.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.SignedJWT;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.jwt.config.signature.RSASignatureConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RemoteJwkRSASignatureConfiguration extends RSASignatureConfiguration {

    private int connectTimeout = RemoteJWKSet.DEFAULT_HTTP_CONNECT_TIMEOUT;

    private int readTimeout = RemoteJWKSet.DEFAULT_HTTP_READ_TIMEOUT;

    private int sizeLimit = RemoteJWKSet.DEFAULT_HTTP_SIZE_LIMIT;

    private JWKSource<SecurityContext> jwkSource;

    private void initRSAKeys(final SignedJWT jwt) {
        try {
            JWKSelector jwkSelector = new JWKSelector(JWKMatcher.forJWSHeader(jwt.getHeader()));
            List<JWK> jwkList = jwkSource.get(jwkSelector, null);
            if (jwkList != null && jwkList.size() > 0) {
                RSAKey rsaKey = jwkList.iterator().next().toRSAKey();
                setPublicKey(rsaKey.toRSAPublicKey());
                setPrivateKey(rsaKey.toRSAPrivateKey());
            }
        } catch (JOSEException e) {
            throw new TechnicalException(e);
        }
    }

    @Override
    public boolean verify(SignedJWT jwt) throws JOSEException {
        initRSAKeys(jwt);
        return super.verify(jwt);
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setSizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    public void setJwkSetUri(String jwkSetUri) {
        try {
            ResourceRetriever resourceRetriever = new DefaultResourceRetriever(connectTimeout, readTimeout, sizeLimit);
            this.jwkSource = new RemoteJWKSet<>(new URL(jwkSetUri), resourceRetriever);
        } catch (MalformedURLException e) {
            throw new TechnicalException(e);
        }
    }
}
