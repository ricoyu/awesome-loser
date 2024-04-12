package com.loserico.jackson.deserialize;

import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jackson.JacksonUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestDeserialize {

    @Test
    public void testDeserialize() {
        String body = IOUtils.readClassPathFileAsString("requestBody.json");
        RequestDTO object = JacksonUtils.toObject(body, RequestDTO.class);
        assertEquals(object.getBizId(), "W00002");
    }

    @Test
    public void testFormatJson() {
        String body = IOUtils.readClassPathFileAsString("requestBody.json");
        String formated = JacksonUtils.formatJsonString(body);
        System.out.println(formated);
    }
}
