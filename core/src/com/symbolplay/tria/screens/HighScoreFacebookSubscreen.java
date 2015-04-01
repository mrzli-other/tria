package com.symbolplay.tria.screens;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.net.FacebookAccessor;
import com.symbolplay.tria.net.FacebookLoginStateListener;
import com.symbolplay.tria.net.TriaServiceAccessor;
import com.symbolplay.tria.screens.general.FacebookScoresControl;

public final class HighScoreFacebookSubscreen {
    
    private final AssetManager assetManager;
    private final Skin guiSkin;
    
    private final FacebookAccessor facebookAccessor;
    
    private final ScrollPane parentScrollPane;
    
    private FacebookScoresControl facebookScoresControl;
    
    private boolean isSelected;
    
    public HighScoreFacebookSubscreen(
            AssetManager assetManager,
            Skin guiSkin,
            ScrollPane parentScrollPane,
            FacebookAccessor facebookAccessor,
            TriaServiceAccessor triaServiceAccessor) {
        
        this.assetManager = assetManager;
        this.guiSkin = guiSkin;
        
        this.facebookAccessor = facebookAccessor;
        facebookAccessor.addLoginStateListener(new FacebookLoginStateListener() {
            @Override
            public void loginStateChanged(boolean isLoggedInState, String loginStatusMessage) {
                if (isLoggedInState) {
                    setLoggedInContent();
                } else {
                    setLoggedOutContent(loginStatusMessage);
                }
            }
        });
        
        this.parentScrollPane = parentScrollPane;
        
        createControls(facebookAccessor, triaServiceAccessor);
        
        isSelected = false;
    }
    
    public void update(float delta) {
        if (!isSelected) {
            return;
        }
    }
    
    private void createControls(FacebookAccessor facebookAccessor, TriaServiceAccessor triaServiceAccessor) {
        facebookScoresControl = new FacebookScoresControl(assetManager, guiSkin, facebookAccessor, triaServiceAccessor);
    }
    
    public void selectSubscreen() {
        isSelected = true;
        
        if (facebookAccessor.isLoggedIn()) {
            setLoggedInContent();
        } else {
            setLoggedOutContent("");
        }        
    }
    
    public void unselectSubscreen() {
        isSelected = false;
        facebookScoresControl.setActive(false);
    }
    
    private void setLoggedOutContent(String statusMessage) {
        parentScrollPane.setWidget(getLoggedOutContent(statusMessage));
        facebookScoresControl.setActive(false);
    }
    
    private void setLoggedInContent() {
        parentScrollPane.setWidget(getLoggedInContent());
        facebookScoresControl.setActive(true);
    }
    
    private Table getLoggedOutContent(String statusMessage) {
        float padding = 10.0f;
        float contentWidth = GameContainer.VIEWPORT_WIDTH - 2.0f * padding;
        
        LabelStyle labelStyle = guiSkin.get("font24", LabelStyle.class);
        
        Table table = new Table(guiSkin);
        table.top().left();
        
        if (StringUtils.isEmpty(statusMessage)) {
            String text1 = "Log in to Facebook on main menu screen to see how you rank compared to your friends.";
            Label label1 = getLabel(text1, labelStyle);
            table.add(label1).width(contentWidth).pad(padding);
        } else {
            Label loginStatusLabel = getLabel(statusMessage, labelStyle);
            table.add(loginStatusLabel).width(contentWidth).pad(padding);
        }
        
        return table;
    }
    
    private Table getLoggedInContent() {
        float padding = 10.0f;
        float contentWidth = GameContainer.VIEWPORT_WIDTH - 2.0f * padding;
        
        Table table = new Table(guiSkin);
        table.top().left();
        
        table.add(facebookScoresControl).width(contentWidth).pad(padding);
        
        return table;
    }
    
    private static Label getLabel(String text, LabelStyle labelStyle) {
        Label label = new Label(text, labelStyle);
        label.setWrap(true);
        return label;
    }
}
