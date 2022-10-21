package org.javaloong.kongmink.open.apim;

import org.javaloong.kongmink.open.common.model.Category;

import java.util.Collection;

public interface CategoryProvider {

    Collection<Category> findAll(int size);
}
