package com.symbolplay.tria.screens.general;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.symbolplay.tria.persistence.userdata.HighScoreData;
import com.symbolplay.tria.resources.ResourceNames;

public final class GlobalScoreEntryControl extends Group {
    
    public GlobalScoreEntryControl(HighScoreData scoreData, int topScore, int rank, float parentWidth, Skin guiSkin, AssetManager assetManager) {
        
        Table entryTable = getEntryTable(scoreData, rank, parentWidth, guiSkin);
        setSize(entryTable.getWidth(), entryTable.getHeight());
        
        if (scoreData.getScore() > 0) {
            float colorComponent = 0.6f;
            Color scoreBarColor = new Color(colorComponent, colorComponent, colorComponent, 0.5f);
            float scoreBarFraction = (float) scoreData.getScore() / (float) topScore;
            
            TextureAtlas atlas = assetManager.get(ResourceNames.GRAPHICS_GUI_ATLAS);
            NinePatch whiteNinePatch = atlas.createPatch(ResourceNames.GUI_WHITE_NINE_IMAGE_NAME);
            whiteNinePatch.setColor(scoreBarColor);
            Image image = new Image(whiteNinePatch);
            image.setSize(parentWidth * scoreBarFraction, entryTable.getHeight());
            addActor(image);
        }
        
        addActor(entryTable);
        
    }
    
    private Table getEntryTable(HighScoreData scoreData, int rank, float parentWidth, Skin guiSkin) {
        float entryPadding = 10.0f;
        float entryRankWidth = 56.0f;
        float entryScoreWidth = 160.0f;
        float entryNameWidth = parentWidth - entryRankWidth - entryScoreWidth - entryPadding * 2.0f;
        
        LabelStyle entryLabelStyle = guiSkin.get("font24", LabelStyle.class);
        
        Table table = new Table(guiSkin);
        table.setSize(parentWidth, 38.0f);
        
        table.clear();
        
        Label highScoreIndexLabel = new Label(String.valueOf(rank) + ".", entryLabelStyle);
        highScoreIndexLabel.setAlignment(Align.center);
        table.add(highScoreIndexLabel).width(entryRankWidth);
        
        Label highScoreNameLabel = new Label(scoreData.getName(), entryLabelStyle);
        highScoreNameLabel.setAlignment(Align.left);
        table.add(highScoreNameLabel).width(entryNameWidth).padLeft(entryPadding).padRight(entryPadding);
        
        Label highScoreValueLabel = new Label(String.valueOf(scoreData.getScore()), entryLabelStyle);
        highScoreValueLabel.setAlignment(Align.right);
        table.add(highScoreValueLabel).width(entryScoreWidth).padRight(entryPadding).right();
        
        return table;
    }
}
