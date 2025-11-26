package org.example.service;

import org.springframework.stereotype.Component;
@Component
public class MutantDetector {

    public boolean isMutant(String[] dna) {
        if (dna == null || dna.length == 0) return false;
        int n = dna.length;
        char[][] matrix = new char[n][n];
        for (int i = 0; i < n; i++) {
            String row = dna[i];
            for (int j = 0; j < n; j++) {
                matrix[i][j] = row.charAt(j);
            }
        }

        int sequences = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (checkDirection(matrix, i, j, n, 0, 1)) sequences++;
                if (checkDirection(matrix, i, j, n, 1, 0)) sequences++;
                if (checkDirection(matrix, i, j, n, 1, 1)) sequences++;
                if (checkDirection(matrix, i, j, n, -1, 1)) sequences++;
                if (sequences >= 2) return true;
            }
        }
        return false;
    }

    private boolean checkDirection(char[][] m, int i, int j, int n, int di, int dj) {
        char base = m[i][j];
        int len = 1;
        int ii = i + di, jj = j + dj;
        while (ii >= 0 && ii < n && jj >= 0 && jj < n && m[ii][jj] == base) {
            len++;
            if (len == 4) return true;
            ii += di; jj += dj;
        }
        return false;
    }
}