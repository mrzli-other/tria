package com.symbolplay.tria.game;

public final class CollisionEffects {
    
    public static final int JUMP_BOOST = 0;
    public static final int REPOSITION_PLATFORMS = 1;
    public static final int VISIBLE_ON_JUMP = 2;
    public static final int IMPALE_ATTACHED_SPIKES = 3;
    public static final int REVEAL_ON_JUMP = 4;
    public static final int IMPALE_SPIKES = 5;
    public static final int TOGGLE_SPIKES = 6;
    private static final int NUM_EFFECTS = 7;
    
    private final boolean effects[];
    private final Object effectData[];
    
    private final JumpBoostCollisionEffectData jumpBoostCollisionEffectData;
    private final RevealOnJumpCollisionEffectData revealOnJumpCollisionEffectData;
    
    public CollisionEffects() {
        effects = new boolean[NUM_EFFECTS];
        effectData = new Object[NUM_EFFECTS];
        
        jumpBoostCollisionEffectData = new JumpBoostCollisionEffectData();
        revealOnJumpCollisionEffectData = new RevealOnJumpCollisionEffectData();
    }
    
    public void clear() {
        for (int i = 0; i < NUM_EFFECTS; i++) {
            effects[i] = false;
        }
    }
    
    public void set(int effect) {
        set(effect, 0.0f);
    }
    
    public void set(int effect, Object data) {
        effects[effect] = true;
        effectData[effect] = data;
    }
    
    public void setJumpBoostEffect(float speed, float soundVolume) {
        effects[JUMP_BOOST] = true;
        
        jumpBoostCollisionEffectData.jumpBoostSpeed = speed;
        jumpBoostCollisionEffectData.soundVolume = soundVolume;
        effectData[JUMP_BOOST] = jumpBoostCollisionEffectData;
    }
    
    public void setRevealOnJumpEffect(int[] revealOnJumpIds) {
        effects[REVEAL_ON_JUMP] = true;
        
        revealOnJumpCollisionEffectData.revealOnJumpIds = revealOnJumpIds;
        effectData[REVEAL_ON_JUMP] = revealOnJumpCollisionEffectData;
    }
    
    public boolean isEffectActive(int effect) {
        return effects[effect];
    }
    
    public Object getEffectData(int effect) {
        return effectData[effect];
    }
}
