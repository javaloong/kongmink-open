package org.javaloong.kongmink.open.rest.auth.jwt.profile;

import net.minidev.json.JSONObject;
import org.pac4j.core.profile.AttributeLocation;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.converter.Converters;
import org.pac4j.core.profile.definition.CommonProfileDefinition;
import org.pac4j.jwt.profile.JwtProfile;

import java.util.Arrays;

public class JwtUserProfileDefinition extends CommonProfileDefinition<JwtProfile> {

    public static final String PREFERRED_USERNAME = "preferred_username";
    public static final String EMAIL_VERIFIED = "email_verified";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String PHONE_NUMBER_VERIFIED = "phone_number_verified";
    public static final String ACCESS_TOKEN = "access_token";

    private boolean keepNestedAttributes = true;

    public JwtUserProfileDefinition() {
        super(x -> new JwtUserProfile());
        Arrays.stream(new String[]{PREFERRED_USERNAME, PHONE_NUMBER,
                ACCESS_TOKEN}).forEach(a -> primary(a, Converters.STRING));
        primary(EMAIL_VERIFIED, Converters.BOOLEAN);
        primary(PHONE_NUMBER_VERIFIED, Converters.BOOLEAN);
    }

    @Override
    public void convertAndAdd(final CommonProfile profile, final AttributeLocation attributeLocation,
                              final String name, final Object value) {
        if (value instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) value;
            jsonObject.forEach((key, objectValue) -> super.convertAndAdd(profile, attributeLocation, key, objectValue));
            if (keepNestedAttributes) {
                super.convertAndAdd(profile, attributeLocation, name, value);
            }
        } else {
            super.convertAndAdd(profile, attributeLocation, name, value);
        }
    }

    public void setKeepNestedAttributes(boolean keepNestedAttributes) {
        this.keepNestedAttributes = keepNestedAttributes;
    }
}
