package com.BrainBlitz.ai;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.HashMap;

@Service
public class OllamaService {

    @Value("${ollama.url}")
    private String ollamaUrl;

    public String generateFeedback(String prompt) {
        try {
            WebClient client = WebClient.create(ollamaUrl);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama3.1");
            requestBody.put("prompt", prompt);
            requestBody.put("stream", false);

            String response = client.post()
            	    .uri("/api/generate")
            	    .header("ngrok-skip-browser-warning", "true")
            	    .header("Content-Type", "application/json")
            	    .bodyValue(requestBody)
            	    .retrieve()
            	    .bodyToMono(String.class)
            	    .block();

            // Extract response text
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(response, Map.class);
            return (String) responseMap.get("response");

        } catch (Exception e) {
            return "Error generating feedback: " + e.getMessage();
        }
    }
}
