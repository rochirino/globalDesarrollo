package org.example.controller;


import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MutantController {

    private final MutantService mutantService;

    public MutantController(MutantService mutantService) {
        this.mutantService = mutantService;
    }

    @Operation(
            summary = "Detecta si una secuencia de ADN pertenece a un mutante",
            description = "Devuelve 200 si es mutante y 403 si es humano. Devuelve 400 ante ADN inválido."
    )
    @ApiResponse(responseCode = "200", description = "Secuencia mutante detectada")
    @ApiResponse(responseCode = "403", description = "Secuencia humana detectada")
    @ApiResponse(responseCode = "400", description = "ADN inválido",
            content = @Content(schema = @Schema(implementation = DnaRequest.class)))
    @PostMapping({"/mutant", "/mutant/"})
    public ResponseEntity<Void> detectMutant(@Valid @RequestBody DnaRequest dnaRequest) {
        boolean isMutant = mutantService.analyze(dnaRequest.getDna());
        return isMutant ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(
            summary = "Obtiene estadísticas de análisis de ADN",
            description = "Devuelve la cantidad de mutantes, humanos y el ratio entre ambos."
    )
    @ApiResponse(responseCode = "200", description = "Estadísticas generadas",
            content = @Content(schema = @Schema(implementation = StatsResponse.class)))
    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(mutantService.getStats());
    }

    @Operation(summary = "Verifica el estado del servidor")
    @ApiResponse(responseCode = "200", description = "El servicio está activo")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
