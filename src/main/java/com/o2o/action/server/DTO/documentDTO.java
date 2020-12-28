package com.o2o.action.server.DTO;

public class documentDTO {
    private String id;
    private String sentiment;

    public ConfidenceScoreDTO getConfidenceScores() {
        return confidenceScores;
    }

    public void setConfidenceScores(ConfidenceScoreDTO confidenceScores) {
        this.confidenceScores = confidenceScores;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    private ConfidenceScoreDTO confidenceScores;

}
