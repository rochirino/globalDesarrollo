package org.example.dto;

public class StatsResponse {
    private long count_mutant_dna;
    private long count_human_dna;
    private double ratio;

    public StatsResponse() {}

    public StatsResponse(long count_mutant_dna, long count_human_dna, double ratio) {
        this.count_mutant_dna = count_mutant_dna;
        this.count_human_dna = count_human_dna;
        this.ratio = ratio;
    }

    public long getCount_mutant_dna() { return count_mutant_dna; }
    public long getCount_human_dna() { return count_human_dna; }
    public double getRatio() { return ratio; }

    public void setCount_mutant_dna(long c) { this.count_mutant_dna = c; }
    public void setCount_human_dna(long c) { this.count_human_dna = c; }
    public void setRatio(double r) { this.ratio = r; }
}