package com.splitnice.authservice.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.splitnice.authservice.model.UserInfoDto;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserInfoSerializer implements Serializer<UserInfoDto> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, UserInfoDto data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing UserInfoDto", e);
        }
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
