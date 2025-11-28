package org.example.entity;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter // Lombok
@NoArgsConstructor // Lombok
@AllArgsConstructor // Lombok
@Builder // Lombok (Patron Builder, opcional pero recomendado)
@Table(name = "dna_record")
public class DnaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dna_hash", unique = true, nullable = false, length = 500)
    private String dnaHash;

    @Column(name = "is_mutant")
    private boolean isMutant;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();



    public DnaRecord(String dnaHash, boolean isMutant) {
        this.dnaHash = dnaHash;
        this.isMutant = isMutant;
        this.createdAt = LocalDateTime.now();
    }


    public boolean isMutant() { return isMutant; }


}
