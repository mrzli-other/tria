package com.symbolplay.tria.game.character.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.resources.ResourceNames;

final class CharacterLegsGraphics extends CharacterGraphicsBase {
    
    private static final float OFFSET_X = 0.25f;
    private static final float OFFSET_Y = 0.0f;
    private static final float WIDTH = 0.5f;
    private static final float HEIGHT = 0.4f;
    
    private final Sprite sprite;
    
    public CharacterLegsGraphics(AssetManager assetManager) {
        
        TextureAtlas atlas = assetManager.get(ResourceNames.CHARACTER_ATLAS);
        sprite = atlas.createSprite(ResourceNames.CHARACTER_LEGS_IMAGE_NAME);
        sprite.setSize(WIDTH, HEIGHT);
    }
    
    @Override
    public void reset() {
    }
    
    @Override
    public void update(float delta) {
    }
    
    @Override
    public void render(SpriteBatch batch, Vector2 characterPosition) {
        sprite.setPosition(characterPosition.x + OFFSET_X, characterPosition.y + OFFSET_Y);
        sprite.draw(batch);
    }
}
