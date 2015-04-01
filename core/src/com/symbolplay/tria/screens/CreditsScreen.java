package com.symbolplay.tria.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.gamelibrary.controls.HighlightImageButton;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.background.GameBackground;
import com.symbolplay.tria.resources.ResourceNames;
import com.symbolplay.tria.screens.general.ChangeParamKeys;
import com.symbolplay.tria.screens.general.GameScreenUtils;

public final class CreditsScreen extends ScreenBase {
    
    private static final int CONFIRM_DIALOG_TARGET_NONE = 0;
    private static final int CONFIRM_DIALOG_TARGET_COMPANY = 1;
    private static final int CONFIRM_DIALOG_TARGET_LIBGDX = 2;
    
    private final GameBackground background;
    
    private int confirmTarget;
    
    public CreditsScreen(GameContainer game) {
        super(game);
        
        guiStage.addListener(getStageInputListener(this));
        
        background = new GameBackground(assetManager);
        
        float offsetFromTop = GameScreenUtils.addTitleLabel("CREDITS", guiSkin, guiStage);
        createContent(offsetFromTop, 0.0f);
        
        confirmTarget = CONFIRM_DIALOG_TARGET_NONE;
    }
    
    @Override
    public void resume(ObjectMap<String, Object> changeParams) {
        super.resume(changeParams);
        
        if (changeParams != null) {
            Boolean isConfirmOk = (Boolean) changeParams.get(ChangeParamKeys.CONFIRM_RESULT, false);
            if (isConfirmOk) {
                if (confirmTarget == CONFIRM_DIALOG_TARGET_COMPANY) {
                    goToCompanyAddress();
                } else if (confirmTarget == CONFIRM_DIALOG_TARGET_LIBGDX) {
                    goToLibGdxAddress();
                }
            }
        }
        confirmTarget = CONFIRM_DIALOG_TARGET_NONE;
    }
    
    @Override
    public void renderImpl() {
        batch.setProjectionMatrix(cameraData.getGameAreaMatrix());
        batch.begin();
        background.render(batch);
        batch.end();
    }
    
    private void createContent(float offsetFromTop, float offsetFromBottom) {
        LabelStyle labelStyle = guiSkin.get("font24", LabelStyle.class);
        TextureAtlas atlas = assetManager.get(ResourceNames.GRAPHICS_GUI_ATLAS);
        
        float padding = 10.0f;
        float imageButtonPadding = padding * 3.0f;
        
        HighlightImageButton companyLogoButton = new HighlightImageButton(
                0.0f,
                0.0f,
                atlas,
                ResourceNames.GUI_CREDITS_COMPANY_LOGO_IMAGE_NAME,
                ResourceNames.GUI_CREDITS_COMPANY_LOGO_IMAGE_NAME,
                assetManager);
        companyLogoButton.addListener(getCompanyLogoClickListener());
        
        HighlightImageButton libGdxLogoButton = new HighlightImageButton(
                0.0f,
                0.0f,
                atlas,
                ResourceNames.GUI_CREDITS_LIBGDX_LOGO_IMAGE_NAME,
                ResourceNames.GUI_CREDITS_LIBGDX_LOGO_IMAGE_NAME,
                assetManager);
        libGdxLogoButton.addListener(getLibGdxLogoClickListener());
        
        Table imageButtonTable = new Table(guiSkin);
        imageButtonTable.add(companyLogoButton).pad(imageButtonPadding);
        imageButtonTable.add(libGdxLogoButton).pad(imageButtonPadding);
        
        FileHandle creditsFileHandle = Gdx.files.internal(ResourceNames.CREDITS);
        String creditsText = creditsFileHandle.readString("UTF-8");
        Label creditsLabel = new Label(creditsText, labelStyle);
        creditsLabel.setAlignment(Align.left);
        
        Table table = GameScreenUtils.addScrollTable(offsetFromTop, offsetFromBottom, guiSkin, guiStage);
        table.add(imageButtonTable).center();
        table.row();
        table.add(creditsLabel).pad(padding);
    }
    
    private static ObjectMap<String, Object> getChangeParams() {
        ObjectMap<String, Object> changeParams = new ObjectMap<String, Object>(1);
        changeParams.put(ChangeParamKeys.CONFIRM_TEXT, "This action will open a browser window.");
        return changeParams;
    }
    
    private void goToCompanyAddress() {
        game.getPlatformSpecificInterface().goToAddress(null, "http://symbolplay.com");
    }
    
    private void goToLibGdxAddress() {
        game.getPlatformSpecificInterface().goToAddress(null, "http://libgdx.badlogicgames.com");
    }
    
    private static InputListener getStageInputListener(final CreditsScreen screen) {
        return new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
                    screen.game.changeScreen(GameContainer.MAIN_MENU_SCREEN_NAME);
                    return true;
                }
                
                return false;
            }
        };
    }
    
    private ClickListener getCompanyLogoClickListener() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.getGameData().getGamePreferences().isConfirmDialogsEnabled()) {
                    confirmTarget = CONFIRM_DIALOG_TARGET_COMPANY;
                    game.pushScreen(GameContainer.CONFIRM_SCREEN_NAME, getChangeParams());
                } else {
                    goToCompanyAddress();
                }
            }
        };
    }
    
    private ClickListener getLibGdxLogoClickListener() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.getGameData().getGamePreferences().isConfirmDialogsEnabled()) {
                    confirmTarget = CONFIRM_DIALOG_TARGET_LIBGDX;
                    game.pushScreen(GameContainer.CONFIRM_SCREEN_NAME, getChangeParams());
                } else {
                    goToLibGdxAddress();
                }
            }
        };
    }
}
