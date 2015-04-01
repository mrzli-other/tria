package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.enemies.EasePatrollerEnemy;
import com.symbolplay.tria.game.enemies.EnemyBase;
import com.symbolplay.tria.game.enemies.OrbiterEnemy;
import com.symbolplay.tria.game.enemies.PlatformPatrollerEnemy;
import com.symbolplay.tria.game.enemies.SawEnemy;
import com.symbolplay.tria.game.enemies.SinePatrollerEnemy;
import com.symbolplay.tria.game.enemies.StaticEnemy;
import com.symbolplay.tria.game.platforms.Platform;

final class RiseGeneratorUtils {
    
    public static final float MAX_PLATFORM_X = GameContainer.GAME_AREA_WIDTH - Platform.WIDTH;
    
    private static final float MOVEMENT_RANGE = GameContainer.GAME_AREA_WIDTH - Platform.WIDTH;
    private static final float MAX_MOVEMENT_OFFSET = MOVEMENT_RANGE * 2.0f;
    private static final float MAX_REPOSITION_OFFSET = MOVEMENT_RANGE;
    
    public static final int STANDARD_MOVEMENT_TYPE_NORMAL = 0;
    public static final int STANDARD_MOVEMENT_TYPE_MOVING = 1;
    public static final int STANDARD_MOVEMENT_TYPE_REPOSITION = 2;
    
    // POSITIONING
    public static float getRandomPlatformX() {
        return MathUtils.random(MAX_PLATFORM_X);
    }
    
    public static float getRandomPlatformX(float leftOffset, float rightOffset) {
        return MathUtils.random(MAX_PLATFORM_X - leftOffset - rightOffset) + leftOffset;
    }
    
    public static float getRandomNextPlatformX(
            float previousX,
            float minChange,
            float maxChange,
            float leftOffset,
            float rightOffset) {
        
        return getRandomNextPlatformX(previousX, minChange, maxChange, minChange, maxChange, leftOffset, rightOffset);
    }
    
    public static float getRandomNextPlatformX(
            float previousX,
            float minChangeLeft,
            float maxChangeLeft,
            float minChangeRight,
            float maxChangeRight,
            float leftOffset,
            float rightOffset) {
        
        float minX = leftOffset;
        float maxX = MAX_PLATFORM_X - rightOffset;
        
        float leftMin = Math.max(previousX - maxChangeLeft, minX);
        float leftMax = Math.max(previousX - minChangeLeft, minX);
        float leftRange = leftMax - leftMin;
        
        float rightMin = Math.min(previousX + minChangeRight, maxX);
        float rightMax = Math.min(previousX + maxChangeRight, maxX);
        float rightRange = rightMax - rightMin;
        
        float totalRange = leftRange + rightRange;
        if (totalRange > 0.0f) {
            float randomValue = MathUtils.random(totalRange);
            if (leftRange > 0.0f && rightRange > 0.0f) {
                if (randomValue <= leftRange) {
                    return leftMin + randomValue;
                } else {
                    return rightMin + (randomValue - leftRange);
                }
            } else if (leftRange > 0.0f) {
                return leftMin + randomValue;
            } else {
                return rightMin + randomValue;
            }
        } else {
            return previousX;
        }
    }
    
    public static void getPositionsX(float[] positions, boolean isThreePositions) {
        positions[0] = MathUtils.random(MAX_PLATFORM_X);
        positions[1] = getSecondPositionX(positions[0]);
        
        if (isThreePositions) {
            positions[2] = getThirdPositionX(positions[0], positions[1]);
        }
    }
    
    private static float getSecondPositionX(float firstPosition) {
        float firstPositionEnd = firstPosition + Platform.WIDTH;
        
        boolean isRange1 = Platform.WIDTH <= firstPosition;
        boolean isRange2 = Platform.WIDTH <= GameContainer.GAME_AREA_WIDTH - firstPositionEnd;
        
        float range1 = isRange1 ? firstPosition - Platform.WIDTH : 0.0f;
        float range2 = isRange2 ? GameContainer.GAME_AREA_WIDTH - firstPositionEnd - Platform.WIDTH : 0.0f;
        
        float totalRange = range1 + range2;
        float totalRangePosition = MathUtils.random(totalRange);
        
        if (isRange1 && totalRangePosition <= range1) {
            return totalRangePosition;
        } else {
            return firstPositionEnd + (totalRangePosition - range1);
        }
    }
    
