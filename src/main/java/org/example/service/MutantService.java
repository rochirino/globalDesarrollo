package org.example.service;

import org.example.service.MutantDetector;
import org.example.dto.StatsResponse;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Service
public class MutantService {

    private final DnaRecordRepository repository;
    private final MutantDetector detector;

    public MutantService(DnaRecordRepository repository, MutantDetector detector) {
        this.repository = repository;
        this.detector = detector;
    }

    private String sha256(String[] dna) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String joined = String.join("|", dna); // separador para evitar colisiones simples
            byte[] digest = md.digest(joined.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing DNA", e);
        }
    }

    /**
     * Si ya existe el hash en BD, devuelve el resultado almacenado.
     * Si no existe, calcula con MutantDetector, guarda y devuelve.
     */
    @Transactional
    public boolean analyze(String[] dna) {
        String key = sha256(dna);
        return repository.findByDnaHash(key)
                .map(DnaRecord::isMutant)
                .orElseGet(() -> {
                    boolean mutant = detector.isMutant(dna);
                    DnaRecord rec = new DnaRecord(key, mutant);
                    repository.save(rec);
                    return mutant;
                });
    }

    public StatsResponse getStats() {
        long countMutant = repository.countByIsMutant(true);
        long countHuman = repository.countByIsMutant(false);
        double ratio;
        if (countHuman == 0) {
            ratio = countMutant == 0 ? 0.0 : 1.0;
        } else {
            ratio = (double) countMutant / countHuman;
        }
        return new StatsResponse(countMutant, countHuman, ratio);
    }
}