package com.symbolplay.tria.screens.playscore;

public final class PlayScoreData {
    
    private final String name;
    private final int score;
    private final boolean isCurrentUser;
    
    public PlayScoreData(String name, int score, boolean isCurrentUser) {
        this.name = name;
        this.score = score;
        this.isCurrentUser = isCurrentUser;
    }
    
    public String getName() {
        return name;
    }
    
    public int getScore() {
        return score;
    }
    
    public boolean isCurrentUser() {
        return isCurrentUser;
    }
}
