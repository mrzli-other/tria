package com.symbolplay.tria.screens.general;

class FinalFriendScoreData {
    
    private final String userId;
    private final String name;
    private final int score;
    private final boolean isCurrentUser;
    
    public FinalFriendScoreData(String userId, String name, int score, boolean isCurrentUser) {
        this.userId = userId;
        this.name = name;
        this.score = score;
        this.isCurrentUser = isCurrentUser;
    }
    
    public String getUserId() {
        return userId;
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