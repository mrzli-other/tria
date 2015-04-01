package com.symbolplay.tria.game.platforms.movement;

import com.badlogic.gdx.math.Vector2;

public final class NullPlatformMovement extends PlatformMovementBase {
    
    public NullPlatformMovement(Vector2 initialPosition) {
        super(initialPosition);
    }
    
    @Override
    public boolean hasVerticalMovement() {
        return false;
    }
}
