package com.loserico.openai;

import com.loserico.networking.utils.HttpUtils;
import org.junit.Test;

public class HelloOpenAI {

    @Test
    public void test1stApi() {
        String response = HttpUtils.post("https://api.openai.com/v1/chat/completions")
                //.addHeader("Content-Type", "application/json")
                //.bearerAuth("sk-proj-s21iDcjCegLcr8Ew9F39T3BlbkFJ0Dri2mwLmwylljFM2ocU")
                .body("{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"system\", \"content\": \"You are a\"}]}")
                .request();
        String.format()
        System.out.println(response);
    }
}
