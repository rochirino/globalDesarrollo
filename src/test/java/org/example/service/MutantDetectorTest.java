package org.example.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private final MutantDetector detector = new MutantDetector();

    @Test
    void detectsHorizontalMutant() {
        String[] dna = {
                "AAAA",
                "TGCT",
                "CAGT",
                "TTCG"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void detectsVerticalMutant() {
        String[] dna = {
                "ATGC",
                "ATGC",
                "ATGC",
                "ATGC"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void detectsDiagonalMainMutant() {
        String[] dna = {
                "ATGCGA",
                "CAGTAC",
                "TTAAGT",
                "AGAAAG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void detectsDiagonalReverseMutant() {
        String[] dna = {
                "AGGGGA",
                "CAGGTC",
                "TTAAGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void returnsFalseForHuman() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void throwsErrorForInvalidCharacters() {
        String[] dna = {"ATX", "CAG", "TTA"};
        assertThrows(Exception.class, () -> detector.isMutant(dna));
    }

    @Test
    void throwsErrorForNonSquareMatrix() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT"
        };
        assertThrows(Exception.class, () -> detector.isMutant(dna));
    }

    @Test
    void throwsErrorForSmallMatrix() {
        String[] dna = {
                "AAA",
                "AAA",
                "AAA"
        };
        assertThrows(Exception.class, () -> detector.isMutant(dna));
    }
    @Test
    @DisplayName("Mutante con secuencia diagonal")
    public void testMutantDiagonal() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCATA",
                "TCACTG"
        };
        assertTrue(detector.isMutant(dna));
    }
    @Test
    @DisplayName("Humano sin ninguna secuencia de 4 letras")
    public void testHumanNoSequences() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGC",
                "CCCTTA",
                "TCACTG"
        };
        assertFalse(detector.isMutant(dna));
    }
    @Test
    @DisplayName("ADN inválido por ser menor a 4x4")
    public void testInvalidSmallMatrix() {
        String[] dna = {"ATG", "CAG", "TTA"};
        Exception ex = assertThrows(IllegalArgumentException.class, () -> detector.isMutant(dna));
        assertTrue(ex.getMessage().toLowerCase().contains("nxn"));
    }
}
