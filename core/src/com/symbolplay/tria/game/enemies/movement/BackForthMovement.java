package com.symbolplay.tria.game.enemies.movement;

import com.symbolplay.gamelibrary.util.GameUtils;

public final class BackForthMovement {
    
    private final float singleDirectionLength;
    private final float bothDirectionLength;
    
    private float mTotalTravelled;
    private float mOffset;
    private boolean mIsInitialDirection;
    
    public BackForthMovement(float singleDirectionLength) {
        this.singleDirectionLength = singleDirectionLength;
        bothDirectionLength = this.singleDirectionLength * 2.0f;
    }
    
    public void update(float travelled) {
        mTotalTravelled = GameUtils.getPositiveModulus(mTotalTravelled + travelled, bothDirectionLength);
        mIsInitialDirection = mTotalTravelled <= singleDirectionLength;
        mOffset = mIsInitialDirection ? mTotalTravelled : bothDirectionLength - mTotalTravelled; 
    }
    
    public float getOffset() {
        return mOffset;
    }
    
    public boolean isInitialDirection() {
        return mIsInitialDirection;
    }
}
