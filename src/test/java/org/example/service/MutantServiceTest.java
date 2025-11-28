package org.example.service;

import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MutantServiceTest {

    private final DnaRecordRepository repo = mock(DnaRecordRepository.class);
    private final MutantDetector detector = mock(MutantDetector.class);

    private final MutantService service = new MutantService(repo, detector);

    @Test
    void usesDatabaseCacheWhenExists() {
        String[] dna = {"AAAA", "CCCC", "GGGG", "TTTT"};
        DnaRecord record = new DnaRecord("hash", true);

        when(repo.findByDnaHash(anyString())).thenReturn(Optional.of(record));

        boolean result = service.analyze(dna);

        assertTrue(result);
        verify(detector, never()).isMutant(any());
    }

    @Test
    void savesResultWhenNotInDatabase() {
        String[] dna = {"AAAA", "CCCC", "GGGG", "TTTT"};

        when(repo.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(detector.isMutant(dna)).thenReturn(true);

        boolean result = service.analyze(dna);

        assertTrue(result);
        verify(repo).save(any(DnaRecord.class));
    }

    @Test
    void computesStatsCorrectly() {
        when(repo.countByIsMutant(true)).thenReturn(2L);
        when(repo.countByIsMutant(false)).thenReturn(1L);

        var stats = service.getStats();

        assertEquals(2, stats.getCount_mutant_dna());
        assertEquals(1, stats.getCount_human_dna());
        assertEquals(2.0, stats.getRatio());
    }

    @Test
    void ratioIsOneWhenNoHumans() {
        when(repo.countByIsMutant(true)).thenReturn(3L);
        when(repo.countByIsMutant(false)).thenReturn(0L);

        var stats = service.getStats();

        assertEquals(1.0, stats.getRatio());
    }

    @Test
    void ratioIsZeroWhenOnlyHumans() {
        when(repo.countByIsMutant(true)).thenReturn(0L);
        when(repo.countByIsMutant(false)).thenReturn(5L);

        var stats = service.getStats();

        assertEquals(0.0, stats.getRatio());
    }
}
