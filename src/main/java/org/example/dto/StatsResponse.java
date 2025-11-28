package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Estadísticas del análisis de ADN")
public class StatsResponse {

    @Schema(description = "Cantidad de ADN mutante detectado", example = "40")
    private long count_mutant_dna;

    @Schema(description = "Cantidad de ADN humano detectado", example = "100")
    private long count_human_dna;

    @Schema(description = "Proporción entre mutantes y humanos", example = "0.4")
    private double ratio;

    public StatsResponse() {}

    public StatsResponse(long count_mutant_dna, long count_human_dna, double ratio) {
        this.count_mutant_dna = count_mutant_dna;
        this.count_human_dna = count_human_dna;
        this.ratio = ratio;
    }
}
