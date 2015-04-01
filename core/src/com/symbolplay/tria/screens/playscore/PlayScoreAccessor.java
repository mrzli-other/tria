package com.symbolplay.tria.screens.playscore;

import java.util.Comparator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.net.FacebookAccessor;
import com.symbolplay.tria.net.FacebookDataListener;
import com.symbolplay.tria.net.FacebookFriendData;
import com.symbolplay.tria.net.FacebookProfileData;
import com.symbolplay.tria.net.TriaServiceAccessor;
import com.symbolplay.tria.net.TriaServiceScoreData;
import com.symbolplay.tria.net.TriaServiceScoreListener;
import com.symbolplay.tria.persistence.GameData;
import com.symbolplay.tria.persistence.userdata.HighScoreData;
import com.symbolplay.tria.screens.general.ScoreLines;

public final class PlayScoreAccessor {
    
    private static final Comparator<PlayScoreData> PLAY_SCORE_DATA_COMPARATOR;
    
    private final GameData gameData;
    private final FacebookAccessor facebookAccessor;
    private final TriaServiceAccessor triaServiceAccessor;
    
    private FacebookProfileData facebookProfileData;
    private Array<FacebookFriendData> facebookFriendsData;
    private ObjectMap<String, TriaServiceScoreData> triaServiceScoreDataMap;
    
    private PlayScoreListener playScoreListener;
    
    static {
        PLAY_SCORE_DATA_COMPARATOR = new Comparator<PlayScoreData>() {
            @Override
            public int compare(PlayScoreData sd1, PlayScoreData sd2) {
                return GameUtils.compareInteger(sd1.getScore(), sd2.getScore());
            }
        };
    }
    
    public PlayScoreAccessor(GameData gameData, FacebookAccessor facebookAccessor, TriaServiceAccessor triaServiceAccessor) {
        this.gameData = gameData;
        
        this.facebookAccessor = facebookAccessor;
        facebookAccessor.addDataListener(new FacebookDataListener() {
            
            @Override
            public void dataReceived(FacebookProfileData profileData, Array<FacebookFriendData> friendsData) {
                PlayScoreAccessor.this.facebookProfileData = profileData;
                PlayScoreAccessor.this.facebookFriendsData = friendsData;
                
                if (profileData != null && friendsData != null) {
                    Array<String> userIds = new Array<String>(true, friendsData.size);
                    userIds.add(profileData.getId());
                    for (FacebookFriendData friendData : friendsData) {
                        userIds.add(friendData.getId());
                    }
                    PlayScoreAccessor.this.triaServiceAccessor.requestFacebookScores(userIds);
                }
            }
            
            @Override
            public void dataError(String message) {
                // do nothing
            }
        });
        
        this.triaServiceAccessor = triaServiceAccessor;
        triaServiceAccessor.addScoreListener(new TriaServiceScoreListener() {
            
            @Override
            public void scoresReceived(ObjectMap<String, TriaServiceScoreData> scoreDataMap) {
                PlayScoreAccessor.this.triaServiceScoreDataMap = scoreDataMap;
                
                Array<PlayScoreData> playScoresData = getPlayScoresDataFromFacebookData();
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
        } else if (ScoreLines.FACEBOOK.equals(selectedScoreLines)) {
            requestFacebookScore();
        } else {
            notifyScoreReceived(null);
        }
    }
    
    private void requestLocalScore() {
        Array<HighScoreData> highScoresData = gameData.getAchievementsData().getHighScores();
        Array<PlayScoreData> playScoresData = getPlayScoresDataFromLocalData(highScoresData);
        notifyScoreReceived(playScoresData);
    }
    
    private void requestFacebookScore() {
        facebookAccessor.requestProfileAndFriends();
    }
    
    private Array<PlayScoreData> getPlayScoresDataFromLocalData(Array<HighScoreData> highScoresData) {
        Array<PlayScoreData> playScoresData = new Array<PlayScoreData>(true, highScoresData.size);
        for (HighScoreData highScoreData : highScoresData) {
            int score = highScoreData.getScore();
            if (score > 0) {
                PlayScoreData playScoreData = new PlayScoreData(highScoreData.getName(), score, false);
                playScoresData.add(playScoreData);
            }
        }
        
        playScoresData.sort(PLAY_SCORE_DATA_COMPARATOR);
        
        return playScoresData;
    }
    
    private Array<PlayScoreData> getPlayScoresDataFromFacebookData() {
        if (!isAllFacebookScoreDataPresent()) {
            return null;
        }
        
        Array<PlayScoreData> playScoresData = new Array<PlayScoreData>(true, facebookFriendsData.size + 1);
        
        String currentUserId = facebookProfileData.getId();
        int currentUserScore = triaServiceScoreDataMap.containsKey(currentUserId) ? triaServiceScoreDataMap.get(currentUserId).getScore() : 0;
        if (currentUserScore > 0) {
            playScoresData.add(new PlayScoreData(facebookProfileData.getName(), currentUserScore, true));
        }
        
        for (FacebookFriendData facebookFriendData : facebookFriendsData) {
            String friendId = facebookFriendData.getId();
            int friendScore = triaServiceScoreDataMap.containsKey(friendId) ? triaServiceScoreDataMap.get(friendId).getScore() : 0;
            if (friendScore > 0) {
                PlayScoreData playScoreData = new PlayScoreData(facebookFriendData.getName(), friendScore, false);
                playScoresData.add(playScoreData);
            }
        }
        
        playScoresData.sort(PLAY_SCORE_DATA_COMPARATOR);
        
        return playScoresData;
    }
    
    private boolean isAllFacebookScoreDataPresent() {
        return facebookProfileData != null && facebookFriendsData != null && triaServiceScoreDataMap != null;
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
