package org.javaloong.kongmink.open.apim.gravitee.internal.mapper;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.ApplicationEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.CategoryEntity;
import org.javaloong.kongmink.open.apim.model.Application;
import org.javaloong.kongmink.open.apim.model.Category;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.module.jsr310.Jsr310Module;

import java.lang.reflect.Type;

public class BeanMapper {

    private static final ModelMapper modelMapper = createModelMapper();

    public static <D> D map(Object source, Class<D> destinationType) {
        return modelMapper.map(source, destinationType);
    }

    public static <D> D map(Object source, Type destinationType) {
        return modelMapper.map(source, destinationType);
    }

    public static void map(Object source, Object destination) {
        modelMapper.map(source, destination);
    }

    private static ModelMapper createModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.registerModule(new Jsr310Module());
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setPropertyCondition(Conditions.isNotNull());
        modelMapper.typeMap(ApplicationEntity.class, Application.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getSettings().getApp().getClientId(), Application::setClientId);
                    mapper.map(src -> src.getSettings().getApp().getType(), Application::setClientType);
                });
        modelMapper.typeMap(CategoryEntity.class, Category.class)
                .addMappings(new PropertyMap<>() {
                    @Override
                    protected void configure() {
                        map(source.getTotalApis(), destination.getTotal());
                    }
                });
        return modelMapper;
    }
}
