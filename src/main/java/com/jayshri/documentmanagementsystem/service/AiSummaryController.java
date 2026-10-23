
package com.jayshri.documentmanagementsystem.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiSummaryController {

    private final AiSummaryService aiSummaryService;

    public AiSummaryController(AiSummaryService aiSummaryService) {
        this.aiSummaryService = aiSummaryService;
    }

    @PostMapping("/summarize")
    public ResponseEntity<Map<String, String>> summarize(
            @RequestBody Map<String, String> request) {

        String text = request.get("text");

        if (text == null || text.isBlank()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Document text is required")
            );
        }

        String summary = aiSummaryService.summarizeText(text);

        return ResponseEntity.status(HttpStatus.OK).body(
                Map.of("summary", summary)
        );
    }

    @PostMapping("/summarize-pdf")
    public ResponseEntity<Map<String, String>> summarizePdf(
            @RequestBody Map<String, String> request) {

        String fileName = request.get("fileName");

        if (fileName == null || fileName.isBlank()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "PDF file name is required")
            );
        }

        try {
            String summary = aiSummaryService.summarizePdf(fileName);

            return ResponseEntity.ok(
                    Map.of(
                            "fileName", fileName,
                            "summary", summary
                    )
            );


    } catch (org.springframework.web.client.HttpClientErrorException e) {

        if (e.getStatusCode().value() == 404) {
            return ResponseEntity.status(404).body(
                    Map.of("error", "PDF file not found")
            );
        }

        return ResponseEntity.status(e.getStatusCode()).body(
                Map.of("error", "AI service request failed")
        );

    } catch (Exception e) {
        e.printStackTrace();

        return ResponseEntity.status(500).body(
                Map.of("error", "PDF summarization failed")
        );
    }

}
}
