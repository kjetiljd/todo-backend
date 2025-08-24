package com.workshop.todoapi.client;

import com.workshop.todoapi.model.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class SorterClient {
    
    private static final Logger logger = LoggerFactory.getLogger(SorterClient.class);
    
    private final WebClient webClient;
    
    public SorterClient(@Value("${app.sorter-service.url:http://localhost:3001}") String sorterServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(sorterServiceUrl)
                .build();
    }
    
    public List<Todo> sortTodos(List<Todo> todos, String sortBy) {
        try {
            logger.debug("Requesting sort from sorter service: sortBy={}, count={}", sortBy, todos.size());
            
            Map<String, Object> request = Map.of(
                "todos", todos,
                "sortBy", sortBy
            );
            
            List<Todo> sortedTodos = webClient
                .post()
                .uri("/sort")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Todo>>() {})
                .timeout(Duration.ofSeconds(5))
                .block();
            
            logger.debug("Successfully received sorted todos from sorter service");
            return sortedTodos != null ? sortedTodos : todos;
            
        } catch (WebClientException e) {
            logger.warn("Failed to call sorter service, using fallback sorting: {}", e.getMessage());
            return todos; // Return original list as fallback
        } catch (Exception e) {
            logger.error("Unexpected error calling sorter service", e);
            return todos; // Return original list as fallback
        }
    }
    
    public boolean isHealthy() {
        try {
            String response = webClient
                .get()
                .uri("/health")
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(3))
                .block();
            
            return response != null && response.contains("OK");
        } catch (Exception e) {
            logger.debug("Sorter service health check failed: {}", e.getMessage());
            return false;
        }
    }
}