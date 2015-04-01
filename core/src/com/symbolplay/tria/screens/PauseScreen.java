package com.symbolplay.tria.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.resources.ResourceNames;
import com.symbolplay.tria.screens.general.ChangeParamKeys;

public final class PauseScreen extends ScreenBase {
    
    private static final float BUTTON_X = 75.0f;
    private static final float BOTTOM_BUTTON_Y = 40.0f;
    private static final float BUTTON_STEP_Y = 120.0f;
    private static final float BUTTON_WIDTH = 300.0f;
    private static final float BUTTON_HEIGHT = 80.0f;
    
    private final Image backgroundImage;
    
    public PauseScreen(GameContainer game) {
        super(game);
        
        guiStage.addListener(getStageInputListener());
        
        TextureAtlas atlas = assetManager.get(ResourceNames.GRAPHICS_GUI_ATLAS);
        
        NinePatch whiteNinePatch = atlas.createPatch(ResourceNames.GUI_WHITE_NINE_IMAGE_NAME);
        backgroundImage = new Image(whiteNinePatch);
        backgroundImage.setColor(1.0f, 1.0f, 1.0f, 0.9f);
        guiStage.addActor(backgroundImage);
        
        Label pausedLabel = new Label("PAUSED", guiSkin, "font40");
        pausedLabel.setBounds(0.0f, 500.0f, GameContainer.VIEWPORT_WIDTH, 50.0f);
        pausedLabel.setAlignment(Align.center);
        guiStage.addActor(pausedLabel);
        
        TextButton resumeButton = new TextButton("RESUME", guiSkin, "font40");
        resumeButton.setBounds(BUTTON_X, BOTTOM_BUTTON_Y + BUTTON_STEP_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PauseScreen.this.game.popScreen(getChangeParams(false));
            }
        });
        guiStage.addActor(resumeButton);
        
        TextButton exitButton = new TextButton("EXIT", guiSkin, "font40");
        exitButton.setBounds(BUTTON_X, BOTTOM_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PauseScreen.this.game.popScreen(getChangeParams(true));
            }
        });
        guiStage.addActor(exitButton);
    }
    
    @Override
    public boolean isTransparent() {
        return true;
    }
    
    @Override
    public void renderImpl() {
        backgroundImage.setBounds(guiCameraRect.x, guiCameraRect.y, guiCameraRect.width, guiCameraRect.height);
    }
    
    private static ObjectMap<String, Object> getChangeParams(boolean isExitPlay) {
        ObjectMap<String, Object> changeParams = new ObjectMap<String, Object>(1);
        changeParams.put(ChangeParamKeys.EXIT_PLAY, isExitPlay);
        return changeParams;
    }
    
    private InputListener getStageInputListener() {
        return new InputListener() {
            
            private boolean isBackAlreadyPressed = false;
            
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if ((keycode == Keys.ESCAPE || keycode == Keys.BACK) && !isBackAlreadyPressed) {
                    game.popScreen(getChangeParams(false));
                    isBackAlreadyPressed = true;
                    return true;
                }
                
                return false;
            }
            
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
                    isBackAlreadyPressed = false;
                    return true;
                }
                
                return false;
            }
        };
    }
}