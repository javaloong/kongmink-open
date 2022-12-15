package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.CategoryProvider;
import org.javaloong.kongmink.open.common.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("gravitee")
public class GraviteeCategoryProviderIT extends GraviteePortalClientTestSupport {

    private CategoryProvider categoryProvider;

    @BeforeEach
    public void setUp() {
        categoryProvider = new GraviteeCategoryProvider(createPortalClient());
    }

    @Test
    public void findAll() {
        Collection<Category> result = categoryProvider.findAll(-1);
        assertThat(result).isNotEmpty().hasSize(1)
                .extracting(Category::getName).contains("Official");
    }
}
