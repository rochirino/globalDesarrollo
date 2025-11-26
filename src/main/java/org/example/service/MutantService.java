package org.example.service;

import org.example.service.MutantDetector;
import org.example.dto.StatsResponse;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private String hash(String[] dna) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(String.join("", dna).getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public boolean analyze(String[] dna) {
        String key = hash(dna);
        return repository.findByDnaHash(key)
                .map(DnaRecord::isMutant)
                .orElseGet(() -> {
                    boolean mutant = detector.isMutant(dna);
                    repository.save(new DnaRecord(key, mutant));
                    return mutant;
                });
    }

    public StatsResponse getStats() {
        long m = repository.countByIsMutant(true);
        long h = repository.countByIsMutant(false);
        double ratio = h == 0 ? (m == 0 ? 0.0 : 1.0) : (double) m / h;
        return new StatsResponse(m, h, ratio);
    }
}