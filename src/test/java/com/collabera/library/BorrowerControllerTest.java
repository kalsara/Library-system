package com.collabera.library;

import com.collabera.library.controller.BorrowerController;
import com.collabera.library.payload.BorrowerDto;
import com.collabera.library.service.BorrowerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BorrowerController.class)
class BorrowerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowerService borrowerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerBorrower_Success() throws Exception {
        // Given
        BorrowerDto inputDto = new BorrowerDto();
        inputDto.setName("John Doe");
        inputDto.setEmail("john@example.com");

        BorrowerDto outputDto = new BorrowerDto();
        outputDto.setId(1L);
        outputDto.setName("John Doe");
        outputDto.setEmail("john@example.com");

        when(borrowerService.registerBorrower(any(BorrowerDto.class))).thenReturn(outputDto);

        // When & Then
        mockMvc.perform(post("/api/v1/borrowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void registerBorrower_ValidationError() throws Exception {
        // Given
        BorrowerDto inputDto = new BorrowerDto();
        inputDto.setName("");  // Invalid name
        inputDto.setEmail("invalid-email");  // Invalid email

        // When & Then
        mockMvc.perform(post("/api/v1/borrowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBorrowerById_Success() throws Exception {
        // Given
        BorrowerDto outputDto = new BorrowerDto();
        outputDto.setId(1L);
        outputDto.setName("John Doe");
        outputDto.setEmail("john@example.com");

        when(borrowerService.getBorrowerById(1L)).thenReturn(outputDto);

        // When & Then
        mockMvc.perform(get("/api/v1/borrowers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }
}