package com.symbolplay.tria.game.enemies.movement;

import com.symbolplay.gamelibrary.util.GameUtils;

public final class LoopMovement {
    
    private final float trajectoryLength;
    
    private float mTotalTravelled;
    
    public LoopMovement(float trajectoryLength) {
        this.trajectoryLength = trajectoryLength;
    }
    
    public void update(float travelled) {
        mTotalTravelled = GameUtils.getPositiveModulus(mTotalTravelled + travelled, trajectoryLength);
    }
    
    public float getOffset() {
        return mTotalTravelled;
    }
}
