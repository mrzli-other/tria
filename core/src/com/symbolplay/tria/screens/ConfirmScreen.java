package com.symbolplay.tria.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.resources.ResourceNames;
import com.symbolplay.tria.screens.general.ChangeParamKeys;

public final class ConfirmScreen extends ScreenBase {
    
    private final Image backgroundImage;
    
    private Label confirmTextLabel;
    
    public ConfirmScreen(GameContainer game) {
        super(game);
        
        guiStage.addListener(getStageInputListener(this));
        
        TextureAtlas atlas = assetManager.get(ResourceNames.GRAPHICS_GUI_ATLAS);
        
        NinePatch whiteNinePatch = atlas.createPatch(ResourceNames.GUI_WHITE_NINE_IMAGE_NAME);
        backgroundImage = new Image(whiteNinePatch);
        backgroundImage.setColor(1.0f, 1.0f, 1.0f, 0.95f);
        guiStage.addActor(backgroundImage);
        
        addContent(0.0f, 0.0f);
    }
    
    @Override
    public void show(ObjectMap<String, Object> changeParams) {
        super.show(changeParams);
        
        if (changeParams != null) {
            String confirmText = (String) changeParams.get(ChangeParamKeys.CONFIRM_TEXT, "");
            confirmTextLabel.setText(confirmText);
        } else {
            confirmTextLabel.setText("");
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
    
    private void addContent(float offsetFromTop, float offsetFromBottom) {
        
        LabelStyle labelStyle = guiSkin.get("font32", LabelStyle.class);
        
        float padding = 10.0f;
        
        float labelWidth = GameContainer.VIEWPORT_WIDTH - 2.0f * padding;
        confirmTextLabel = getLabel("", labelStyle);
        
        float buttonWidth = 200.0f;
        float buttonHeight = 60.0f;
        
        TextButton okButton = new TextButton("OK", guiSkin, "font32");
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.popScreen(getChangeParams(true));
            }
        });
        
        TextButton cancelButton = new TextButton("CANCEL", guiSkin, "font32");
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.popScreen(getChangeParams(false));
            }
        });
        
        float tableHeight = GameContainer.VIEWPORT_HEIGHT - offsetFromBottom - offsetFromTop;
        
        Table table = new Table(guiSkin);
        table.setBounds(0.0f, offsetFromBottom, GameContainer.VIEWPORT_WIDTH, tableHeight);
        table.center();
        table.add(confirmTextLabel).colspan(2).width(labelWidth).pad(padding).padTop(0.0f);
        table.row();
        table.add(okButton).width(buttonWidth).height(buttonHeight).pad(padding);
        table.add(cancelButton).width(buttonWidth).height(buttonHeight).pad(padding);
        
        guiStage.addActor(table);
    }
    
    private static Label getLabel(String text, LabelStyle labelStyle) {
        Label label = new Label(text, labelStyle);
        label.setAlignment(Align.center);
        label.setWrap(true);
        return label;
    }
    
    private static ObjectMap<String, Object> getChangeParams(boolean isConfirmOk) {
        ObjectMap<String, Object> changeParams = new ObjectMap<String, Object>(1);
        changeParams.put(ChangeParamKeys.CONFIRM_RESULT, isConfirmOk);
        return changeParams;
    }
    
    private static InputListener getStageInputListener(final ConfirmScreen screen) {
        return new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
                    screen.game.popScreen(getChangeParams(false));
                    return true;
                }
                
                return false;
            }
        };
    }
}
