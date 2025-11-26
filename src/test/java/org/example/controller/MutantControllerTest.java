package org.example.controller;


import org.example.MutantApplication;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for /mutant and /stats endpoints
 */
@SpringBootTest(classes = MutantApplication.class)
@AutoConfigureMockMvc
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DnaRecordRepository dnaRecordRepository;

    @AfterEach
    void clean() {
        dnaRecordRepository.deleteAll();
    }

    private String wrap(String array) {
        return "{ \"dna\": " + array + " }";
    }

    @Test
    void mutantReturns200() throws Exception {
        String dna = wrap("[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]");
        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(dna))
                .andExpect(status().isOk());
        assertEquals(1, dnaRecordRepository.count());
    }

    @Test
    void humanReturns403() throws Exception {
        String dna = wrap("[\"ATGCGA\",\"CAGTGC\",\"TTATTT\",\"AGACGG\",\"GCGTCA\",\"TCACTG\"]");
        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(dna))
                .andExpect(status().isForbidden());
        assertEquals(1, dnaRecordRepository.count());
    }

    @Test
    void invalidCharReturns400() throws Exception {
        String dna = wrap("[\"ATXGGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]");
        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(dna))
                .andExpect(status().isBadRequest());
        assertEquals(0, dnaRecordRepository.count());
    }

    @Test
    void statsReturnsCorrect() throws Exception {
        dnaRecordRepository.save(new DnaRecord("h1", true));
        dnaRecordRepository.save(new DnaRecord("h2", true));
        dnaRecordRepository.save(new DnaRecord("h3", false));

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna", is(2)))
                .andExpect(jsonPath("$.count_human_dna", is(1)))
                .andExpect(jsonPath("$.ratio", is(2.0)));
    }
}
