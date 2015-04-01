package com.symbolplay.tria.screens.general;

import java.util.Comparator;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.net.FacebookAccessor;
import com.symbolplay.tria.net.FacebookFriendData;
import com.symbolplay.tria.net.FacebookProfileData;
import com.symbolplay.tria.net.FacebookDataListener;
import com.symbolplay.tria.net.TriaServiceAccessor;
import com.symbolplay.tria.net.TriaServiceScoreData;
import com.symbolplay.tria.net.TriaServiceScoreListener;

public class FacebookScoresControl extends Table {
    
    private static final Comparator<FinalFriendScoreData> FRIEND_SCORE_DATA_COMPARATOR;
    
    private final AssetManager assetManager;
    private final Skin guiSkin;
    
    private final FacebookAccessor facebookAccessor;
    private final TriaServiceAccessor triaServiceAccessor;
    
    private boolean isActive;
    
    private FacebookProfileData profileData;
    private Array<FacebookFriendData> friendsData;
    private ObjectMap<String, TriaServiceScoreData> scoreDataMap;
    
    static {
        FRIEND_SCORE_DATA_COMPARATOR = new Comparator<FinalFriendScoreData>() {
            @Override
            public int compare(FinalFriendScoreData sd1, FinalFriendScoreData sd2) {
                if (sd1.getScore() > sd2.getScore()) {
                    return -1;
                } else if (sd1.getScore() < sd2.getScore()) {
                    return 1;
                } else {
                    return sd1.getName().compareToIgnoreCase(sd2.getName());
                }
            }
        };
    }
    
    public FacebookScoresControl(AssetManager assetManager, Skin guiSkin, FacebookAccessor facebookAccessor, TriaServiceAccessor triaServiceAccessor) {
        this.assetManager = assetManager;
        this.guiSkin = guiSkin;
        
        this.facebookAccessor = facebookAccessor;
        facebookAccessor.addDataListener(new FacebookDataListener() {
            
            @Override
            public void dataReceived(FacebookProfileData profileData, Array<FacebookFriendData> friendsData) {
                FacebookScoresControl.this.profileData = profileData;
                FacebookScoresControl.this.friendsData = friendsData;
                
                if (profileData != null && friendsData != null) {
                    Array<String> userIds = new Array<String>(true, friendsData.size);
                    userIds.add(profileData.getId());
                    for (FacebookFriendData friendData : friendsData) {
                        userIds.add(friendData.getId());
                    }
                    FacebookScoresControl.this.triaServiceAccessor.requestFacebookScores(userIds);
                }
                
                refreshContent();
            }
            
            @Override
            public void dataError(String message) {
                setTextContent(message);
            }
        });
        
        this.triaServiceAccessor = triaServiceAccessor;
        triaServiceAccessor.addScoreListener(new TriaServiceScoreListener() {
            
            @Override
            public void scoresReceived(ObjectMap<String, TriaServiceScoreData> scoreDataMap) {
                FacebookScoresControl.this.scoreDataMap = scoreDataMap;
                refreshContent();
            }
            
            @Override
            public void error(String message) {
                setTextContent(message);
            }
        });
        
        setActive(false);
        
        clearData();
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);
        
        if (!isActive) {
            return;
        }
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
        
        clearData();
        setWaitContent();
        if (isActive) {
            facebookAccessor.requestProfileAndFriends();
        }
    }
    
    private void clearData() {
        profileData = null;
        friendsData = null;
        scoreDataMap = null;
    }
    
    private void setWaitContent() {
        clear();
        center();
        add(GameScreenUtils.getWaitAnimatedImage(assetManager));
    }
    
    private void setTextContent(String text) {
        clear();
        center();
        
        LabelStyle labelStyle = guiSkin.get("font24", LabelStyle.class);
        
        float labelPadding = 10.0f;
        float labelWidth = GameContainer.VIEWPORT_WIDTH - 2.0f * labelPadding;
        
        Label label = new Label(text, labelStyle);
        label.setWrap(true);
        label.setAlignment(Align.center);
        add(label).width(labelWidth).pad(labelPadding);
    }
    
    private void refreshContent() {
        
        Array<FinalFriendScoreData> finalFriendScoresData = getFinalScoresData();
        if (finalFriendScoresData == null) {
            return;
        }
        
        clear();
        left().top();
        
        if (finalFriendScoresData.size <= 0) {
            return;
        }
        
        float width = getWidth();
        
        int topScore = finalFriendScoresData.get(0).getScore();
        
        for (int i = 0; i < finalFriendScoresData.size; i++) {
            FinalFriendScoreData scoreData = finalFriendScoresData.get(i);
            FacebookScoreEntryControl scoreEntryControl = new FacebookScoreEntryControl(scoreData, topScore, i + 1, width, guiSkin, assetManager);
            add(scoreEntryControl);
            row();
        }
    }
    
    private Array<FinalFriendScoreData> getFinalScoresData() {
        if (!isAllDataPresent()) {
            return null;
        }
        
        Array<FinalFriendScoreData> finalScoresData = new Array<FinalFriendScoreData>(true, friendsData.size + 1);
        
        String currentUserId = profileData.getId();
        int currentUserScore = scoreDataMap.containsKey(currentUserId) ? scoreDataMap.get(currentUserId).getScore() : 0;
        finalScoresData.add(new FinalFriendScoreData(currentUserId, profileData.getName(), currentUserScore, true));
        
        for (FacebookFriendData friendData : friendsData) {
            String friendId = friendData.getId();
            int friendScore = scoreDataMap.containsKey(friendId) ? scoreDataMap.get(friendId).getScore() : 0;
            FinalFriendScoreData finalFriendScoreData = new FinalFriendScoreData(friendId, friendData.getName(), friendScore, false);
            finalScoresData.add(finalFriendScoreData);
        }
        
        finalScoresData.sort(FRIEND_SCORE_DATA_COMPARATOR);
        
        return finalScoresData;
    }
    
    private boolean isAllDataPresent() {
        return profileData != null && friendsData != null && scoreDataMap != null;
    }
}
