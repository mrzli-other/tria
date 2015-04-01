package com.symbolplay.tria.persistence.userdata;

public final class HighScoreData {
    
    private String name;
    private int score;
    private long time;
    
    public HighScoreData() {
    }
    
    public HighScoreData(String name, int score, long time) {
        this.name = name;
        this.score = score;
        this.time = time;
    }
    
    public String getName() {
        return name;
    }
    
    public int getScore() {
        return score;
    }
    
    public long getTime() {
        return time;
    }
}
