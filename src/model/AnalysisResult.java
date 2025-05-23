package model;

public record AnalysisResult(
    double overallScore,
    double pitchAccuracy,
    double timingAccuracy
) {}
