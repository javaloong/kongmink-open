package org.javaloong.kongmink.open.am.embedded.keycloak;

public class KeycloakServerProperties {

    public static final String SERVER_CONTEXT_PATH = "keycloak.embedded.server.context-path";
    public static final String REALM_CONFIGURATION_PATH = "keycloak.embedded.realm.configuration.path";
    public static final String ADMIN_USERNAME = "keycloak.embedded.security.admin.username";
    public static final String ADMIN_PASSWORD = "keycloak.embedded.security.admin.password";

    private final String contextPath;
    private final String realmConfigPath;
    private final String adminUser;
    private final String adminPassword;

    public KeycloakServerProperties() {
        this.contextPath = System.getProperty(SERVER_CONTEXT_PATH, "/auth");
        this.realmConfigPath = System.getProperty(REALM_CONFIGURATION_PATH, "demo-realm.json");
        this.adminUser = System.getProperty(ADMIN_USERNAME, "admin");
        this.adminPassword = System.getProperty(ADMIN_PASSWORD, "admin");
    }

    public KeycloakServerProperties(String contextPath, String realmConfigPath,
                                    String adminUser, String adminPassword) {
        this.contextPath = contextPath;
        this.realmConfigPath = realmConfigPath;
        this.adminUser = adminUser;
        this.adminPassword = adminPassword;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getRealmConfigPath() {
        return realmConfigPath;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public String getAdminPassword() {
        return adminPassword;
    }
}
