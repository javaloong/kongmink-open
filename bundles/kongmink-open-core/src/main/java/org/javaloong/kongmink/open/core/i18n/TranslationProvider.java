package org.javaloong.kongmink.open.core.i18n;

import org.osgi.framework.Bundle;

import java.util.Locale;

public interface TranslationProvider {

    String getText(Bundle bundle, String key, String defaultText, Locale locale);

    String getText(Bundle bundle, String key, String defaultText,
                   Locale locale, Object... arguments);
}
