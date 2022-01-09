package org.javaloong.kongmink.open.am.embedded.keycloak;

import org.apache.commons.lang.StringUtils;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.services.managers.ApplianceBootstrap;
import org.keycloak.services.managers.RealmManager;
import org.keycloak.services.resources.KeycloakApplication;
import org.keycloak.services.util.JsonConfigProviderFactory;
import org.keycloak.util.JsonSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.NoSuchElementException;

public class EmbeddedKeycloakApplication extends KeycloakApplication {

    private static final Logger log = LoggerFactory.getLogger(EmbeddedKeycloakApplication.class);

    public EmbeddedKeycloakApplication() {
        this(new KeycloakServerProperties());
    }

    public EmbeddedKeycloakApplication(KeycloakServerProperties serverProperties) {
        super();
        createMasterRealmAdminUser(serverProperties.getAdminUser(), serverProperties.getAdminPassword());
        createConfigRealms(serverProperties.getRealmConfigPath());
    }

    protected void loadConfig() {
        JsonConfigProviderFactory factory = new RegularJsonConfigProviderFactory();
        Config.init(factory.create().orElseThrow(() -> new NoSuchElementException("No value present")));
    }

    private void createMasterRealmAdminUser(String adminUser, String adminUserPassword) {
        KeycloakSession session = getSessionFactory().create();
        ApplianceBootstrap applianceBootstrap = new ApplianceBootstrap(session);
        try {
            session.getTransactionManager().begin();
            applianceBootstrap.createMasterRealmUser(adminUser, adminUserPassword);
            session.getTransactionManager().commit();
        } catch (Exception ex) {
            log.warn("Couldn't create keycloak master admin user: {}", ex.getMessage());
            session.getTransactionManager().rollback();
        }
        session.close();
    }

    private void createConfigRealms(String path) {
        String[] realmPaths = StringUtils.split(path, ',');
        for (String realmPath: realmPaths) {
            createConfigRealm(realmPath);
        }
    }

    private void createConfigRealm(String path) {
        KeycloakSession session = getSessionFactory().create();
        try {
            session.getTransactionManager().begin();
            RealmManager manager = new RealmManager(session);
            InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
            if (stream == null) {
                log.error("Unable to find keycloak realm  in file: {}", path);
                return;
            }
            manager.importRealm(JsonSerialization.readValue(stream, RealmRepresentation.class));
            session.getTransactionManager().commit();
        } catch (Exception ex) {
            log.warn("Failed to import Realm json file: {}", ex.getMessage());
            session.getTransactionManager().rollback();
        }
        session.close();
    }

    public static class RegularJsonConfigProviderFactory extends JsonConfigProviderFactory {

    }
}
