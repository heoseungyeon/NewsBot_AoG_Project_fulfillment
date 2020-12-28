package com.o2o.action.server.DTO;

public class ConfidenceScoreDTO {
    private float positive;
    private float neutral;
    private float negative;

    public float getPositive() {
        return positive;
    }

    public float getNeutral() {
        return neutral;
    }

    public void setNeutral(float neutral) {
        this.neutral = neutral;
    }

    public float getNegative() {
        return negative;
    }

    public void setNegative(float negative) {
        this.negative = negative;
    }

    public void setPositive(float positive) {
        this.positive = positive;
    }
}
