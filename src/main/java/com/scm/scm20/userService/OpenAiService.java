package com.scm.scm20.userService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final ObjectMapper mapper = new ObjectMapper();

    public String getAIResponse(String message) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        String prompt = """
        Extract intent and name from this message.

        Return ONLY JSON:
        {
          "intent": "get_phone | get_email | get_all",
          "name": "person name"
        }

        Message: %s
        """.formatted(message);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(requestBody), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                entity,
                String.class
        );

        return response.getBody();
    }
}
