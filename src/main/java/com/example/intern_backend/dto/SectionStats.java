package com.example.intern_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SectionStats {
    private String section;
    private int totalApplications;
    private int totalOffers;
    private double completionRate;

    public SectionStats() {}

    public SectionStats(String section, int totalApplications, int totalOffers, double completionRate) {
        this.section = section;
        this.totalApplications = totalApplications;
        this.totalOffers = totalOffers;
        this.completionRate = completionRate;
    }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public int getTotalApplications() { return totalApplications; }
    public void setTotalApplications(int totalApplications) { this.totalApplications = totalApplications; }
    public int getTotalOffers() { return totalOffers; }
    public void setTotalOffers(int totalOffers) { this.totalOffers = totalOffers; }
    public double getCompletionRate() { return completionRate; }
    public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
}
