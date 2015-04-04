package com.symbolplay.tria.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.net.TriaServiceAccessor;
import com.symbolplay.tria.screens.general.GlobalScoresControl;

public final class HighScoreGlobalSubscreen {
    
    private final AssetManager assetManager;
    private final Skin guiSkin;
    
    private final ScrollPane parentScrollPane;
    
    private GlobalScoresControl globalScoresControl;
    
    private boolean isSelected;
    
    public HighScoreGlobalSubscreen(
            AssetManager assetManager,
            Skin guiSkin,
            ScrollPane parentScrollPane,
            TriaServiceAccessor triaServiceAccessor) {
        
        this.assetManager = assetManager;
        this.guiSkin = guiSkin;
        
        this.parentScrollPane = parentScrollPane;
        
        createControls(triaServiceAccessor);
        
        isSelected = false;
    }
    
    public void update(float delta) {
        if (!isSelected) {
            return;
        }
    }
    
    private void createControls(TriaServiceAccessor triaServiceAccessor) {
        globalScoresControl = new GlobalScoresControl(assetManager, guiSkin, triaServiceAccessor);
    }
    
    public void selectSubscreen() {
        isSelected = true;
        
        parentScrollPane.setWidget(getContent());
        globalScoresControl.setActive(true);     
    }
    
    public void unselectSubscreen() {
        isSelected = false;
        globalScoresControl.setActive(false);
    }
    
    private Table getContent() {
        float padding = 10.0f;
        float contentWidth = GameContainer.VIEWPORT_WIDTH - 2.0f * padding;
        
        Table table = new Table(guiSkin);
        table.top().left();
        
        table.add(globalScoresControl).width(contentWidth).pad(padding);
        
        return table;
    }
}
