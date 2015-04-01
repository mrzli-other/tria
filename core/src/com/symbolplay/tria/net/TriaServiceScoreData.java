package com.symbolplay.tria.net;

public final class TriaServiceScoreData {
    
    private final String userId;
    private final int score;
    
    public TriaServiceScoreData(String userId, int score) {
        this.userId = userId;
        this.score = score;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public int getScore() {
        return score;
    }
}
