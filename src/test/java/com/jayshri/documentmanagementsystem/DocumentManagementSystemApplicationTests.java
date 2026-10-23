
package com.jayshri.documentmanagementsystem;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.jayshri.documentmanagementsystem.service.AiSummaryService;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class DocumentManagementSystemApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void getFilesWithoutAuthenticationShouldBeRejected() throws Exception {
        mockMvc.perform(get("/files"))
                .andExpect(status().isForbidden());
    }


    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "jayshri_dev")
    void summarizeMissingPdfShouldReturnNotFound() throws Exception {

        when(aiSummaryService.summarizePdf("missing-file.pdf"))
                .thenThrow(
                        new org.springframework.web.client
                                .HttpClientErrorException(
                                org.springframework.http.HttpStatus.NOT_FOUND
                        )
                );

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/ai/summarize-pdf")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"fileName\":\"missing-file.pdf\"}")
        ).andExpect(status().isNotFound());
    }


    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "jayshri_dev")
    void summarizePdfWithEmptyFileNameShouldReturnBadRequest() throws Exception {
        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/ai/summarize-pdf")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"fileName\":\"\"}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "jayshri_dev")
    void summarizeEmptyTextShouldReturnBadRequest() throws Exception {
        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/ai/summarize")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"\"}")
        ).andExpect(status().isBadRequest());
    }
    @MockBean
    private AiSummaryService aiSummaryService;
    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "jayshri_dev")
    void summarizeTextShouldReturnSummary() throws Exception {

        when(aiSummaryService.summarizeText("Java is a programming language."))
                .thenReturn("Java is a versatile programming language.");

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .post("/ai/summarize")
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                .content("{\"text\":\"Java is a programming language.\"}")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"summary\":\"Java is a versatile programming language.\"}"
                ));
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "jayshri_dev")
    void summarizePdfShouldReturnSummary() throws Exception {

        when(aiSummaryService.summarizePdf("sample.pdf"))
                .thenReturn("This document explains Java programming.");

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .post("/ai/summarize-pdf")
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                .content("{\"fileName\":\"sample.pdf\"}")
                )
                .andExpect(status().isOk())
                .andExpect(
                        org.springframework.test.web.servlet.result.MockMvcResultMatchers
                                .jsonPath("$.fileName").value("sample.pdf")
                )
                .andExpect(
                        org.springframework.test.web.servlet.result.MockMvcResultMatchers
                                .jsonPath("$.summary")
                                .value("This document explains Java programming.")
                );
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "jayshri_dev")
    void summarizeWhitespaceTextShouldReturnBadRequest() throws Exception {

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/ai/summarize")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"   \"}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "jayshri_dev")
    void summarizeWithoutTextFieldShouldReturnBadRequest() throws Exception {

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/ai/summarize")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "jayshri_dev")
    void summarizePdfWhenAiServiceFailsShouldReturnInternalServerError()
            throws Exception {

        when(aiSummaryService.summarizePdf("sample.pdf"))
                .thenThrow(new RuntimeException("AI service unavailable"));

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/ai/summarize-pdf")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"fileName\":\"sample.pdf\"}")
        ).andExpect(status().isInternalServerError());
    }


    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "jayshri_dev")
    void summarizeTextWhenAiServiceFailsShouldReturnInternalServerError() throws Exception {

        when(aiSummaryService.summarizeText("Java programming"))
                .thenThrow(new RuntimeException("AI service unavailable"));

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/ai/summarize")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Java programming\"}")
        ).andExpect(status().isInternalServerError());
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "jayshri_dev")
    void summarizePdfWhenAiServiceReturnsBadRequestShouldReturnBadRequest()
            throws Exception {

        when(aiSummaryService.summarizePdf("invalid.pdf"))
                .thenThrow(
                        new org.springframework.web.client.HttpClientErrorException(
                                org.springframework.http.HttpStatus.BAD_REQUEST
                        )
                );

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/ai/summarize-pdf")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"fileName\":\"invalid.pdf\"}")
        ).andExpect(status().isBadRequest());
    }


}
