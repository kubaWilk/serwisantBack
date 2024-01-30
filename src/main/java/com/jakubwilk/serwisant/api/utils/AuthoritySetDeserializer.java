package com.jakubwilk.serwisant.api.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jakubwilk.serwisant.api.entity.Role;
import com.jakubwilk.serwisant.api.entity.jpa.Authority;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AuthoritySetDeserializer extends JsonDeserializer<Set<Authority>> {

    @Override
    public Set<Authority> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        ArrayNode arrayNode = jsonParser.getCodec().readTree(jsonParser);
        Set<Authority> authorities = new HashSet<>();

        for (int i = 0; i < arrayNode.size(); i++) {
            String authorityString = arrayNode.get(i).asText();
            Role role = Role.valueOf(authorityString);

            Authority authority = new Authority(null,null,role);

            authorities.add(authority);
        }

        return authorities;
    }
}
