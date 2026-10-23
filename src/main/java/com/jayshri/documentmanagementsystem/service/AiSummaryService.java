
package com.jayshri.documentmanagementsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Service
public class AiSummaryService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String AI_SERVICE_URL =
            "http://127.0.0.1:5000/summarize";

    public String summarizeText(String text) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = Map.of("text", text);

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                AI_SERVICE_URL,
                request,
                Map.class
        );

        Map responseBody = response.getBody();

        if (responseBody == null || responseBody.get("summary") == null) {
            throw new IllegalStateException(
                    "AI service returned an empty summary"
            );
        }

        return responseBody.get("summary").toString();
    }
    public String summarizePdf(String fileName) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = Map.of(
                "fileName", fileName
        );

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://127.0.0.1:5000/summarize-pdf",
                request,
                Map.class
        );

        Map responseBody = response.getBody();

        if (responseBody == null || responseBody.get("summary") == null) {
            throw new IllegalStateException(
                    "AI service returned an empty PDF summary"
            );
        }

        return responseBody.get("summary").toString();
    }
}
