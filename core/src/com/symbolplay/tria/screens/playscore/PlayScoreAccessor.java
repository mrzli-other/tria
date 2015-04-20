package com.symbolplay.tria.screens.playscore;

import java.util.Comparator;

import com.badlogic.gdx.utils.Array;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.net.TriaServiceAccessor;
import com.symbolplay.tria.net.TriaServiceScoreListener;
import com.symbolplay.tria.persistence.GameData;
import com.symbolplay.tria.persistence.userdata.HighScoreData;
import com.symbolplay.tria.screens.general.ScoreLines;

public final class PlayScoreAccessor {
    
    private static final Comparator<HighScoreData> HIGH_SCORE_COMPARATOR;
    
    private final GameData gameData;
    private final TriaServiceAccessor triaServiceAccessor;
    
    private PlayScoreListener playScoreListener;
    
    static {
        HIGH_SCORE_COMPARATOR = new Comparator<HighScoreData>() {
            
            @Override
            public int compare(HighScoreData s1, HighScoreData s2) {
                return -GameUtils.compareInteger(s1.getScore(), s2.getScore());
            }
        };
    }
    
    public PlayScoreAccessor(GameData gameData, TriaServiceAccessor triaServiceAccessor) {
        this.gameData = gameData;
        
        this.triaServiceAccessor = triaServiceAccessor;
        triaServiceAccessor.addScoreListener(new TriaServiceScoreListener() {
            
            @Override
            public void scoresReceived(Array<HighScoreData> scoresData) {
                Array<PlayScoreData> playScoresData = getPlayScoresDataFromHighScoresData(scoresData);
                notifyScoreReceived(playScoresData);
            }
            
            @Override
            public void error(String message) {
            }
        });
        
        playScoreListener = null;
    }
    
    public void requestScores() {
        String selectedScoreLines = gameData.getGamePreferences().getSelectedScoreLines();
        
        if (ScoreLines.LOCAL.equals(selectedScoreLines)) {
            requestLocalScore();
        } else if (ScoreLines.GLOBAL.equals(selectedScoreLines)) {
            requestGlobalScore();
        } else {
            notifyScoreReceived(null);
        }
    }
    
    private void requestLocalScore() {
        Array<HighScoreData> highScoresData = gameData.getAchievementsData().getHighScores();
        Array<PlayScoreData> playScoresData = getPlayScoresDataFromHighScoresData(highScoresData);
        notifyScoreReceived(playScoresData);
    }
    
    private void requestGlobalScore() {
        triaServiceAccessor.requestGlobalScores();
    }
    
    private Array<PlayScoreData> getPlayScoresDataFromHighScoresData(Array<HighScoreData> highScoresData) {
        
        Array<HighScoreData> highScoresDataSorted = new Array<HighScoreData>(highScoresData);
        highScoresDataSorted.sort(HIGH_SCORE_COMPARATOR);
        
        Array<PlayScoreData> playScoresData = new Array<PlayScoreData>(true, highScoresData.size);
        
        int rank = 1;
        for (HighScoreData highScoreData : highScoresData) {
            int score = highScoreData.getScore();
            if (score > 0) {
                PlayScoreData playScoreData = new PlayScoreData(rank, highScoreData.getName(), score);
                playScoresData.add(playScoreData);
            }
            rank++;
        }
        
        return playScoresData;
    }
    
    public void setPlayScoreListener(PlayScoreListener playScoreListener) {
        this.playScoreListener = playScoreListener;
    }
    
    private void notifyScoreReceived(Array<PlayScoreData> playScores) {
        if (playScoreListener != null) {
            playScoreListener.scoreReceived(playScores);
        }
    }
}