    private static float getThirdPositionX(float firstPosition, float secondPosition) {
        float lowPosition = Math.min(firstPosition, secondPosition);
        float lowPositionEnd = lowPosition + Platform.WIDTH;
        
        float highPosition = Math.max(firstPosition, secondPosition);
        float highPositionEnd = highPosition + Platform.WIDTH;
        
        boolean isRange1 = Platform.WIDTH <= lowPosition;
        boolean isRange2 = Platform.WIDTH <= highPosition - lowPositionEnd;
        boolean isRange3 = Platform.WIDTH <= GameContainer.GAME_AREA_WIDTH - highPositionEnd;
        
        float range1 = isRange1 ? lowPosition - Platform.WIDTH : 0.0f;
        float range2 = isRange2 ? highPosition - lowPositionEnd - Platform.WIDTH : 0.0f;
        float range3 = isRange3 ? GameContainer.GAME_AREA_WIDTH - highPositionEnd - Platform.WIDTH : 0.0f;
        
        float totalRange = range1 + range2 + range3;
        float totalRangePosition = MathUtils.random(totalRange);
        
        if (isRange1 && totalRangePosition <= range1) {
            return totalRangePosition;
        } else if (isRange2 && totalRangePosition <= range1 + range2) {
            return lowPositionEnd + (totalRangePosition - range1);
        } else {
            return highPositionEnd + (totalRangePosition - range1 - range2);
        }
    }
    
    public static void getRandomAdjacentPositionsX(float[] positions, boolean isThreePositions) {
        float rightOffset = isThreePositions ? Platform.WIDTH * 2.0f : Platform.WIDTH;
        float leftSideX = getRandomPlatformX(0.0f, rightOffset);
        getRandomAdjacentPositionsX(leftSideX, positions, isThreePositions);
    }
    
    public static void getRandomNextAdjacentPositionsX(float previousLeftSideX, float minChange, float maxChange, float[] positions, boolean isThreePositions) {
        float rightOffset = isThreePositions ? Platform.WIDTH * 2.0f : Platform.WIDTH;
        float leftSideX = getRandomNextPlatformX(previousLeftSideX, minChange, maxChange, 0.0f, rightOffset);
        getRandomAdjacentPositionsX(leftSideX, positions, isThreePositions);
    }
    
    public static void getRandomAdjacentPositionsX(float leftSideX, float[] positions, boolean isThreePositions) {
        if (isThreePositions) {
            int permutationIndex = MathUtils.random(5);
            if (permutationIndex == 0) {
                positions[0] = leftSideX;
                positions[1] = leftSideX + Platform.WIDTH;
                positions[2] = leftSideX + Platform.WIDTH * 2.0f;
            } else if (permutationIndex == 1) {
                positions[0] = leftSideX;
                positions[2] = leftSideX + Platform.WIDTH;
                positions[1] = leftSideX + Platform.WIDTH * 2.0f;
            } else if (permutationIndex == 2) {
                positions[1] = leftSideX;
                positions[0] = leftSideX + Platform.WIDTH;
                positions[2] = leftSideX + Platform.WIDTH * 2.0f;
            } else if (permutationIndex == 3) {
                positions[1] = leftSideX;
                positions[2] = leftSideX + Platform.WIDTH;
                positions[0] = leftSideX + Platform.WIDTH * 2.0f;
            } else if (permutationIndex == 4) {
                positions[2] = leftSideX;
                positions[0] = leftSideX + Platform.WIDTH;
                positions[1] = leftSideX + Platform.WIDTH * 2.0f;
            } else {
                positions[2] = leftSideX;
                positions[1] = leftSideX + Platform.WIDTH;
                positions[0] = leftSideX + Platform.WIDTH * 2.0f;
            }
        } else {
            int permutationIndex = MathUtils.random(1);
            if (permutationIndex == 0) {
                positions[0] = leftSideX;
                positions[1] = leftSideX + Platform.WIDTH;
            } else {
                positions[1] = leftSideX;
                positions[0] = leftSideX + Platform.WIDTH;
            }
        }
    }
    // END POSITIONING
    
    // STANDARD
    public static void setHorizontalPlatformMovement(Platform platform, float minMovingSpeed, float maxMovingSpeed) {
        float speed = MathUtils.random(minMovingSpeed, maxMovingSpeed);
        float initialOffset = MathUtils.random(MAX_MOVEMENT_OFFSET);
        platform.setHorizontalMovement(MOVEMENT_RANGE, speed, initialOffset);
    }
    
    public static void setRepositionPlatformMovement(Platform platform, float minRepositionAmount, float maxRepositionAmount) {
        float initialOffset = MathUtils.random(MAX_REPOSITION_OFFSET);
        platform.setRepositionMovement(MOVEMENT_RANGE, minRepositionAmount, maxRepositionAmount, initialOffset);
    }
    
