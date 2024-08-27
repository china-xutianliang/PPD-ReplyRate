package com.xtl.eSdk.kimi.api;

import com.xtl.eSdk.kimi.entity.KimiAiChatResponse;
import com.xtl.ecore.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


@Slf4j
public class KimiApi {

    private static final String KIMI_CHAT_URL = "https://api.moonshot.cn/v1/chat/completions";

    private static final String KIMI_MODEL = "moonshot-v1-8k";

    public static String kimiAiChat(String systemMessage, String kimiKey,String userMessage) {
        String content = "";
        try {
            RestTemplate restTemplate = SpringUtils.getBean("restTemplate");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", kimiKey);

            String requestBody = String.format("{ \"model\": \"%s\", \"messages\": [ { \"role\": \"system\", \"content\": \"%s\" }, { \"role\": \"user\", \"content\": \"%s\" } ], \"temperature\": %f }", KIMI_MODEL, systemMessage, userMessage, 0.3);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<KimiAiChatResponse> responseEntity = restTemplate.exchange(KIMI_CHAT_URL, HttpMethod.POST, requestEntity, KimiAiChatResponse.class);
            content = responseEntity.getBody().getChoices()[0].getMessage().getContent();

        } catch (Exception ex) {
            log.info("请求异常：{}", ex.getMessage());
            return content;
        }
        return content;
    }


}
