package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.validation.ValidDna;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request que contiene la matriz de ADN para analizar")
public class DnaRequest {

    @Schema(
            description = "Secuencia de ADN NxN solo con caracteres A,T,C,G",
            example = "[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]"
    )
    @ValidDna
    @NotNull(message = "El campo dna no puede ser nulo")
    @NotEmpty(message = "El campo dna no puede estar vacío")
    private String[] dna;

}
