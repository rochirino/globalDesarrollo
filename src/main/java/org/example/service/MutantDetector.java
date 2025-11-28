package org.example.service;

import org.example.validation.DnaValidator;
import org.springframework.stereotype.Component;



@Component
public class MutantDetector {

    // Constante para la cantidad de letras iguales consecutivas
    private static final int SEQUENCE_LENGTH = 4;

    public boolean isMutant(String[] dna) {
        // 1. Validaciones básicas
        if (dna == null || dna.length == 0) throw new IllegalArgumentException("DNA vacío");
        int n = dna.length;
        if (n < SEQUENCE_LENGTH) throw new IllegalArgumentException("Tamaño de matriz insuficiente (Mínimo 4x4)");

        // Convertimos a matriz de caracteres para acceso rápido
        char[][] matrix = new char[n][n];
        for (int i = 0; i < n; i++) {
            if (dna[i].length() != n) throw new IllegalArgumentException("La matriz debe ser cuadrada (NxN)");
            // Validación de caracteres permitidos con Regex rápido
            if (!dna[i].matches("[ATCG]+")) throw new IllegalArgumentException("Caracteres inválidos (Solo A, T, C, G)");
            matrix[i] = dna[i].toCharArray();
        }

        // 2. Conteo de secuencias
        int sequencesFound = 0;

        // Recorremos la matriz UNA sola vez (Optimización)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                // Solo buscamos si hay espacio suficiente en cada dirección para evitar IndexOutOfBounds

                // Horizontal (Hacia la derecha)
                if (j <= n - SEQUENCE_LENGTH) {
                    if (checkDirection(matrix, i, j, 0, 1)) {
                        sequencesFound++;
                        if (sequencesFound > 1) return true; // Early Termination (Optimización del profesor)
                    }
                }

                // Vertical (Hacia abajo)
                if (i <= n - SEQUENCE_LENGTH) {
                    if (checkDirection(matrix, i, j, 1, 0)) {
                        sequencesFound++;
                        if (sequencesFound > 1) return true;
                    }
                }

                // Diagonal (Abajo-Derecha)
                if (i <= n - SEQUENCE_LENGTH && j <= n - SEQUENCE_LENGTH) {
                    if (checkDirection(matrix, i, j, 1, 1)) {
                        sequencesFound++;
                        if (sequencesFound > 1) return true;
                    }
                }

                // Diagonal Inversa (Abajo-Izquierda)
                if (i <= n - SEQUENCE_LENGTH && j >= SEQUENCE_LENGTH - 1) {
                    if (checkDirection(matrix, i, j, 1, -1)) {
                        sequencesFound++;
                        if (sequencesFound > 1) return true;
                    }
                }
            }
        }

        return false; // Si terminamos y encontramos 0 o 1 secuencia, es Humano.
    }

    /**
     * Verifica si hay 4 letras iguales consecutivas desde (row, col) en la dirección (deltaRow, deltaCol)
     */
    private boolean checkDirection(char[][] matrix, int row, int col, int deltaRow, int deltaCol) {
        char first = matrix[row][col];
        for (int k = 1; k < SEQUENCE_LENGTH; k++) {
            if (matrix[row + k * deltaRow][col + k * deltaCol] != first) {
                return false;
            }
        }
        return true;
    }
}
