package org.javaloong.kongmink.open.apim.gravitee.internal.mapper;

import org.javaloong.kongmink.open.apim.model.Category;
import org.javaloong.kongmink.open.apim.gravitee.internal.mapper.BeanMapper;
import org.modelmapper.TypeToken;

import java.util.Collection;

public class CategoryMapper {

    public static Collection<Category> mapToCategories(Collection<?> data) {
        TypeToken<Collection<Category>> typeToken = new TypeToken<>() {
        };
        return BeanMapper.map(data, typeToken.getType());
    }
}
