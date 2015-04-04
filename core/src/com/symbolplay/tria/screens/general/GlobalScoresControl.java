package com.symbolplay.tria.screens.general;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.net.TriaServiceAccessor;
import com.symbolplay.tria.net.TriaServiceScoreListener;
import com.symbolplay.tria.persistence.userdata.HighScoreData;
import com.symbolplay.tria.persistence.userdata.UserData;

public class GlobalScoresControl extends Table {
    
    private final AssetManager assetManager;
    private final Skin guiSkin;
    
    private final TriaServiceAccessor triaServiceAccessor;
    
    private boolean isActive;
    
    private Array<HighScoreData> scoresData;
    
    public GlobalScoresControl(AssetManager assetManager, Skin guiSkin, TriaServiceAccessor triaServiceAccessor) {
        this.assetManager = assetManager;
        this.guiSkin = guiSkin;
        
        this.triaServiceAccessor = triaServiceAccessor;
        triaServiceAccessor.addScoreListener(new TriaServiceScoreListener() {
            
            @Override
            public void scoresReceived(Array<HighScoreData> scoresData) {
                GlobalScoresControl.this.scoresData = scoresData;
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
            triaServiceAccessor.requestGlobalScores();
        }
    }
    
    private void clearData() {
        scoresData = null;
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
        
        if (scoresData == null) {
            return;
        }
        
        clear();
        left().top();
        
        
        float width = getWidth();
        
        int topScore = scoresData.size > 0 ? scoresData.get(0).getScore() : 1;
        
        for (int i = 0; i < scoresData.size; i++) {
            HighScoreData scoreData = scoresData.get(i);
            GlobalScoreEntryControl scoreEntryControl = new GlobalScoreEntryControl(scoreData, topScore, i + 1, width, guiSkin, assetManager);
            add(scoreEntryControl);
            row();
        }
        
        for (int i = scoresData.size; i < 100; i++) {
            HighScoreData scoreData = new HighScoreData(UserData.DEFAULT_HIGH_SCORE_NAME, 0, 0L);
            GlobalScoreEntryControl scoreEntryControl = new GlobalScoreEntryControl(scoreData, topScore, i + 1, width, guiSkin, assetManager);
            add(scoreEntryControl);
            row();
        }
    }
}
