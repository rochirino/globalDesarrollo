package org.example.service;

import org.example.validation.DnaValidator;
import org.springframework.stereotype.Component;


@Component
public class MutantDetector {

    public boolean isMutant(String[] dna) {

        if (dna == null || dna.length == 0)
            throw new IllegalArgumentException("DNA vacío");

        int n = dna.length;

        // Debe ser NxN y mínimo 4x4
        if (n < 4)
            throw new IllegalArgumentException("La matriz debe ser NxN y de tamaño mínimo 4");

        for (String row : dna) {
            if (row.length() != n)
                throw new IllegalArgumentException("La matriz debe ser NxN");

            if (!row.matches("[ATCG]+"))
                throw new IllegalArgumentException("Caracter inválido");
        }

        char[][] m = toMatrix(dna);

        // Si encuentra 1 secuencia → mutante (como piden tus tests)
        return hasHorizontal(m, n)
                || hasVertical(m, n)
                || hasDiagonal(m, n)
                || hasReverseDiagonal(m, n);
    }

    // -----------------------------
    //   METODOS DE DETECCION
    // -----------------------------

    private boolean hasHorizontal(char[][] m, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n - 4; j++) {
                char c = m[i][j];
                if (c == m[i][j+1] && c == m[i][j+2] && c == m[i][j+3])
                    return true;
            }
        }
        return false;
    }

    private boolean hasVertical(char[][] m, int n) {
        for (int i = 0; i <= n - 4; i++) {
            for (int j = 0; j < n; j++) {
                char c = m[i][j];
                if (c == m[i+1][j] && c == m[i+2][j] && c == m[i+3][j])
                    return true;
            }
        }
        return false;
    }

    private boolean hasDiagonal(char[][] m, int n) {
        for (int i = 0; i <= n - 4; i++) {
            for (int j = 0; j <= n - 4; j++) {
                char c = m[i][j];
                if (c == m[i+1][j+1] && c == m[i+2][j+2] && c == m[i+3][j+3])
                    return true;
            }
        }
        return false;
    }

    private boolean hasReverseDiagonal(char[][] m, int n) {
        for (int i = 0; i <= n - 4; i++) {
            for (int j = 3; j < n; j++) {
                char c = m[i][j];
                if (c == m[i+1][j-1] && c == m[i+2][j-2] && c == m[i+3][j-3])
                    return true;
            }
        }
        return false;
    }

    private char[][] toMatrix(String[] dna) {
        int n = dna.length;
        char[][] m = new char[n][n];
        for (int i = 0; i < n; i++)
            m[i] = dna[i].toCharArray();
        return m;
    }
}
