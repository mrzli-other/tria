package com.symbolplay.tria.game.items;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.rise.RiseObject;
import com.symbolplay.tria.resources.ResourceNames;

public abstract class ItemBase implements RiseObject {
    
    public static final int SHIELD_EFFECT = 0;
    public static final int LIFE_EFFECT = 1;
    public static final int ANTI_GRAVITY_EFFECT = 2;
    public static final int ROCKET_EFFECT = 3;
    public static final int COIN_EFFECT = 4;
    public static final int NUM_EFFECTS = 5;
    
    private static final int EXISTING_STATE = 0;
    private static final int TEXT_STATE = 1;
    private static final int GONE_STATE = 2;
    
    private static final float TEXT_COUNTDOWN_DURATION = 3.0f;
    
    protected final Sprite sprite;
    private final Vector2 initialPosition;
    private final Vector2 offsetFromPlatform;
    
    protected final Vector2 position;
    protected final Vector2 size;
    protected final float radius;
    
    private int itemState;
    
    private float textCountdown;
    
    private String pickedUpText;
    private boolean isPickedUpTextBoundsDirty;
    private final Vector2 pickedUpTextBounds;
    
    public ItemBase(float x, float y, String imageName, AssetManager assetManager) {
        
        initialPosition = new Vector2(x, y);
        offsetFromPlatform = new Vector2();
        
        position = new Vector2(initialPosition);
        
        TextureAtlas atlas = assetManager.get(ResourceNames.ITEMS_ATLAS);
        sprite = atlas.createSprite(imageName);
        GameUtils.multiplySpriteSize(sprite, GameContainer.PIXEL_TO_METER);
        sprite.setOriginCenter();
        
        size = new Vector2(sprite.getWidth(), sprite.getHeight());
        radius = size.x / 2.0f;
        
        itemState = EXISTING_STATE;
        
        textCountdown = TEXT_COUNTDOWN_DURATION;
        
        pickedUpTextBounds = new Vector2();
    }
    
    public final void update(float delta) {
        if (itemState == EXISTING_STATE) {
            updateImpl(delta);
        } else if (itemState == TEXT_STATE) {
            textCountdown -= delta;
            if (textCountdown <= 0.0f) {
                textCountdown = 0.0f;
                itemState = GONE_STATE;
            }
        }
    }
    
    protected void updateImpl(float delta) {
    }
    
    public final void render(SpriteBatch batch) {
        if (itemState == EXISTING_STATE) {
            sprite.draw(batch);
        }
    }
    
    public final void renderText(SpriteBatch batch, float visibleAreaPosition, BitmapFont gameAreaFont) {
        if (itemState == TEXT_STATE) {
            if (isPickedUpTextBoundsDirty) {
                TextBounds textBounds = gameAreaFont.getBounds(pickedUpText);
                pickedUpTextBounds.set(textBounds.width, textBounds.height);
                isPickedUpTextBoundsDirty = false;
            }
            float alpha = textCountdown / TEXT_COUNTDOWN_DURATION;
            Color c = gameAreaFont.getColor();
            gameAreaFont.setColor(c.r, c.g, c.b, alpha);
            float textX = (position.x + size.x / 2.0f) * GameContainer.METER_TO_PIXEL - pickedUpTextBounds.x / 2.0f;
            textX = MathUtils.clamp(textX, 0.0f, GameContainer.VIEWPORT_WIDTH - pickedUpTextBounds.x);
            float textY = (position.y + size.y / 2.0f - visibleAreaPosition) * GameContainer.METER_TO_PIXEL + pickedUpTextBounds.y / 2.0f;
            gameAreaFont.draw(batch, pickedUpText, textX, textY);
            gameAreaFont.setColor(c);
        }
    }
    
    public void setOffsetFromPlatform(Vector2 platformInitialPosition) {
        offsetFromPlatform.set(
                initialPosition.x - platformInitialPosition.x,
                initialPosition.y - platformInitialPosition.y);
    }
    
    public final void updatePosition(Vector2 platformPosition) {
        position.set(
                platformPosition.x + offsetFromPlatform.x,
                platformPosition.y + offsetFromPlatform.y);
        
        updatePositionImpl();
    }
    
    protected abstract void updatePositionImpl();
    
    public void pickUp() {
        itemState = TEXT_STATE;
    }
    
    public boolean isCollision(Rectangle rect) {
        return false;
    }
    
    public boolean isExisting() {
        return itemState == EXISTING_STATE;
    }
    
    public abstract int getEffect();
    
    public abstract Object getValue();
    
    public void setPickedUpText(String pickedUpText) {
        this.pickedUpText = pickedUpText;
        isPickedUpTextBoundsDirty = true;
    }
    
    public Vector2 getPosition() {
        return position;
    }
    
    @Override
    public int getGroupId() {
        return -1;
    }
    
    @Override
    public float getPositionY() {
        return position.y;
    }
    
    @Override
    public void offsetPositionY(float offset) {
        initialPosition.y += offset;
        position.y += offset;
    }
}
