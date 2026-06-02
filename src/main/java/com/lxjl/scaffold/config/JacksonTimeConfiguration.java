package com.lxjl.scaffold.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 统一时间格式：{@code yyyy-MM-dd HH:mm:ss}。
 */
@Configuration
public class JacksonTimeConfiguration {

    private static final DateTimeFormatter API = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonLocalDateTimeApiFormat() {
        return builder -> {
            builder.serializers(new LocalDateTimeSerializer(API));
            builder.deserializers(new LocalDateTimeDeserializer(API));
        };
    }
}
