package com.symbolplay.tria.game.character.states.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.game.Sounds;
import com.symbolplay.tria.game.character.CharacterEffects;
import com.symbolplay.tria.game.items.ItemBase;
import com.symbolplay.tria.resources.ResourceNames;

public final class ItemCollisionHandler {
    
    private static final float AUTO_PICK_UP_LINE_OFFSET = 1.0f;
    
    public ItemCollisionHandler() {
    }
    
    public void handleCollisionWithItems(Vector2 position, Array<ItemBase> items, int minActiveIndex, int maxActiveIndex,
            Array<Integer> blockedItemEffects, CharacterEffects characterEffects, float visibleAreaPosition, Rectangle characterRect) {
        
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            ItemBase item = items.get(i);
            if (item.isExisting() && item.isCollision(characterRect) && !isItemEffectBlocked(item, blockedItemEffects)) {
                handleItemPickUp(item, characterEffects, visibleAreaPosition);
                item.pickUp();
                return;
            }
        }
    }
    
    public void handleCoinAutoPickup(float characterPositionY, Array<ItemBase> items, int minActiveIndex, int maxActiveIndex,
            CharacterEffects characterEffects, float visibleAreaPosition) {
        
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            ItemBase item = items.get(i);
            if (item.isExisting() && item.getEffect() == ItemBase.COIN_EFFECT && isAutoPickupCollision(characterPositionY, visibleAreaPosition, item)) {
                handleItemPickUp(item, characterEffects, visibleAreaPosition);
                return;
            }
        }
    }
    
    private static boolean isItemEffectBlocked(ItemBase item, Array<Integer> blockedItemEffects) {
        return blockedItemEffects != null && blockedItemEffects.contains(item.getEffect(), false);
    }
    
    private static boolean isAutoPickupCollision(float characterPositionY, float visibleAreaPosition, ItemBase item) {
        float autoPickupPositionY = characterPositionY + AUTO_PICK_UP_LINE_OFFSET;
        float itemY = item.getPosition().y;
        return visibleAreaPosition <= itemY && itemY <= autoPickupPositionY;
    }
    
    private void handleItemPickUp(ItemBase item, CharacterEffects characterEffects, float visibleAreaPosition) {
        int effect = item.getEffect();
        
        switch (effect) {
            case ItemBase.SHIELD_EFFECT:
                characterEffects.setShield((Float) item.getValue());
                Sounds.play(ResourceNames.SOUND_ITEM);
                break;
                
            case ItemBase.LIFE_EFFECT:
                characterEffects.addLife();
                Sounds.play(ResourceNames.SOUND_ITEM);
                break;
                
            case ItemBase.ANTI_GRAVITY_EFFECT:
                characterEffects.setAntiGravityItemDuration();
                Sounds.play(ResourceNames.SOUND_ITEM);
                break;
                
            case ItemBase.ROCKET_EFFECT:
                characterEffects.setRocketItemDuration();
                Sounds.play(ResourceNames.SOUND_ITEM);
                break;
                
            case ItemBase.COIN_EFFECT:
                characterEffects.addCoins((Integer) item.getValue());
                Sounds.play(ResourceNames.SOUND_COIN);
                break;
            
            default:
                break;
        }
        
        item.pickUp();
    }
}
