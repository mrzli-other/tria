package com.symbolplay.tria.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public final class DebugData {
    
    private int fps;
    private int renderCalls;
    private int maxSpritesInBatch;
    
    public DebugData() {
    }
    
    public void initializeFrameData() {
        fps = Gdx.graphics.getFramesPerSecond();
        renderCalls = 0;
        maxSpritesInBatch = 0;
    }
    
    public void updateForBatch(SpriteBatch batch) {
        renderCalls += batch.renderCalls;
        maxSpritesInBatch = Math.max(batch.maxSpritesInBatch, maxSpritesInBatch);
        batch.maxSpritesInBatch = 0;
    }
    
    @Override
    public String toString() {
        return String.format("Fps: %d; Calls: %d; Max Batch: %d", fps, renderCalls, maxSpritesInBatch);
    }
}
