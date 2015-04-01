package com.symbolplay.tria.game.character.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.resources.ResourceNames;

final class AutoPickUpLineGraphics extends CharacterGraphicsBase {
    
//    private static final float OFFSET_Y = 0.975f;
//    private static final float WIDTH = 11.25f;
//    private static final float HEIGHT = 0.05f;
    
    private static final float OFFSET_Y = 0.35f;
    private static final float WIDTH = 11.25f;
    private static final float HEIGHT = 0.675f;
    
    private final Sprite sprite;
    
    public AutoPickUpLineGraphics(AssetManager assetManager) {
        
        TextureAtlas atlas = assetManager.get(ResourceNames.CHARACTER_ATLAS);
        
        sprite = atlas.createSprite(ResourceNames.CHARACTER_AUTO_PICK_UP_LINE_IMAGE_NAME);
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
        sprite.setPosition(0.0f, characterPosition.y + OFFSET_Y);
        sprite.draw(batch);
    }
}
