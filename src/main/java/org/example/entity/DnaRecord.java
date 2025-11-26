package org.example.entity;



import jakarta.persistence.*;

@Entity
@Table(name = "dna_record")
public class DnaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dna_hash", unique = true, nullable = false, length = 500)
    private String dnaHash;

    @Column(name = "is_mutant")
    private boolean isMutant;

    public DnaRecord() {}

    public DnaRecord(String dnaHash, boolean isMutant) {
        this.dnaHash = dnaHash;
        this.isMutant = isMutant;
    }

    public Long getId() { return id; }
    public String getDnaHash() { return dnaHash; }
    public boolean isMutant() { return isMutant; }

    public void setId(Long id) { this.id = id; }
    public void setDnaHash(String dnaHash) { this.dnaHash = dnaHash; }
    public void setMutant(boolean mutant) { isMutant = mutant; }
}
