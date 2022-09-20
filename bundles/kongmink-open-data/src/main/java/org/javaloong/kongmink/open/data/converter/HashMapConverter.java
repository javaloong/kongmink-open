package org.javaloong.kongmink.open.data.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return attribute == null ? null : mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
        };
        try {
            return dbData == null ? null : mapper.readValue(dbData, typeRef);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
