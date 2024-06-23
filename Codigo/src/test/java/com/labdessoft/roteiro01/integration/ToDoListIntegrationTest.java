package com.labdessoft.roteiro01.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ToDoListIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateTask() {
        String url = "/api/tasks";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String jsonBody = "{\"title\": \"Learn Spring Boot\", \"description\": \"Understand the basics of Spring Boot\", \"type\": \"DATA\"}";
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).contains("Learn Spring Boot");
    }

    @Test
    public void testGetAllTasks() {
        testCreateTask(); // Ensure there is at least one task
        String url = "/api/tasks";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("Learn Spring Boot");
    }

    @Test
    public void testUpdateTask() {
        testCreateTask(); // Ensure there is a task to update
        String updateUrl = "/api/tasks/1";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String updateJsonBody = "{\"title\": \"Updated Task\", \"description\": \"Updated Description\", \"type\": \"DATA\"}";
        HttpEntity<String> updateEntity = new HttpEntity<>(updateJsonBody, headers);

        ResponseEntity<String> updateResponse = restTemplate.exchange(updateUrl, HttpMethod.PUT, updateEntity, String.class);

        assertThat(updateResponse.getStatusCodeValue()).isEqualTo(200);
        assertThat(updateResponse.getBody()).contains("Updated Task");
    }

    @Test
    public void testDeleteTask() {
        testCreateTask(); // Ensure there is a task to delete
        String deleteUrl = "/api/tasks/1";
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, Void.class);

        assertThat(deleteResponse.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    public void testMarkTaskAsCompleted() {
        testCreateTask(); // Ensure there is a task to mark as completed
        String completeUrl = "/api/tasks/1/complete";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> completeEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> completeResponse = restTemplate.exchange(completeUrl, HttpMethod.PATCH, completeEntity, String.class);

        assertThat(completeResponse.getStatusCodeValue()).isEqualTo(200);
        assertThat(completeResponse.getBody()).contains("\"completed\":true");
    }

    @Test
    public void testViewTask() {
        testCreateTask(); // Ensure there is a task to view
        String viewUrl = "/api/tasks/1";
        ResponseEntity<String> viewResponse = restTemplate.getForEntity(viewUrl, String.class);

        assertThat(viewResponse.getStatusCodeValue()).isEqualTo(200);
        assertThat(viewResponse.getBody()).contains("Learn Spring Boot");
    }
}
