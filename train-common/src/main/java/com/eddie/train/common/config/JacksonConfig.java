//package com.eddie.train.common.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//
//@Configuration
//public class JacksonConfig {
//
//    @Bean
//    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
//        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//        SimpleMoudle simpleMoudle = new SimpleMoudle();
//        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
//        objectMapper.registerModule(simpleModule);
//        return objectMapper;
//    }
//}
