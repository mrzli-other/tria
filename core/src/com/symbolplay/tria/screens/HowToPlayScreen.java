package com.symbolplay.tria.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.background.GameBackground;
import com.symbolplay.tria.resources.ResourceNames;
import com.symbolplay.tria.screens.general.GameScreenUtils;

public final class HowToPlayScreen extends ScreenBase {
    
    private final GameBackground background;
    
    public HowToPlayScreen(GameContainer game) {
        super(game);
        
        guiStage.addListener(getStageInputListener(this));
        
        background = new GameBackground(assetManager);
        
        float offsetFromTop = GameScreenUtils.addTitleLabel("HOW TO PLAY", guiSkin, guiStage);
        addContent(offsetFromTop, 0.0f);
    }
    
    @Override
    public void show(ObjectMap<String, Object> changeParams) {
        super.show(changeParams);
        gameData.getGamePreferences().setHowToPlayShown(true);
    }
    
    @Override
    public void renderImpl() {
        batch.setProjectionMatrix(cameraData.getGameAreaMatrix());
        batch.begin();
        background.render(batch);
        batch.end();
    }
    
    private void addContent(float offsetFromTop, float offsetFromBottom) {
        
        LabelStyle labelStyle = guiSkin.get("font24", LabelStyle.class);
        TextureAtlas atlas = assetManager.get(ResourceNames.GRAPHICS_GUI_ATLAS);
        
        float labelPadding = 10.0f;
        float topLabelPadding = labelPadding * 2.5f;
        float labelWidth = GameContainer.VIEWPORT_WIDTH - 2.0f * labelPadding;
        
        Label characterControlLabel = getLabel("Tilt the phone to move the character left or right:", labelStyle);
        Image characterControlImage = getImage(ResourceNames.GUI_HOW_TO_PLAY_CHARACTER_CONTROL_IMAGE_NAME, atlas);
        
        Label goThroughSidesLabel = getLabel("Moving the character through the left or right side of the screen will make it appear on the other side - " +
                "you will need to move like this to pass some areas:", labelStyle);
        Image goThroughSidesImage = getImage(ResourceNames.GUI_HOW_TO_PLAY_GO_THROUGH_SIDES_IMAGE_NAME, atlas);
        
        Label itemsLabel = getLabel("Things that HELP you:", labelStyle);
        Image itemsImage = getImage(ResourceNames.GUI_HOW_TO_PLAY_ITEMS_IMAGE_NAME, atlas);
        
        Label enemiesLabel = getLabel("Things that KILL you:", labelStyle);
        Image enemiesImage = getImage(ResourceNames.GUI_HOW_TO_PLAY_ENEMIES_IMAGE_NAME, atlas);
        
        Table table = GameScreenUtils.addScrollTable(offsetFromTop, offsetFromBottom, guiSkin, guiStage);
        
        table.add(characterControlLabel).width(labelWidth).pad(labelPadding).padTop(0.0f);
        table.row();
        table.add(characterControlImage).width(GameContainer.VIEWPORT_WIDTH);
        table.row();
        table.add(goThroughSidesLabel).width(labelWidth).pad(labelPadding).padTop(topLabelPadding);
        table.row();
        table.add(goThroughSidesImage).width(GameContainer.VIEWPORT_WIDTH);
        table.row();
        table.add(itemsLabel).width(labelWidth).pad(labelPadding).padTop(topLabelPadding);
        table.row();
        table.add(itemsImage).width(GameContainer.VIEWPORT_WIDTH);
        table.row();
        table.add(enemiesLabel).width(labelWidth).pad(labelPadding).padTop(topLabelPadding);
        table.row();
        table.add(enemiesImage).width(GameContainer.VIEWPORT_WIDTH);
    }
    
    private static Label getLabel(String text, LabelStyle labelStyle) {
        Label label = new Label(text, labelStyle);
        label.setWrap(true);
        return label;
    }
    
    private static Image getImage(String imageName, TextureAtlas atlas) {
        TextureRegion imageRegion = atlas.findRegion(imageName);
        Image image = new Image(imageRegion);
        return image;
    }
    
    private static InputListener getStageInputListener(final HowToPlayScreen screen) {
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
}
