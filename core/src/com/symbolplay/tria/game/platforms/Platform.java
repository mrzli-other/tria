package com.symbolplay.tria.game.platforms;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.gamelibrary.util.Pools;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.CollisionEffects;
import com.symbolplay.tria.game.PlatformToCharCollisionData;
import com.symbolplay.tria.game.character.GameCharacter;
import com.symbolplay.tria.game.items.ItemBase;
import com.symbolplay.tria.game.platforms.features.AttachedSpikesPlatformFeature;
import com.symbolplay.tria.game.platforms.features.CrumblePlatformFeature;
import com.symbolplay.tria.game.platforms.features.JumpBoostPlatformFeature;
import com.symbolplay.tria.game.platforms.features.RevealOnJumpPlatformFeature;
import com.symbolplay.tria.game.platforms.features.SpikesPlatformFeature;
import com.symbolplay.tria.game.platforms.features.TimedSpikesPlatformFeature;
import com.symbolplay.tria.game.platforms.features.ToggleSpikesPlatformFeature;
import com.symbolplay.tria.game.platforms.features.VisibleOnJumpPlatformFeature;
import com.symbolplay.tria.game.platforms.movement.HorizontalPlatformMovement;
import com.symbolplay.tria.game.platforms.movement.NullPlatformMovement;
import com.symbolplay.tria.game.platforms.movement.PlatformMovementBase;
import com.symbolplay.tria.game.platforms.movement.RepositionPlatformMovement;
import com.symbolplay.tria.game.rise.RiseObject;
import com.symbolplay.tria.resources.ResourceNames;

public final class Platform implements RiseObject {
    
    public static final float WIDTH = 2.0f;
    public static final float HEIGHT = 0.5f;
    
    private static final int ATTACHED_ITEMS_INITIAL_CAPACITY = 2;
    
    private final int platformId;
    private final int groupId;
    
    private final Vector2 initialPosition;
    
    protected final Sprite sprite;
    
    private final PlatformModifier platformModifier;
    
    private PlatformMovementBase platformMovement;
    
    private JumpBoostPlatformFeature jumpBoostFeature;
    private CrumblePlatformFeature crumbleFeature;
    private VisibleOnJumpPlatformFeature visibleOnJumpFeature;
    private RevealOnJumpPlatformFeature revealOnJumpFeature;
    private ToggleSpikesPlatformFeature toggleSpikesFeature;
    private TimedSpikesPlatformFeature timedSpikesFeature;
    private AttachedSpikesPlatformFeature attachedSpikesFeature;
    private SpikesPlatformFeature spikesFeature;
    
    private final Array<ItemBase> attachedItems;
    
    private final AssetManager assetManager;
    
    public Platform(int id, int groupId, float x, float y, AssetManager assetManager) {
        
        this.assetManager = assetManager;
        
        this.platformId = id;
        this.groupId = groupId;
        
        initialPosition = new Vector2(x, y);
        
        TextureAtlas atlas = assetManager.get(ResourceNames.PLATFORMS_ATLAS);
        sprite = atlas.createSprite(ResourceNames.PLATFORM_NORMAL_IMAGE_NAME);
        sprite.setBounds(initialPosition.x, initialPosition.y, WIDTH, HEIGHT);
        
        platformModifier = new PlatformModifier();
        platformMovement = new NullPlatformMovement(initialPosition);
        
        attachedItems = new Array<ItemBase>(ATTACHED_ITEMS_INITIAL_CAPACITY);
    }
    
    public final void update(float delta, Vector2 c1, Vector2 c2, PlatformToCharCollisionData collisionData) {
        // if platform can move up, additional platform to char collision must be checked
        if (platformMovement.hasVerticalMovement() && collisionData.isEnabled) {
            Vector2 position = getPosition();
            Vector2 p1 = Pools.obtainVector();
            p1.set(position.x, position.y + HEIGHT);
            
            updateImpl(delta, c1, c2, collisionData);
            
            position = getPosition();
            Vector2 p2 = Pools.obtainVector();
            p2.set(position.x, position.y + HEIGHT);
            
            // only check for collision when platform is going up, and character is going down
            if (p2.y > p1.y) {
                collisionData.isCollision = Intersector.intersectSegments(
                        c1, c2, p1, p2, collisionData.collisionPoint);
                if (collisionData.isCollision) {
                    collisionData.collisionPlatform = this;
                    collisionData.collisionPoint.y = p2.y;
                }
            }
            
            Pools.freeVector(p1);
            Pools.freeVector(p2);
        } else {
            updateImpl(delta, c1, c2, collisionData);
        }
    }
    
    protected void updateImpl(float delta, Vector2 c1, Vector2 c2, PlatformToCharCollisionData collisionData) {
        
        if (platformModifier.isMoving) {
            platformMovement.update(delta);
            Vector2 position = getPosition();
            for (ItemBase item : attachedItems) {
                item.updatePosition(position);
            }
        }
        
        updateFeatures(delta);
        updatePlatformModifier();
    }
    
