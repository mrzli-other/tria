package com.symbolplay.tria.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.gamelibrary.game.CameraData;
import com.symbolplay.gamelibrary.game.Screen;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.persistence.GameData;
import com.symbolplay.tria.resources.Resources;

// Initially based on Gustavo Steigert's AbstractScreen class
// http://steigert.blogspot.com/2012/02/2-libgdx-tutorial-game-screens.html
public abstract class ScreenBase implements Screen {
    
    protected final GameContainer game;
    protected final Resources resources;
    protected final AssetManager assetManager;
    protected final Skin guiSkin;
    protected final GameData gameData;
    protected final CameraData cameraData;
    
    protected final SpriteBatch batch;
    protected final Stage guiStage;
    
    protected final Color clearColor;
    
    protected final Rectangle guiCameraRect;
    
    public ScreenBase(GameContainer game) {
        this.game = game;
        resources = game.getResources();
        assetManager = resources.getAssetManager();
        guiSkin = resources.getGuiSkin();
        gameData = game.getGameData();
        cameraData = game.getCameraData();
        
        guiCameraRect = cameraData.getGuiCameraRect();
        
        batch = new SpriteBatch();
        
        guiStage = new Stage();
        guiStage.setViewport(cameraData.getGuiViewport());
        
        clearColor = new Color(Color.BLACK);
    }
    
    @Override
    public void show(ObjectMap<String, Object> changeParams) {
        Gdx.input.setInputProcessor(guiStage);
    }
    
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
    
    @Override
    public void pause() {
        Gdx.input.setInputProcessor(null);
    }
    
    @Override
    public void resume(ObjectMap<String, Object> changeParams) {
        Gdx.input.setInputProcessor(guiStage);
    }
    
    @Override
    public final void update(float delta) {
        updateImpl(delta);
        guiStage.act(delta);
    }
    
    protected void updateImpl(float delta) {
    }
    
    @Override
    public final void render() {
        
        if (!isTransparent()) {
            if (clearColor != null) {
                Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
            } else {
                Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            }
            
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
        
        renderImpl();
        
        guiStage.draw();
    }
    
    protected void renderImpl() {
    }
    
    @Override
    public void resize(int width, int height) {
    }
    
    @Override
    public void dispose() {
        guiStage.dispose();
        batch.dispose();
    }
    
    @Override
    public boolean isTransparent() {
        return false;
    }
}