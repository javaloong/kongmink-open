package org.javaloong.kongmink.open.am.users.keycloak.internal;

import org.javaloong.kongmink.open.am.users.model.Password;
import org.javaloong.kongmink.open.am.users.model.User;
import org.javaloong.kongmink.open.am.users.model.UserProfile;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserMapper {

    public static final String COMPANY_NAME_KEY = "companyName";
    public static final String COMPANY_PROVINCE_KEY = "companyProvince";
    public static final String COMPANY_CITY_KEY = "companyCity";
    public static final String COMPANY_ADDRESS_KEY = "companyAddress";
    public static final String CONTACT_NAME_KEY = "contactName";
    public static final String CONTACT_PHONE_KEY = "contactPhone";

    public static User mapToUser(UserRepresentation userRepresentation) {
        User user = new User();
        user.setId(userRepresentation.getId());
        user.setUsername(userRepresentation.getUsername());
        user.setEmail(userRepresentation.getEmail());
        user.setEmailVerified(userRepresentation.isEmailVerified());
        user.setEnabled(userRepresentation.isEnabled());
        user.setProfile(mapToUserProfile(userRepresentation));
        user.setCreatedTimestamp(LocalDateTime.ofInstant(
                Instant.ofEpochMilli(userRepresentation.getCreatedTimestamp()), ZoneId.systemDefault()));
        return user;
    }

    public static UserRepresentation mapToUserRepresentation(User user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEnabled(user.isEnabled());
        if(user.getPassword() != null) {
            userRepresentation.setCredentials(
                    Collections.singletonList(mapToPasswordCredential(user.getPassword())));
        }
        if(user.getProfile() != null) {
            userRepresentation.setAttributes(mapToUserAttributes(user.getProfile()));
        }
        return userRepresentation;
    }

    public static CredentialRepresentation mapToPasswordCredential(Password password) {
        CredentialRepresentation passwordCredential = new CredentialRepresentation();
        passwordCredential.setTemporary(false);
        passwordCredential.setType(CredentialRepresentation.PASSWORD);
        passwordCredential.setValue(password.getPasswordNew());
        return passwordCredential;
    }

    public static Map<String, List<String>> mapToUserAttributes(UserProfile userProfile) {
        Map<String, List<String>> attributes = new LinkedHashMap<>();
        setUserAttribute(attributes, COMPANY_NAME_KEY, userProfile.getCompanyName());
        setUserAttribute(attributes, COMPANY_PROVINCE_KEY, userProfile.getCompanyProvince());
        setUserAttribute(attributes, COMPANY_CITY_KEY, userProfile.getCompanyCity());
        setUserAttribute(attributes, COMPANY_ADDRESS_KEY, userProfile.getCompanyAddress());
        setUserAttribute(attributes, CONTACT_NAME_KEY, userProfile.getContactName());
        setUserAttribute(attributes, CONTACT_PHONE_KEY, userProfile.getContactPhone());
        return attributes;
    }

    public static UserProfile mapToUserProfile(Map<String, List<String>> attributes) {
        UserProfile userProfile = new UserProfile();
        userProfile.setCompanyName(getUserAttribute(attributes, COMPANY_NAME_KEY));
        userProfile.setCompanyProvince(getUserAttribute(attributes, COMPANY_PROVINCE_KEY));
        userProfile.setCompanyCity(getUserAttribute(attributes, COMPANY_CITY_KEY));
        userProfile.setCompanyAddress(getUserAttribute(attributes, COMPANY_ADDRESS_KEY));
        userProfile.setContactName(getUserAttribute(attributes, CONTACT_NAME_KEY));
        userProfile.setContactPhone(getUserAttribute(attributes, CONTACT_PHONE_KEY));
        return userProfile;
    }

    public static UserProfile mapToUserProfile(UserRepresentation userRepresentation) {
        UserProfile userProfile = mapToUserProfile(userRepresentation.getAttributes());
        userProfile.setUserId(userRepresentation.getId());
        return userProfile;
    }

    private static String getUserAttribute(Map<String, List<String>> attributes, String key) {
        String value = null;
        if(attributes != null && attributes.containsKey(key) && !attributes.get(key).isEmpty()) {
            value = attributes.get(key).get(0);
        }
        return value;
    }

    private static void setUserAttribute(Map<String, List<String>> attributes, String key, Object value) {
        if(value != null) {
            attributes.put(key, Collections.singletonList((String)value));
        }
        else {
            attributes.put(key, Collections.emptyList());
        }
    }
}
