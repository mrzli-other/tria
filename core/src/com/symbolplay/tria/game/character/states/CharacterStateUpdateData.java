package com.symbolplay.tria.game.character.states;

import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.game.PlatformToCharCollisionData;
import com.symbolplay.tria.game.enemies.EnemyBase;
import com.symbolplay.tria.game.items.ItemBase;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.game.rise.RiseActiveData;

public final class CharacterStateUpdateData {
    
    public float horizontalSpeed;
    public PlatformToCharCollisionData platformToCharCollisionData;
    public Array<Platform> platforms;
    public Array<EnemyBase> enemies;
    public Array<ItemBase> items;
    public RiseActiveData riseActiveData;
    public float visibleAreaPosition;
    public float delta;
}
