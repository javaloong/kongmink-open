package org.javaloong.kongmink.open.core.internal.i18n;

import org.javaloong.kongmink.open.core.i18n.LocaleProvider;
import org.javaloong.kongmink.open.core.i18n.TranslationProvider;
import org.javaloong.kongmink.open.core.internal.AbstractTestSupport;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class I18nProviderImplIT extends AbstractTestSupport {

    @Inject
    LocaleProvider localeProvider;
    @Inject
    TranslationProvider i18nProvider;

    @Test
    public void testLocale() {
        Locale locale = localeProvider.getLocale();
        assertThat(locale)
                .returns("zh", Locale::getLanguage)
                .returns("CN", Locale::getCountry)
                .returns("zh-CN", Locale::toLanguageTag);
    }

    @Test
    public void testI18nMessages() {
        Bundle bundle = FrameworkUtil.getBundle(this.getClass());
        assertThat(getMessage(bundle, "NOT_EXIST", "不存在"))
                .isEqualTo("不存在");

        assertThat(getMessage(bundle, "SUCCESS", null))
                .isEqualTo("成功");
        assertThat(getMessage(bundle, "FAILED", null))
                .isEqualTo("Failed");

        assertThat(getMessage(bundle, "WELCOME", null, "小明"))
                .isEqualTo("你好，小明");

        assertThat(i18nProvider.getText(bundle, "WELCOME", null, Locale.ENGLISH, "Foo"))
                .isEqualTo("Hello, Foo");
    }

    private String getMessage(Bundle bundle, String key, String defaultText, Object... arguments) {
        return i18nProvider.getText(bundle, key, defaultText, localeProvider.getLocale(), arguments);
    }
}
