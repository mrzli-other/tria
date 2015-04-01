package com.symbolplay.tria.game.character.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.resources.ResourceNames;

final class CharacterBodyGraphics extends CharacterGraphicsBase {
    
    private static final float BODY_OFFSET_X = 0.0f;
    private static final float BODY_OFFSET_Y = 0.35f;
    private static final float BODY_WIDTH = 1.0f;
    private static final float BODY_HEIGHT = 1.15f;
    
    private static final float ARM_WIDTH = 0.3f;
    private static final float ARM_HEIGHT = 0.65f;
    private static final float ARM_OFFSET_X = (BODY_WIDTH - ARM_WIDTH) / 2.0f;
    private static final float ARM_OFFSET_Y = 0.25f;
    
    private static final float ARM_ROTATION = 28.0f;
    
    private final Sprite bodySprite;
    private final Sprite armSprite;
    private boolean isLeft;
    
    public CharacterBodyGraphics(AssetManager assetManager) {
        
        TextureAtlas atlas = assetManager.get(ResourceNames.CHARACTER_ATLAS);
        
        bodySprite = atlas.createSprite(ResourceNames.CHARACTER_BODY_IMAGE_NAME);
        bodySprite.setSize(BODY_WIDTH, BODY_HEIGHT);
        
        armSprite = atlas.createSprite(ResourceNames.CHARACTER_ARM_IMAGE_NAME);
        armSprite.setSize(ARM_WIDTH, ARM_HEIGHT);
        armSprite.setOrigin(ARM_WIDTH / 2.0f, ARM_HEIGHT - ARM_WIDTH / 2.0f);
        
        isLeft = false;
        setArmRotation();
    }
    
    @Override
    public void reset() {
    }
    
    @Override
    public void update(float delta) {
    }
    
    @Override
    public void render(SpriteBatch batch, Vector2 characterPosition) {
        bodySprite.setPosition(characterPosition.x + BODY_OFFSET_X, characterPosition.y + BODY_OFFSET_Y);
        bodySprite.draw(batch);
        armSprite.setPosition(characterPosition.x + ARM_OFFSET_X, characterPosition.y + ARM_OFFSET_Y);
        armSprite.draw(batch);
    }
    
    public void setOrientation(boolean isLeft) {
        if (this.isLeft != isLeft) {
            bodySprite.flip(true, false);
            this.isLeft = isLeft;
            setArmRotation();
        }
    }
    
    private void setArmRotation() {
        armSprite.setRotation(isLeft ? -ARM_ROTATION : ARM_ROTATION);
    }
}