    private void updateFeatures(float delta) {
        
        Vector2 position = getPosition();
        
        if (jumpBoostFeature != null) {
            jumpBoostFeature.update(delta, position);
        }
        
        if (crumbleFeature != null) {
            crumbleFeature.update(delta, position);
        }
        
        if (visibleOnJumpFeature != null) {
            visibleOnJumpFeature.update(delta, position);
        }
        
        if (revealOnJumpFeature != null) {
            revealOnJumpFeature.update(delta, position);
        }
        
        if (toggleSpikesFeature != null) {
            toggleSpikesFeature.update(delta, position);
        }
        
        if (timedSpikesFeature != null) {
            timedSpikesFeature.update(delta, position);
        }
        
        if (attachedSpikesFeature != null) {
            attachedSpikesFeature.update(delta, position);
        }
    }
    
    private  void updatePlatformModifier() {
        platformModifier.reset();
        
        platformMovement.updatePlatformModifier(platformModifier);
        
        if (crumbleFeature != null) {
            crumbleFeature.updatePlatformModifier(platformModifier);
        }
        
        if (visibleOnJumpFeature != null) {
            visibleOnJumpFeature.updatePlatformModifier(platformModifier);
        }
        
        if (revealOnJumpFeature != null) {
            revealOnJumpFeature.updatePlatformModifier(platformModifier);
        }
    }
    
    public final void render(SpriteBatch batch) {
        if (!isVisible()) {
            return;
        }
        
        Vector2 position = getPosition();
        
        renderBackgroundFeatures(batch);
        
        sprite.setPosition(position.x, position.y);
        
        if (platformModifier.textureRegion != null) {
            sprite.setRegion(platformModifier.textureRegion);
        }
        sprite.setColor(platformModifier.platformColor);
        sprite.draw(batch);
        
        platformMovement.render(batch, platformModifier.platformColor.a);
        renderForegroundFeatures(batch);
    }
    
    private void renderBackgroundFeatures(SpriteBatch batch) {
        Vector2 position = getPosition();
        Color color = platformModifier.featuresColor;
        
        if (attachedSpikesFeature != null) {
            attachedSpikesFeature.renderBackground(batch, position, color);
        }
        
        if (spikesFeature != null) {
            spikesFeature.renderBackground(batch, position, color);
        }
        
        if (toggleSpikesFeature != null) {
            toggleSpikesFeature.renderBackground(batch, position, color);
        }
        
        if (timedSpikesFeature != null) {
            timedSpikesFeature.renderBackground(batch, position, color);
        }
        
        if (jumpBoostFeature != null) {
            jumpBoostFeature.renderBackground(batch, position, color);
        }
    }
    
    private void renderForegroundFeatures(SpriteBatch batch) {
        Vector2 position = getPosition();
        Color color = platformModifier.featuresColor;
        
        if (toggleSpikesFeature != null) {
            toggleSpikesFeature.renderForeground(batch, position, color);
        }
        
        if (timedSpikesFeature != null) {
            timedSpikesFeature.renderForeground(batch, position, color);
        }
        
        if (visibleOnJumpFeature != null) {
            visibleOnJumpFeature.renderForeground(batch, position, color);
        }
        
        if (revealOnJumpFeature != null) {
            revealOnJumpFeature.renderForeground(batch, position, color);
        }
    }
    
    public void offsetY(float offset) {
        Vector2 position = getPosition();
        position.y += offset;
    }
    
    public boolean isCollision(Vector2 c1, Vector2 c2, Vector2 intersection) {
        if (!isCollidable()) {
            return false;
        }
        
        Vector2 position = getPosition();
        Vector2 p1 = Pools.obtainVector();
        Vector2 p2 = Pools.obtainVector();
        
        float pY = position.y + HEIGHT;
        p1.set(position.x - GameCharacter.COLLISION_LINE_LENGTH, pY);
        p2.set(position.x + WIDTH - GameCharacter.COLLISION_WIDTH_OFFSET, pY);
        
        boolean isIntersection = Intersector.intersectSegments(c1, c2, p1, p2, intersection);
        
        Pools.freeVector(p1);
        Pools.freeVector(p2);
        
        return isIntersection;
    }
    
    public boolean isVisible() {
        return platformModifier.isVisible;
    }
    
    public boolean isActive(float visibleAreaPosition, float visiblePlatformsAreaPadding) {
        if (!platformModifier.isActive) {
            return false;
        }
        
        Vector2 position = getPosition();
        float activeRangeLower = visibleAreaPosition - HEIGHT - visiblePlatformsAreaPadding;
        float activeRangeUpper = visibleAreaPosition + GameContainer.GAME_AREA_HEIGHT + visiblePlatformsAreaPadding;
        return position.y >= activeRangeLower && position.y <= activeRangeUpper;
    }
    
    public boolean isCollidable() {
        return platformModifier.isCollidable;
    }
    
    public void applyEffects(CollisionEffects collisionEffects) {
        platformMovement.applyEffects(collisionEffects);
        
        if (visibleOnJumpFeature != null) {
            visibleOnJumpFeature.applyEffects(collisionEffects);
        }
        
        if (revealOnJumpFeature != null) {
            revealOnJumpFeature.applyEffects(collisionEffects);
        }
        
        if (toggleSpikesFeature != null) {
            toggleSpikesFeature.applyEffects(collisionEffects);
        }
    }
    
