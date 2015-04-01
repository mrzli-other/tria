package com.symbolplay.tria.game.rise;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.resources.ResourceNames;

public final class ScoreLineObject implements RiseObject {
    
    private static final float TEXT_OFFSET_Y = 0.05f;
    private static final float TEXT_SIDE_OFFSET = 0.05f;
    
    private final Sprite sprite;
    private final BitmapFont gameAreaFont;
    
    private float y;
    
    private final String nameText;
    private final String scoreText;
    
    private final Vector2 nameTextBounds;
    // private final Vector2 scoreTextBounds;
    
    public ScoreLineObject(float y, String name, int score, boolean isCurrentUserScore, BitmapFont gameAreaFont, AssetManager assetManager) {
        
        this.gameAreaFont = gameAreaFont;
        
        this.y = y;
        
        this.nameText = isCurrentUserScore ? name + " (You)" : name;
        this.scoreText = String.valueOf(score);
        
        TextureAtlas atlas = assetManager.get(ResourceNames.BACKGROUND_ATLAS);
        sprite = atlas.createSprite(ResourceNames.BACKGROUND_SCORE_LINE_IMAGE_NAME);
        sprite.setPosition(0.0f, y);
        GameUtils.multiplySpriteSize(sprite, GameContainer.PIXEL_TO_METER);
        
        TextBounds textBounds;
        textBounds = gameAreaFont.getBounds(nameText);
        nameTextBounds = new Vector2(textBounds.width, textBounds.height);
        
        // textBounds = gameAreaFont.getBounds(scoreText);
        // scoreTextBounds = new Vector2(textBounds.width, textBounds.height);
    }
    
    public void update(float delta) {
        sprite.setPosition(0.0f, y);
    }
    
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
    
    public void renderText(SpriteBatch batch, float visibleAreaPosition) {
        float textY = (y + sprite.getHeight() + TEXT_OFFSET_Y - visibleAreaPosition) * GameContainer.METER_TO_PIXEL + nameTextBounds.y;
        float scoreTextX = TEXT_SIDE_OFFSET * GameContainer.METER_TO_PIXEL;
        float nameTextX = (GameContainer.GAME_AREA_WIDTH - TEXT_SIDE_OFFSET) * GameContainer.METER_TO_PIXEL - nameTextBounds.x;
        
        gameAreaFont.draw(batch, nameText, nameTextX, textY);
        gameAreaFont.draw(batch, scoreText, scoreTextX, textY);
    }
    
    @Override
    public int getGroupId() {
        return -1;
    }
    
    @Override
    public float getPositionY() {
        return y;
    }
    
    @Override
    public void offsetPositionY(float offset) {
        y += offset;
    }
}
