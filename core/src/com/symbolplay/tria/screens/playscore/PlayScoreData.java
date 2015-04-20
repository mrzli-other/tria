package com.symbolplay.tria.screens.playscore;

public final class PlayScoreData {
    
    private final int rank;
    private final String name;
    private final int score;
    
    public PlayScoreData(int rank, String name, int score) {
        this.rank = rank;
        this.name = name;
        this.score = score;
    }
    
    public int getRank() {
        return rank;
    }
    
    public String getName() {
        return name;
    }
    
    public int getScore() {
        return score;
    }
}
