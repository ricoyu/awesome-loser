package com.loserico.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loserico.common.lang.utils.IOUtils;
import lombok.SneakyThrows;
import org.junit.Test;

public class CustomizeObjectMapperTest {

    @SneakyThrows
    @Test
    public void test() {
        ObjectMapper objectMapper = new ObjectMapper();
        String tasks = IOUtils.readClassPathFileAsString("tasks.json");
        String value = objectMapper.readValue(tasks, String.class);
        System.out.println(value);
    }
}
