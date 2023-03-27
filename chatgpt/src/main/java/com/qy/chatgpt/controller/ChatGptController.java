package com.qy.chatgpt.controller;

import com.qy.chatgpt.model.ChatGptChina;
import com.qy.chatgpt.model.ChatGptRequest;
import com.squareup.moshi.Moshi;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/chat-gpt")
public class ChatGptController {

    @Value("${chat-gpt.apikey}")
    private String APIKEY;

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .build();

    /**
     * 直接访问chatgpt
     * @param prompt
     * @return
     */
    @PostMapping("/search")
    public String search(String prompt) {
        ChatGptRequest completionRequest = new ChatGptRequest();
        MediaType MediaTypeJson = MediaType.get("application/json");
        completionRequest.setPrompt(prompt);

        String reqJson = new Moshi.Builder().build().adapter(ChatGptRequest.class).toJson(completionRequest);
        System.out.println("reqJson: " + reqJson);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                // 将 API_KEY 替换成你自己的 API_KEY
                .header("Authorization", "Bearer " + APIKEY)
                .post(RequestBody.create(MediaTypeJson, reqJson))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 国内代理chatgpt https://www.openai-proxy.com/
     * @param content
     * @return
     */
    @PostMapping("/search-china")
    public String searchFromChina(String content) {
        ChatGptChina chatGptChina = new ChatGptChina();
        MediaType MediaTypeJson = MediaType.get("application/json");
        chatGptChina.setContent(content);
        chatGptChina.setApiKey(APIKEY);
        chatGptChina.setSessionId(UUID.randomUUID().toString());

        String reqJson = new Moshi.Builder().build().adapter(ChatGptChina.class).toJson(chatGptChina);
        System.out.println("reqJson: " + reqJson);
        Request request = new Request.Builder()
                .url("https://api.openai-proxy.com/pro/chat/completions")
                .post(RequestBody.create(MediaTypeJson, reqJson))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
