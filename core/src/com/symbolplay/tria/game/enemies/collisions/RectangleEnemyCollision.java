package com.symbolplay.tria.game.enemies.collisions;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

final class RectangleEnemyCollision extends EnemyCollisionBase {
    
    private final Rectangle collisionRect;
    private final float collisionPadding;
    
    public RectangleEnemyCollision(Sprite sprite, float collisionPadding) {
        this.collisionPadding = collisionPadding;
        
        float x = sprite.getX() + this.collisionPadding;
        float y = sprite.getY() + this.collisionPadding;
        float width = sprite.getWidth() - 2.0f * this.collisionPadding;
        float height = sprite.getHeight() - 2.0f * this.collisionPadding;
        collisionRect = new Rectangle(x, y, width, height);
    }
    
    @Override
    public void update(Sprite sprite) {
        collisionRect.setX(sprite.getX() + collisionPadding);
        collisionRect.setY(sprite.getY() + collisionPadding);
    }
    
    @Override
    public boolean isCollision(Rectangle characterRect) {
        return Intersector.overlaps(collisionRect, characterRect);
    }
}
