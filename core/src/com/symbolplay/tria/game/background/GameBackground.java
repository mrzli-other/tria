package com.symbolplay.tria.game.background;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.resources.ResourceNames;

public final class GameBackground {
    
//    private static final int NUM_PLATFORM_SPRITES = 50;
//    private static final float BACKGROUND_PLATFORM_PARALLAX_FACTOR = 2.0f;
//    private static final float BACKGROUND_PLATFORM_MOVEMENT_FACTOR = 1.0f - 1.0f / BACKGROUND_PLATFORM_PARALLAX_FACTOR;
    
    private final Sprite backgroundSprite;
    
//    private final Sprite[] backgroundPlatformSprites;
//    
//    private final float backgroundPlatformOffsetXRange;
//    private final float backgroundPlatformMinYStep;
//    private final float backgroundPlatformMaxYStep;
    
    public GameBackground(AssetManager assetManager) {
        TextureAtlas backgroundAtlas = assetManager.get(ResourceNames.BACKGROUND_ATLAS);
        
        backgroundSprite = backgroundAtlas.createSprite(ResourceNames.BACKGROUND_IMAGE_NAME);
        GameUtils.multiplySpriteSize(backgroundSprite, GameContainer.PIXEL_TO_METER);
        float backgroundX = (GameContainer.GAME_AREA_WIDTH - backgroundSprite.getWidth()) / 2.0f;
        backgroundSprite.setX(backgroundX);
        
//        BackgroundPlatformSpritesData backgroundPlatformSpritesData = getBackgroundPlatformSprites(backgroundAtlas);
//        backgroundPlatformSprites = backgroundPlatformSpritesData.sprites;
//        backgroundPlatformOffsetXRange = backgroundPlatformSpritesData.offsetXRange;
//        backgroundPlatformMinYStep = backgroundPlatformSpritesData.minYStep;
//        backgroundPlatformMaxYStep = backgroundPlatformSpritesData.maxYStep;
//        
//        reset();
    }
    
    public void reset() {
//        resetPlatformObjectPositions(backgroundPlatformSprites, backgroundPlatformOffsetXRange, backgroundPlatformMinYStep, backgroundPlatformMaxYStep);
    }
    
    public void update(float delta, float visibleAreaPosition) {
    }
    
    public void render(SpriteBatch batch) {
        render(batch, 0.0f, 0.0f);
    }
    
    public void render(SpriteBatch batch, float visibleAreaPosition, float visibleAreaPositionChange) {
        backgroundSprite.setY(visibleAreaPosition);
        backgroundSprite.draw(batch);
        
//        changeObjectPositions(backgroundPlatformSprites, visibleAreaPositionChange * BACKGROUND_PLATFORM_MOVEMENT_FACTOR);
//        renderSprites(batch, backgroundPlatformSprites);
    }
    
//    public static void renderSprites(SpriteBatch batch, Sprite[] sprites) {
//        for (Sprite sprite : sprites) {
//            sprite.draw(batch);
//        }
//    }
    
    public void changeObjectPositions(float visibleAreaPositionChange) {
//        changeObjectPositions(backgroundPlatformSprites, visibleAreaPositionChange);
    }
    
//    private static void resetPlatformObjectPositions(Sprite[] sprites, float offsetXRange, float minYStep, float maxYStep) {
//        float y = 0;
//        for (Sprite sprite : sprites) {
//            float x = MathUtils.random(offsetXRange);
//            y += MathUtils.random(minYStep, maxYStep);
//            sprite.setPosition(x, y);
//        }
//    }
    
//    private static void changeObjectPositions(Sprite[] sprites, float offsetY) {
//        for (Sprite sprite : sprites) {
//            sprite.translateY(offsetY);
//        }
//    }
    
//    private static BackgroundPlatformSpritesData getBackgroundPlatformSprites(TextureAtlas backgroundAtlas) {
//        AtlasRegion gridregion = backgroundAtlas.findRegion(ResourceNames.BACKGROUND_PLATFORM_IMAGE_NAME);
//        float spriteWidth = gridregion.getRegionWidth() * GameContainer.PIXEL_TO_METER;
//        float spriteHeight = gridregion.getRegionHeight() * GameContainer.PIXEL_TO_METER;
//        
//        float offsetXRange = GameContainer.GAME_AREA_WIDTH - spriteWidth;
//        
//        Sprite[] sprites = new Sprite[NUM_PLATFORM_SPRITES];
//        for (int i = 0; i < NUM_PLATFORM_SPRITES; i++) {
//            Sprite sprite = new Sprite(gridregion);
//            sprite.setSize(spriteWidth, spriteHeight);
//            sprites[i] = sprite;
//        }
//        
//        BackgroundPlatformSpritesData spritesData = new BackgroundPlatformSpritesData();
//        spritesData.sprites = sprites;
//        spritesData.offsetXRange = offsetXRange;
//        spritesData.minYStep = spriteHeight * 2.0f;
//        spritesData.maxYStep = spritesData.minYStep * 5.0f;
//        
//        return spritesData;
//    }
    
//    private static class BackgroundPlatformSpritesData {
//        public Sprite[] sprites;
//        public float offsetXRange;
//        public float minYStep;
//        public float maxYStep;
//    }
}