    // SET MOVEMENT
    public Platform setNoMovement() {
        platformMovement = new NullPlatformMovement(initialPosition);
        return this;
    }
    
    public Platform setHorizontalMovement(float range, float speed, float initialOffset) {
        platformMovement = new HorizontalPlatformMovement(initialPosition, range, speed, initialOffset, assetManager);
        return this;
    }
    
    public Platform setRepositionMovement(float range, float minRepositionAmount, float maxRepositionAmount, float initialOffset) {
        platformMovement = new RepositionPlatformMovement(initialPosition, range, minRepositionAmount, maxRepositionAmount, initialOffset, assetManager);
        return this;
    }
    // END SET MOVEMENT
    
    // SET FEATURES
    public Platform setJumpBoostFeature(float positionFraction, int jumpBoostSize) {
        jumpBoostFeature = new JumpBoostPlatformFeature(this, positionFraction, jumpBoostSize, assetManager);
        return this;
    }
    
    public Platform setCrumbleFeature() {
        crumbleFeature = new CrumblePlatformFeature(this, assetManager);
        return this;
    }
    
    public Platform setVisibleOnJumpFeature() {
        visibleOnJumpFeature = new VisibleOnJumpPlatformFeature(this, assetManager);
        return this;
    }
    
    public Platform setRevealOnJumpFeature(boolean isInitiallyVisible, int[] revealIds) {
        revealOnJumpFeature = new RevealOnJumpPlatformFeature(this, isInitiallyVisible, revealIds, assetManager);
        return this;
    }
    
    public Platform setToggleSpikesFeature(int[] states) {
        toggleSpikesFeature = new ToggleSpikesPlatformFeature(this, states, assetManager);
        return this;
    }
    
    public Platform setTimedSpikesFeature(float cycleOffset, float activeDuration, float inactiveDuration) {
        timedSpikesFeature = new TimedSpikesPlatformFeature(this, cycleOffset, activeDuration, inactiveDuration, assetManager);
        return this;
    }
    
    public Platform setAttachedSpikesFeature(boolean isBottom, boolean isLeft, boolean isRight) {
        attachedSpikesFeature = new AttachedSpikesPlatformFeature(this, isBottom, isLeft, isRight, assetManager);
        return this;
    }
    
    public Platform setSpikesFeature() {
        spikesFeature = new SpikesPlatformFeature(this, assetManager);
        return this;
    }
    // END SET FEATURES
    
    public void attachItem(ItemBase item) {
        item.setOffsetFromPlatform(initialPosition);
        attachedItems.add(item);
    }
    
    public void applyContactToCollisionEffects(float collisionPointX, CollisionEffects collisionEffects) {
        platformMovement.applyContact(collisionEffects);
        
        Vector2 position = getPosition();
        float relativeCollisionPointX = collisionPointX - position.x;
        
        if (jumpBoostFeature != null && jumpBoostFeature.isContact(relativeCollisionPointX)) {
            jumpBoostFeature.applyContact(collisionEffects);
        }
        
        if (crumbleFeature != null && crumbleFeature.isContact(relativeCollisionPointX)) {
            crumbleFeature.applyContact(collisionEffects);
        }
        
        if (revealOnJumpFeature != null && revealOnJumpFeature.isContact(relativeCollisionPointX)) {
            revealOnJumpFeature.applyContact(collisionEffects);
        }
        
        if (toggleSpikesFeature != null && toggleSpikesFeature.isContact(relativeCollisionPointX)) {
            toggleSpikesFeature.applyContact(collisionEffects);
        }
        
        if (timedSpikesFeature != null && timedSpikesFeature.isContact(relativeCollisionPointX)) {
            timedSpikesFeature.applyContact(collisionEffects);
        }
        
        if (spikesFeature != null && spikesFeature.isContact(relativeCollisionPointX)) {
            spikesFeature.applyContact(collisionEffects);
        }
        
        collisionEffects.set(CollisionEffects.VISIBLE_ON_JUMP);
    }
    
    public void applyFeaturesCollisionEffects(Rectangle characterRect, Vector2 characterSpeed, CollisionEffects collisionEffects) {
        if (attachedSpikesFeature != null && attachedSpikesFeature.isSpecialCollision(characterRect, characterSpeed)) {
            attachedSpikesFeature.applySpecialCollision(collisionEffects);
        }
    }
    
    public int getPlatformId() {
        return platformId;
    }
    
    public Vector2 getPosition() {
        return platformMovement.getPosition();
    }
    
    public Vector2 getInitialPosition() {
        return initialPosition;
    }
    
    @Override
    public int getGroupId() {
        return groupId;
    }
    
    @Override
    public float getPositionY() {
        return getPosition().y;
    }
    
    @Override
    public void offsetPositionY(float offset) {
        initialPosition.y += offset;
        platformMovement.offsetPositionY(offset);
    }
}
