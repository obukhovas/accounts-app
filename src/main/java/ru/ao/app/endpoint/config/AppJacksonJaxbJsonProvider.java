package ru.ao.app.endpoint.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class AppJacksonJaxbJsonProvider extends JacksonJaxbJsonProvider {

    private final ObjectMapper defaultObjectMapper;

    public AppJacksonJaxbJsonProvider() {
        super();
        defaultObjectMapper = createDefaultMapper();
        setMapper(defaultObjectMapper);
    }

    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper result = new ObjectMapper();
        result.enable(SerializationFeature.INDENT_OUTPUT);
        result.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return result;
    }
}
