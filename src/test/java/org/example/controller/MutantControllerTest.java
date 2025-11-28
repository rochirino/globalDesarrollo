package org.example.controller;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DnaRecordRepository repo;

    @AfterEach
    void limpiar() {
        repo.deleteAll();
    }

    /** Helper para armar JSON */
    private static String dnaJson(String arr) {
        return "{ \"dna\": " + arr + " }";
    }

    // ADN válidos
    private final String MUTANT = dnaJson("""
            ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
        """);

    private final String HUMAN = dnaJson("""
            ["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"]
        """);

    // ADN inválidos
    private final String INVALID_NXN = dnaJson("""
            ["ATG","CAGT","TTAT"]
        """);

    private final String INVALID_CHAR = dnaJson("""
            ["ATXG","CAGT","TTAT","AGGA"]
        """);

    // ===============================
    // TESTS /mutant
    // ===============================

    @Test
    @DisplayName("Devuelve 200 para mutante y guarda en BD")
    void mutant_ok() throws Exception {
        mockMvc.perform(post("/mutant")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MUTANT))
                .andExpect(status().isOk());

        assertEquals(1, repo.count());
        assertTrue(repo.findAll().get(0).isMutant());
    }

    @Test
    @DisplayName("Devuelve 403 para humano y guarda en BD")
    void human_forbidden() throws Exception {
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(HUMAN))
                .andExpect(status().isForbidden());

        assertEquals(1, repo.count());
        assertFalse(repo.findAll().get(0).isMutant());
    }

    @Test
    @DisplayName("Devuelve 400 si ADN no es NxN")
    void invalid_nxn() throws Exception {
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(INVALID_NXN))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("NxN")));
    }

    @Test
    @DisplayName("Devuelve 400 si contiene caracteres inválidos")
    void invalid_char() throws Exception {
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(INVALID_CHAR))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("A,T,C,G")));
    }

    @Test
    @DisplayName("No guarda dos veces el mismo ADN (usa cache)")
    void cache_works() throws Exception {

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MUTANT))
                .andExpect(status().isOk());

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MUTANT))
                .andExpect(status().isOk());

        assertEquals(1, repo.count(), "Debe haber solo 1 registro en la BD");
    }

    // ===============================
    // TESTS /stats
    // ===============================

    @Test
    @DisplayName("GET /stats devuelve estadísticas correctas")
    void stats_ok() throws Exception {

        repo.save(new DnaRecord("h1", true));
        repo.save(new DnaRecord("h2", true));
        repo.save(new DnaRecord("h3", false));

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna", is(2)))
                .andExpect(jsonPath("$.count_human_dna", is(1)))
                .andExpect(jsonPath("$.ratio", is(2.0)));
    }

    // ===============================
    // TEST health
    // ===============================

    @Test
    @DisplayName("GET /health funciona")
    void health_ok() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void debug_detector_directo() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        var d = new org.example.service.MutantDetector();
        System.out.println("¿Detector dice mutante?: " + d.isMutant(dna));
    }
}
