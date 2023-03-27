package com.qy.chatgpt.model;

import lombok.Data;

@Data
public class ChatGptChina {

    private String apiKey;

    private String sessionId;

    private String content;
}
