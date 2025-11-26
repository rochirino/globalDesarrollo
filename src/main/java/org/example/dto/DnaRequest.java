package org.example.dto;

import org.example.validation.ValidDna;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public class DnaRequest {
    @ValidDna
       @NotNull(message = "El campo dna no puede ser nulo")
    @NotEmpty(message = "El campo dna no puede estar vacío")
    private String[] dna;

    public DnaRequest() {}

    public DnaRequest(String[] dna) { this.dna = dna; }

    public String[] getDna() { return dna; }
    public void setDna(String[] dna) { this.dna = dna; }
}