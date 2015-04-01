package com.symbolplay.tria.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.persistence.gamepreferences.GamePreferencesWrapper;
import com.symbolplay.tria.resources.ResourceNames;
import com.symbolplay.tria.screens.general.ChangeParamKeys;

public final class RateRequestScreen extends ScreenBase {
    
    private static final float BUTTON_X = 75.0f;
    private static final float BOTTOM_BUTTON_Y = 40.0f;
    private static final float BUTTON_STEP_Y = 120.0f;
    private static final float BUTTON_WIDTH = 300.0f;
    private static final float BUTTON_HEIGHT = 80.0f;
    
    private final Image backgroundImage;
    
    public RateRequestScreen(GameContainer game) {
        super(game);
        
        guiStage.addListener(getStageInputListener(this));
        
        TextureAtlas atlas = assetManager.get(ResourceNames.GRAPHICS_GUI_ATLAS);
        
        NinePatch whiteNinePatch = atlas.createPatch(ResourceNames.GUI_WHITE_NINE_IMAGE_NAME);
        backgroundImage = new Image(whiteNinePatch);
        backgroundImage.setColor(1.0f, 1.0f, 1.0f, 0.95f);
        guiStage.addActor(backgroundImage);
        
        addText(0.0f, BOTTOM_BUTTON_Y + 3.0f * BUTTON_STEP_Y);
        
        TextButton rateButton = new TextButton("RATE", guiSkin, "font40");
        rateButton.setBounds(BUTTON_X, BOTTOM_BUTTON_Y + 2.0f * BUTTON_STEP_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        rateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (RateRequestScreen.this.game.getGameData().getGamePreferences().isConfirmDialogsEnabled()) {
                    RateRequestScreen.this.game.pushScreen(GameContainer.CONFIRM_SCREEN_NAME, getChangeParams());
                } else {
                    rate();
                }
            }
        });
        guiStage.addActor(rateButton);
        
        TextButton laterButton = new TextButton("LATER", guiSkin, "font40");
        laterButton.setBounds(BUTTON_X, BOTTOM_BUTTON_Y + BUTTON_STEP_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        laterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                RateRequestScreen.this.game.popScreen();
            }
        });
        guiStage.addActor(laterButton);
        
        TextButton neverButton = new TextButton("NEVER", guiSkin, "font40");
        neverButton.setBounds(BUTTON_X, BOTTOM_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        neverButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameData.getGamePreferences().setRatingRequestDisabled(true);
                RateRequestScreen.this.game.popScreen();
            }
        });
        guiStage.addActor(neverButton);
    }
    
    @Override
    public void show(ObjectMap<String, Object> changeParams) {
        super.show(changeParams);
        
        GamePreferencesWrapper gamePreferences = gameData.getGamePreferences();
        gamePreferences.setGamesPlayedSinceLastRatingRequest(0);
        gamePreferences.setLastRatingRequestTime(System.currentTimeMillis());
    }
    
    @Override
    public void resume(ObjectMap<String, Object> changeParams) {
        super.resume(changeParams);
        
        if (changeParams != null) {
            Boolean isConfirmOk = (Boolean) changeParams.get(ChangeParamKeys.CONFIRM_RESULT, false);
            if (isConfirmOk) {
                rate();
            }
        }
    }
    
    @Override
    public boolean isTransparent() {
        return true;
    }
    
    @Override
    public void renderImpl() {
        backgroundImage.setBounds(guiCameraRect.x, guiCameraRect.y, guiCameraRect.width, guiCameraRect.height);
    }
    
    private void addText(float offsetFromTop, float offsetFromBottom) {
        LabelStyle labelStyle = guiSkin.get("font40", LabelStyle.class);
        
        String rateText = "Please consider\nrating\nSymbol Jump!";
        
        float rateTextLabelHeight = GameContainer.VIEWPORT_HEIGHT - offsetFromTop - offsetFromBottom;
        Label rateTextLabel = new Label(rateText, labelStyle);
        rateTextLabel.setBounds(0.0f, offsetFromBottom, GameContainer.VIEWPORT_WIDTH, rateTextLabelHeight);
        rateTextLabel.setAlignment(Align.center);
        
        guiStage.addActor(rateTextLabel);
    }
    
    private void rate() {
        gameData.getGamePreferences().setRatingRequestDisabled(true);
        game.popScreen();
        game.getPlatformSpecificInterface().rate();
    }
    
    private static ObjectMap<String, Object> getChangeParams() {
        ObjectMap<String, Object> changeParams = new ObjectMap<String, Object>(1);
        changeParams.put(ChangeParamKeys.CONFIRM_TEXT, "This action will open a browser window or Google Play application.");
        return changeParams;
    }
    
    private static InputListener getStageInputListener(final RateRequestScreen screen) {
        return new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
                    screen.game.popScreen();
                    return true;
                }
                
                return false;
            }
        };
    }
}
