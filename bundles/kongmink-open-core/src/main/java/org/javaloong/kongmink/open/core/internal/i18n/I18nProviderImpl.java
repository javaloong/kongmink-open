package org.javaloong.kongmink.open.core.internal.i18n;

import org.javaloong.kongmink.open.core.i18n.LocaleProvider;
import org.javaloong.kongmink.open.core.i18n.TranslationProvider;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

@Component(immediate = true, configurationPid = I18nProviderImpl.CONFIGURATION_PID)
public class I18nProviderImpl implements TranslationProvider, LocaleProvider {

    private final Logger logger = LoggerFactory.getLogger(I18nProviderImpl.class);

    public static final String CONFIGURATION_PID = "org.javaloong.kongmink.open.i18n";

    // LocaleProvider
    public static final String LANGUAGE = "language";
    public static final String SCRIPT = "script";
    public static final String REGION = "region";
    public static final String VARIANT = "variant";
    private Locale locale;

    // TranslationProvider
    private final ResourceBundleTracker resourceBundleTracker;

    @Activate
    @SuppressWarnings("unchecked")
    public I18nProviderImpl(ComponentContext componentContext) {
        modified((Map<String, Object>) componentContext.getProperties());

        this.resourceBundleTracker = new ResourceBundleTracker(componentContext.getBundleContext(), this);
        this.resourceBundleTracker.open();
    }

    @Deactivate
    protected void deactivate(ComponentContext componentContext) {
        this.resourceBundleTracker.close();
    }

    @Modified
    protected synchronized void modified(Map<String, Object> config) {
        final String language = toStringOrNull(config.get(LANGUAGE));
        final String script = toStringOrNull(config.get(SCRIPT));
        final String region = toStringOrNull(config.get(REGION));
        final String variant = toStringOrNull(config.get(VARIANT));

        setLocale(language, script, region, variant);
    }

    private void setLocale(String language, String script, String region,
                           String variant) {
        Locale oldLocale = this.locale;
        if (language == null || language.isEmpty()) {
            // at least the language must be defined otherwise the system default locale is used
            logger.debug("No language set, setting locale to 'null'.");
            locale = null;
            if (oldLocale != null) {
                logger.info("Locale is not set, falling back to the default locale");
            }
            return;
        }

        final Locale.Builder builder = new Locale.Builder();
        try {
            builder.setLanguage(language);
        } catch (final RuntimeException ex) {
            logger.warn("Language ({}) is invalid. Cannot create locale, keep old one.", language, ex);
            return;
        }

        try {
            builder.setScript(script);
        } catch (final RuntimeException ex) {
            logger.warn("Script ({}) is invalid. Skip it.", script, ex);
            return;
        }

        try {
            builder.setRegion(region);
        } catch (final RuntimeException ex) {
            logger.warn("Region ({}) is invalid. Skip it.", region, ex);
            return;
        }

        try {
            builder.setVariant(variant);
        } catch (final RuntimeException ex) {
            logger.warn("Variant ({}) is invalid. Skip it.", variant, ex);
            return;
        }
        final Locale newLocale = builder.build();
        locale = newLocale;

        if (!newLocale.equals(oldLocale)) {
            logger.info("Locale set to '{}'.", newLocale);
        }
    }

    private String toStringOrNull(Object value) {
        return value == null ? null : value.toString();
    }

    @Override
    public Locale getLocale() {
        final Locale locale = this.locale;
        if (locale == null) {
            return Locale.getDefault();
        }
        return locale;
    }

    @Override
    public String getText(Bundle bundle, String key, String defaultText, Locale locale) {
        LanguageResourceBundleManager languageResource = this.resourceBundleTracker.getLanguageResource(bundle);

        if (languageResource != null) {
            String text = languageResource.getText(key, locale);
            if (text != null) {
                return text;
            }
        }

        return defaultText;
    }

    @Override
    public String getText(Bundle bundle, String key, String defaultText, Locale locale, Object... arguments) {
        String text = getText(bundle, key, defaultText, locale);

        if (text != null) {
            return MessageFormat.format(text, arguments);
        }

        return null;
    }
}
