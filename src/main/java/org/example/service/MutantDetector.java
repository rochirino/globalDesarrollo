package org.example.service;

import org.example.validation.DnaValidator;
import org.springframework.stereotype.Component;
@Component




public class MutantDetector {

    private static final int SEQ = 4;

    public boolean isMutant(String[] dna) {

        int n = dna.length;
        char[][] m = new char[n][n];

        for (int i = 0; i < n; i++) {
            m[i] = dna[i].toCharArray();
        }

        int found = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                char base = m[i][j];

                // Horizontal →
                if (j <= n - SEQ && check(m, i, j, 0, 1, base)) {
                    if (++found == 2) return true;
                }

                // Vertical ↓
                if (i <= n - SEQ && check(m, i, j, 1, 0, base)) {
                    if (++found == 2) return true;
                }

                // Diagonal ↘
                if (i <= n - SEQ && j <= n - SEQ && check(m, i, j, 1, 1, base)) {
                    if (++found == 2) return true;
                }

                // Diagonal ↗
                if (i >= SEQ - 1 && j <= n - SEQ && check(m, i, j, -1, 1, base)) {
                    if (++found == 2) return true;
                }
            }
        }

        return false;
    }

    private boolean check(char[][] m, int r, int c, int dr, int dc, char base) {

        for (int k = 1; k < SEQ; k++) {
            if (m[r + k * dr][c + k * dc] != base) {
                return false;
            }
        }

        return true;
    }
}
