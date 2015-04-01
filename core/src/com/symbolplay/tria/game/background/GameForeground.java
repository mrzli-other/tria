package com.symbolplay.tria.game.background;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.resources.ResourceNames;

public final class GameForeground {
    
    private static final float SIDE_WIDTH = 1.875f;
    private static final float SIDE_HEIGHT = 20.0f;
    
    private static final float LEFT_SIDE_X = -SIDE_WIDTH;
    private static final float RIGHT_SIDE_X = GameContainer.GAME_AREA_WIDTH;
    
    private static final Color COLOR;
    
    private final Sprite leftSprite;
    private final Sprite rightSprite;
    
    static {
        COLOR = new Color(0.8f, 0.8f, 0.8f, 1.0f);
    }
    
    public GameForeground(AssetManager assetManager) {
        
        TextureAtlas atlas = assetManager.get(ResourceNames.BACKGROUND_ATLAS);
        
        leftSprite = atlas.createSprite(ResourceNames.BACKGROUND_FOREGROUND_SIDE_LEFT_IMAGE_NAME);
        leftSprite.setBounds(LEFT_SIDE_X, 0.0f, SIDE_WIDTH, SIDE_HEIGHT);
        leftSprite.setColor(COLOR);
        
        rightSprite = atlas.createSprite(ResourceNames.BACKGROUND_FOREGROUND_SIDE_RIGHT_IMAGE_NAME);
        rightSprite.setBounds(RIGHT_SIDE_X, 0.0f, SIDE_WIDTH, SIDE_HEIGHT);
        rightSprite.setColor(COLOR);
    }
    
    public void render(SpriteBatch batch, float visibleAreaPosition) {
        leftSprite.setPosition(LEFT_SIDE_X, visibleAreaPosition);
        leftSprite.draw(batch);
        
        rightSprite.setPosition(RIGHT_SIDE_X, visibleAreaPosition);
        rightSprite.draw(batch);
    }
}