    public static int getStandardRandomPlatformType(float normalWeight, float movingWeight, float repositionWeight) {
        float totalWeight = normalWeight + movingWeight + repositionWeight;
        float randomValue = MathUtils.random(totalWeight);
        if (normalWeight > 0.0f && randomValue <= normalWeight) {
            return STANDARD_MOVEMENT_TYPE_NORMAL;
        } else if (movingWeight >= 0.0f && randomValue <= normalWeight + movingWeight) {
            return STANDARD_MOVEMENT_TYPE_MOVING;
        } else if (repositionWeight >= 0.0f) {
            return STANDARD_MOVEMENT_TYPE_REPOSITION;
        } else {
            return STANDARD_MOVEMENT_TYPE_NORMAL;
        }
    }
    
    public static float getStandardMovingSectionWeight(float absoluteHeight) {
        return 1.0f;
    }
    
    public static float getStandardRepositionSectionWeight(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 1.0f;
        } else if (absoluteHeight <= 3000.0f) {
            return 1.0f - (absoluteHeight - 1000.0f) * 0.0004f;
        } else if (absoluteHeight <= 5000.0f) {
            return 0.2f - (absoluteHeight - 3000.0f) * 0.00005f;
        } else {
            return 0.1f;
        }
    }
    
    public static float getStandardMixedSectionWeight(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 1.0f;
        } else if (absoluteHeight <= 5000.0f) {
            return 1.0f - (absoluteHeight - 1000.0f) * 0.000225f;
        } else {
            return 0.1f;
        }
    }
    
    public static float getStandardMinStepY(float absoluteHeight) {
//        if (absoluteHeight <= 100.0f) {
//            return 1.0f;
//        } else if (absoluteHeight <= 160.0f) {
//            return 1.0f + (absoluteHeight - 100.0f) * 0.01f;
//        } else if (absoluteHeight <= 760.0f) {
//            return 1.6f + (absoluteHeight - 160.0f) * 0.001f;
//        } else {
//            return 2.2f;
//        }
        
        return 1.0f;
    }
    
    public static float getStandardMaxStepY(float absoluteHeight) {
        if (absoluteHeight <= 100.0f) {
            return 1.0f;
        } else if (absoluteHeight <= 200.0f) {
            return 1.0f + (absoluteHeight - 100.0f) * 0.015f;
        } else if (absoluteHeight <= 1450.0f) {
            return 2.5f + (absoluteHeight - 200.0f) * 0.002f;
        } else {
            return 5.0f;
        }
    }
    
    public static float getStandardMinMovingSpeed(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 1.0f + absoluteHeight * 0.0045f;
        } else if (absoluteHeight <= 3000.0f) {
            return 5.5f + (absoluteHeight - 1000.0f) * 0.00125f;
        } else {
            return 8.0f;
        }
    }
    
    public static float getStandardMaxMovingSpeed(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 1.5f + absoluteHeight * 0.005f;
        } else if (absoluteHeight <= 3000.0f) {
            return 6.5f + (absoluteHeight - 1000.0f) * 0.00175f;
        } else {
            return 10.0f;
        }
    }
    
    public static float getStandardMinRepositionAmount(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 1.0f + absoluteHeight * 0.0025f;
        } else {
            return 3.5f;
        }
    }
    
    public static float getStandardMaxRepositionAmount(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 1.5f + absoluteHeight * 0.0045f;
        } else if (absoluteHeight <= 3000.0f) {
            return 6.0f + (absoluteHeight - 1000.0f) * 0.0015f;
        } else {
            return 9.0f;
        }
    }
    // END STANDARD
    
    // PLATFORMS
    public static Platform addPlatform(RiseGeneratorData rgd, float x, float y, AssetManager assetManager) {
        Platform platform = new Platform(rgd.nextPlatformId, rgd.nextGroupId, x, y + rgd.relativeHeight, assetManager);
        rgd.platforms.add(platform);
        
        rgd.nextPlatformId++;
        rgd.nextGroupId++;
        
        return platform;
    }
    
    public static Platform getStandardPlatform(
            RiseGeneratorData rgd,
            float y,
            int platformType,
            float minMovingSpeed,
            float maxMovingSpeed,
            float minRepositionAmount,
            float maxRepositionAmount,
            AssetManager assetManager) {
        
        float x;
        
        if (platformType == RiseGeneratorUtils.STANDARD_MOVEMENT_TYPE_NORMAL) {
            x = RiseGeneratorUtils.getRandomPlatformX();
        } else {
            x = 0.0f;
        }
        
        Platform platform = new Platform(rgd.nextPlatformId, rgd.nextGroupId, x, y + rgd.relativeHeight, assetManager);
        
        if (platformType == RiseGeneratorUtils.STANDARD_MOVEMENT_TYPE_MOVING) {
            RiseGeneratorUtils.setHorizontalPlatformMovement(platform, minMovingSpeed, maxMovingSpeed);
        } else if (platformType == RiseGeneratorUtils.STANDARD_MOVEMENT_TYPE_REPOSITION) {
            RiseGeneratorUtils.setRepositionPlatformMovement(platform, minRepositionAmount, maxRepositionAmount);
        }
        
        return platform;
    }
    // END PLATFORMS
    
    // ENEMIES
    public static void addStaticEnemy(RiseGeneratorData rgd, float x, float y, AssetManager assetManager) {
        EnemyBase enemy = new StaticEnemy(x, y + rgd.relativeHeight, assetManager);
        rgd.enemies.add(enemy);
    }
    
    public static void addSinePatrollerEnemy(RiseGeneratorData rgd, float x, float y, AssetManager assetManager) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float speed = getDefaultSinePatrollerSpeed(absoluteHeight);
        addSinePatrollerEnemy(rgd, x, y, speed, assetManager);
    }
    
    public static void addSinePatrollerEnemy(RiseGeneratorData rgd, float x, float y, float speed, AssetManager assetManager) {
        float range = GameContainer.GAME_AREA_WIDTH - SinePatrollerEnemy.WIDTH;
        float initialOffset = MathUtils.random(range * 2.0f);
        EnemyBase enemy = new SinePatrollerEnemy(x, y + rgd.relativeHeight, range, speed, initialOffset, assetManager);
        rgd.enemies.add(enemy);
    }
    
    public static float getDefaultSinePatrollerSpeed(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 3.0f + absoluteHeight * 0.003f;
        } else if (absoluteHeight <= 3000.0f) {
            return 6.0f + (absoluteHeight - 1000.0f) * 0.001f;
        } else {
            return 8.0f;
        }
    }
    
    public static void addEasePatrollerEnemy(RiseGeneratorData rgd, float x, float y, AssetManager assetManager) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float period = getEasePatrollerPeriod(absoluteHeight);
        addEasePatrollerEnemy(rgd, x, y, period, assetManager);
    }
    
    public static void addEasePatrollerEnemy(RiseGeneratorData rgd, float x, float y, float period, AssetManager assetManager) {
        float range = GameContainer.GAME_AREA_WIDTH - EasePatrollerEnemy.WIDTH;
        float startTimeOffset = MathUtils.random(period);
        
        EnemyBase enemy = new EasePatrollerEnemy(x, y + rgd.relativeHeight, range, period, startTimeOffset, assetManager);
        rgd.enemies.add(enemy);
    }
    
    public static float getEasePatrollerPeriod(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 7.6f - absoluteHeight * 0.002f;
        } else if (absoluteHeight <= 3000.0f) {
            return 5.6f - (absoluteHeight - 1000.0f) * 0.001f;
        } else {
            return 3.6f;
        }
    }
    
    public static void addPlatformPatrollerEnemy(RiseGeneratorData rgd, float x, float y, float range, AssetManager assetManager) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float speed = getPlatformPatrollerSpeed(absoluteHeight);
        addPlatformPatrollerEnemy(rgd, x, y, range, speed, assetManager);
    }
    
    public static void addPlatformPatrollerEnemy(RiseGeneratorData rgd, float x, float y, float range, float speed, AssetManager assetManager) {
        float initialOffset = MathUtils.random(range * 2.0f);
        EnemyBase enemy = new PlatformPatrollerEnemy(x, y + rgd.relativeHeight, range, speed, initialOffset, assetManager);
        rgd.enemies.add(enemy);
    }
    
    public static float getPlatformPatrollerSpeed(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 5.0f + absoluteHeight * 0.002f;
        } else if (absoluteHeight <= 3000.0f) {
            return 7.0f + (absoluteHeight - 1000.0f) * 0.0015f;
        } else {
            return 10.0f;
        }
    }
    
    public static void addOrbiterEnemy(RiseGeneratorData rgd, float x, float y, AssetManager assetManager) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float speed = getOrbiterSpeed(absoluteHeight);
        addOrbiterEnemy(rgd, x, y, speed, assetManager);
    }
    
    public static void addOrbiterEnemy(RiseGeneratorData rgd, float x, float y, float speed, AssetManager assetManager) {
        float maxOffset = (GameContainer.GAME_AREA_WIDTH - OrbiterEnemy.WIDTH) * MathUtils.PI;
        float initialOffset = MathUtils.random(maxOffset);
        
        EnemyBase enemy = new OrbiterEnemy(x, y + rgd.relativeHeight, speed, initialOffset, assetManager);
        rgd.enemies.add(enemy);
    }
    
    public static float getOrbiterSpeed(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 5.5f + absoluteHeight * 0.003f;
        } else if (absoluteHeight <= 3000.0f) {
            return 8.5f + (absoluteHeight - 1000.0f) * 0.0015f;
        } else {
            return 11.5f;
        }
    }
    
    public static void addSawEnemy(RiseGeneratorData rgd, float x, float y, AssetManager assetManager) {
        EnemyBase enemy = new SawEnemy(x, y + rgd.relativeHeight, assetManager);
        rgd.enemies.add(enemy);
    }
    // END ENEMIES
}
