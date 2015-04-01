package com.symbolplay.tria.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.persistence.GameData;
import com.symbolplay.tria.persistence.userdata.HighScoreData;

public final class HighScoreLocalSubscreen {
    
    private final Skin guiSkin;
    
    private final ScrollPane parentScrollPane;
    
    private final GameData gameData;
    
    private final Table table;
    
    public HighScoreLocalSubscreen(Skin guiSkin, ScrollPane parentScrollPane, GameData gameData) {
        
        this.guiSkin = guiSkin;
        
        this.parentScrollPane = parentScrollPane;
        
        this.gameData = gameData;
        
        table = createTable(guiSkin);
    }
    
    private Table createTable(Skin guiSkin) {
        Table table = new Table(guiSkin);
        table.top().left();
        return table;
    }
    
    private void fillHighScores() {
        float padding = 10.0f;
        
        table.clear();
        table.pad(padding);
        
        float entryPadding = 10.0f;
        float entryRankWidth = 56.0f;
        float entryScoreWidth = 160.0f;
        float entryNameWidth = GameContainer.VIEWPORT_WIDTH - entryRankWidth - entryScoreWidth - entryPadding * 2.0f - padding * 2.0f;
        
        LabelStyle entryLabelStyle = guiSkin.get("font24", LabelStyle.class);
        
        Array<HighScoreData> highScores = gameData.getAchievementsData().getHighScores();
        for (int i = 0; i < highScores.size; i++) {
            HighScoreData highScore = highScores.get(i);
            
            Label highScoreIndexLabel = new Label(String.valueOf(i + 1) + ".", entryLabelStyle);
            highScoreIndexLabel.setAlignment(Align.center);
            table.add(highScoreIndexLabel).width(entryRankWidth);
            
            Label highScoreNameLabel = new Label(highScore.getName(), entryLabelStyle);
            highScoreNameLabel.setAlignment(Align.left);
            table.add(highScoreNameLabel).width(entryNameWidth).padLeft(entryPadding).padRight(entryPadding);
            
            Label highScoreValueLabel = new Label(String.valueOf(highScore.getScore()), entryLabelStyle);
            highScoreValueLabel.setAlignment(Align.right);
            table.add(highScoreValueLabel).width(entryScoreWidth).right();
            
            table.row();
        }
    }
    
    public void selectSubscreen() {
        parentScrollPane.setWidget(table);
        fillHighScores();
    }
    
    public void unselectSubscreen() {
    }
}
