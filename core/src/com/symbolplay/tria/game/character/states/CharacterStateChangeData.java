package com.symbolplay.tria.game.character.states;

import com.badlogic.gdx.utils.ObjectMap;

public final class CharacterStateChangeData {
    
    public static final String ENEMY_TYPE_KEY = "enemytype"; 
    
    public static final int GENERAL_ENEMY_TYPE_VALUE = 0;
    public static final int SAW_ENEMY_TYPE_VALUE = 1;
    public static final int SPIKES_ENEMY_TYPE_VALUE = 2;
    
    public static final String VISIBLE_AREA_POSITION_KEY = "visibleareaposition";
    
    private static final int DATA_CAPACITY = 10;
    
    private final ObjectMap<String, Object> data;
    
    public CharacterStateChangeData() {
        data = new ObjectMap<String, Object>(DATA_CAPACITY);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getData(String key) {
        return (T) data.get(key);
    }
    
    public void setData(String key, Object value) {
        data.put(key, value);
    }
    
    public void clear() {
        data.clear();
    }
}
