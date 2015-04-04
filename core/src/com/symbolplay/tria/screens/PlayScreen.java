package com.symbolplay.tria.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.Sounds;
import com.symbolplay.tria.game.rise.Rise;
import com.symbolplay.tria.resources.ResourceNames;
import com.symbolplay.tria.screens.general.ChangeParamKeys;
import com.symbolplay.tria.screens.playscore.PlayScoreAccessor;

public final class PlayScreen extends ScreenBase {
    
    // private static final long DEBUG_LABEL_UPDATE_INTERVAL = 100l;
    
    private final Rise rise;
    
    private Label livesLabel;
    private Label coinsLabel;
    private Label scoreLabel;
    
    // private final Label debugLabel;
    // private long debugLabelLastUpdateTime = 0l;
    
    public PlayScreen(GameContainer game) {
        super(game);
        
        guiStage.addListener(getStageInputListener());
        
        PlayScoreAccessor playScoreAccessor = new PlayScoreAccessor(gameData, game.getTriaServiceAccessor());
        rise = new Rise(cameraData, gameData.getCareerData(), resources.getGameAreaFont(), playScoreAccessor, assetManager);
        
        addTopDisplay();
        
        // debugLabel = new Label("", guiSkin);
        // debugLabel.setBounds(0.0f, 0.0f, 20.0f, 72.0f);
        // debugLabel.setAlignment(Align.left);
        // guiStage.addActor(debugLabel);
    }
    
    @Override
    public void show(ObjectMap<String, Object> changeParams) {
        super.show(changeParams);
        rise.reset();
    }
    
    @Override
    public void hide() {
        Sounds.stopAll();
        cameraData.setGameAreaPosition(0.0f, 0.0f);
        super.hide();
    }
    
    @Override
    public void pause() {
        Sounds.pauseAll();
        super.pause();
    }
    
    @Override
    public void resume(ObjectMap<String, Object> changeParams) {
        super.resume(changeParams);
        if (changeParams != null && (Boolean) changeParams.get(ChangeParamKeys.EXIT_PLAY)) {
            updateCareerData();
            game.changeScreen(GameContainer.MAIN_MENU_SCREEN_NAME);
        } else {
            Sounds.resumeAll();
        }
    }
    
    @Override
    protected void updateImpl(float delta) {
        
        // Profiling.startTime(1);
        
        if (rise.isFinished()) {
            updateCareerData();
            ObjectMap<String, Object> changeParams = new ObjectMap<String, Object>(1);
            changeParams.put(ChangeParamKeys.SCORE, rise.getScore());
            game.changeScreen(GameContainer.GAME_OVER_SCREEN_NAME, changeParams);
        }
        
        rise.update(delta);
        
        livesLabel.setText("x" + String.valueOf(rise.getLives()));
        coinsLabel.setText("x" + String.valueOf(rise.getCoins()));
        scoreLabel.setText(String.valueOf(rise.getScore()));
        
        // Profiling.endTime(1, "Update", true);
    }
    
    @Override
    public void renderImpl() {
        
        // Profiling.startTime(2);
        
        rise.render();
        
        // TODO: only for debugging
        // if (System.currentTimeMillis() - debugLabelLastUpdateTime >= DEBUG_LABEL_UPDATE_INTERVAL) {
        // DebugData debugData = rise.getDebugData();
        // debugLabel.setText(debugData.toString());
        // debugLabelLastUpdateTime = System.currentTimeMillis();
        // }
        
        // Profiling.endTime(2, "Render", true);
    }
    
    private void addTopDisplay() {
        TextureAtlas atlas = assetManager.get(ResourceNames.GRAPHICS_GUI_ATLAS);
        
        float topDisplayHeight = 40.0f;
        float padding = 5.0f;
        float topDisplayY = GameContainer.VIEWPORT_HEIGHT - topDisplayHeight;
        float topLineThickness = 2.0f;
        
        Image topBackgroundImage = getImage(atlas, ResourceNames.GUI_PLAY_TOP_BACKGROUND_IMAGE_NAME);
        topBackgroundImage.setBounds(0.0f, topDisplayY, GameContainer.VIEWPORT_WIDTH, topDisplayHeight);
        topBackgroundImage.setColor(1.0f, 1.0f, 1.0f, 0.8f);
        guiStage.addActor(topBackgroundImage);
        
        Image topLineImage = getImage(atlas, ResourceNames.GUI_PLAY_TOP_LINE_IMAGE_NAME);
        topLineImage.setBounds(0.0f, topDisplayY, GameContainer.VIEWPORT_WIDTH, topLineThickness);
        topLineImage.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        guiStage.addActor(topLineImage);
        
        ImageButton pauseButton = new ImageButton(GameUtils.getTextureDrawable(atlas, ResourceNames.GUI_PLAY_PAUSE_IMAGE_NAME));
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.pushScreen(GameContainer.PAUSE_SCREEN_NAME);
            }
        });
        
        Image livesImage = getImage(atlas, ResourceNames.GUI_PLAY_LIVES_IMAGE_NAME);
        guiStage.addActor(livesImage);
        
        livesLabel = new Label("", guiSkin, "font24");
        livesLabel.setAlignment(Align.center | Align.left);
        guiStage.addActor(livesLabel);
        
        Image coinsImage = getImage(atlas, ResourceNames.GUI_PLAY_COINS_IMAGE_NAME);
        guiStage.addActor(coinsImage);
        
        coinsLabel = new Label("", guiSkin, "font24");
        coinsLabel.setAlignment(Align.center | Align.left);
        guiStage.addActor(coinsLabel);
        
        scoreLabel = new Label("", guiSkin, "font32");
        scoreLabel.setAlignment(Align.center | Align.right);
        guiStage.addActor(scoreLabel);
        
        float livesLabelWidth = 55.0f;
        float coinsLabelWidth = 83.0f;
        float scoreLabelWidth = GameContainer.VIEWPORT_WIDTH - padding * 9.0f - livesLabelWidth - coinsLabelWidth -
                pauseButton.getWidth() - livesImage.getWidth() - coinsImage.getWidth();
        
        Table table = new Table(guiSkin);
        table.top().left();
        table.setBounds(0.0f, topDisplayY, GameContainer.VIEWPORT_WIDTH, topDisplayHeight);
        table.add(pauseButton).padRight(padding);
        table.add(livesImage).padLeft(padding).padRight(padding).center();
        table.add(livesLabel).width(livesLabelWidth).padTop(2.0f).padRight(padding + 2.0f).center();
        table.add(coinsImage).padLeft(padding).padTop(2.0f).padRight(padding).center();
        table.add(coinsLabel).width(coinsLabelWidth).padTop(2.0f).padRight(padding).center();
        table.add(scoreLabel).width(scoreLabelWidth).padTop(-3.0f).padBottom(-3.0f).padRight(padding);
        guiStage.addActor(table);
    }
    
    private static Image getImage(TextureAtlas atlas, String imageName) {
        Image image = new Image(GameUtils.getTextureDrawable(atlas, imageName));
        return image;
    }
    
    private void updateCareerData() {
        gameData.getCareerData().changeCoins(rise.getCoins());
        gameData.getCareerData().setInitialLivesNextGame(0);
    }
    
    private InputListener getStageInputListener() {
        return new InputListener() {
            
            private boolean isBackAlreadyPressed = false;
            
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if ((keycode == Keys.ESCAPE || keycode == Keys.BACK) && !isBackAlreadyPressed) {
                    game.pushScreen(GameContainer.PAUSE_SCREEN_NAME);
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