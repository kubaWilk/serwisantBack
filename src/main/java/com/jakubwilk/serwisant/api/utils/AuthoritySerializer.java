package com.jakubwilk.serwisant.api.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jakubwilk.serwisant.api.entity.jpa.Authority;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthoritySerializer extends JsonSerializer<Set<Authority>> {
    @Override
    public void serialize(Set<Authority> authorities, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String[] authorityArray = authorities.stream()
                .map(authority -> authority.getAuthority().toString())
                .toArray(String[]::new);
        jsonGenerator.writeObject(authorityArray);
    }
}