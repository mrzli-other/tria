package com.symbolplay.tria.game.enemies.collisions;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

final class CircleEnemyCollision extends EnemyCollisionBase {
    
    private final Circle collisionCircle;
    private final float collisionPadding;
    
    public CircleEnemyCollision(Sprite sprite, float collisionPadding) {
        this.collisionPadding = collisionPadding;
        
        float x = sprite.getX() + sprite.getWidth() / 2.0f;
        float y = sprite.getY() + sprite.getHeight() / 2.0f;
        float radius = sprite.getWidth() / 2.0f - this.collisionPadding;
        collisionCircle = new Circle(x, y, radius);
    }
    
    @Override
    public void update(Sprite sprite) {
        collisionCircle.x = sprite.getX() + sprite.getWidth() / 2.0f;
        collisionCircle.y = sprite.getY() + sprite.getHeight() / 2.0f;
    }
    
    @Override
    public boolean isCollision(Rectangle characterRect) {
        return Intersector.overlaps(collisionCircle, characterRect);
    }
}
