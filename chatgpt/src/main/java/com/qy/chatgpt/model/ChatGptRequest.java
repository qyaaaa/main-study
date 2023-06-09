package com.qy.chatgpt.model;

import lombok.Data;

@Data
public class ChatGptRequest {
    // 模型: https://platform.openai.com/docs/api-reference/models/list
    private String model = "text-davinci-003";
    private String prompt;
    private Integer max_tokens = 256;
    private float temperature = 0.5f;
    private Integer top_p = 1;
    private Integer n = 1;
    private Boolean stream = false;
    private Boolean logprobs;
    private String stop;
}
