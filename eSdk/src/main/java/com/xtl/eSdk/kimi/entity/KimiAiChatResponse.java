package com.xtl.eSdk.kimi.entity;

import lombok.Data;

@Data
public class KimiAiChatResponse {
    private String id;
    private String object;
    private long created;
    private String model; // 模型
    private Choice[] choices; // 聊天响应内容
    private Usage usage; // token相关

    @Data
    public static class Choice {
        private int index;
        private Message message;
        private String finish_reason;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }

    @Data
    public static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
    }
}
