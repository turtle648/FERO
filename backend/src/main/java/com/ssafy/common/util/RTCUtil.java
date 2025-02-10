package com.ssafy.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.common.model.Message;

public class RTCUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // JSON -> Java Objcet (deserialization)
    public static Message getObject(final String message) throws Exception {
        return objectMapper.readValue(message, Message.class);
    }

    // Java Object -> JSON (serialization)
    public static String getString(final Message message) throws Exception {
        return objectMapper.writeValueAsString(message);
    }
}
