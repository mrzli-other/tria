package com.symbolplay.tria.game.platforms;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class PlatformModifier {
    
    private static final float DEFAULT_COLOR_VALUE = 1.0f;
    private static final Color DEFAULT_COLOR;
    
    public boolean isVisible;
    public boolean isActive;
    public boolean isMoving;
    public boolean isCollidable;
    public TextureRegion textureRegion;
    public final Color platformColor;
    public final Color featuresColor;
    
    static {
        DEFAULT_COLOR = new Color(DEFAULT_COLOR_VALUE, DEFAULT_COLOR_VALUE, DEFAULT_COLOR_VALUE, 1.0f);
    }
    
    public PlatformModifier() {
        platformColor = new Color();
        featuresColor = new Color();
        reset();
    }
    
    public void reset() {
        isVisible = true;
        isActive = true;
        isMoving = true;
        isCollidable = true;
        textureRegion = null;
        platformColor.set(DEFAULT_COLOR);
        featuresColor.set(DEFAULT_COLOR);
    }
}
