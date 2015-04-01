package com.symbolplay.tria.game.character;

public final class UpgradesUtil {
    
    private static final float ANTI_GRAVITY_BASE_DURATION = 4.0f;
    private static final float ANTI_GRAVITY_DURATION_INCREMENT = 0.7f;
    
    private static final float ROCKET_BASE_DURATION = 4.0f;
    private static final float ROCKET_DURATION_INCREMENT = 0.7f;
    
    public static float getAntiGravityDuration(int upgradeLevel) {
        return ANTI_GRAVITY_BASE_DURATION + upgradeLevel * ANTI_GRAVITY_DURATION_INCREMENT;
    }
    
    public static float getRocketDuration(int upgradeLevel) {
        return ROCKET_BASE_DURATION + upgradeLevel * ROCKET_DURATION_INCREMENT;
    }
    
    public static int getAntiGravityNextUpgradeCost(int currentUpgradeLevel) {
        switch (currentUpgradeLevel) {
            case 0:
                return 75;
            case 1:
                return 150;
            case 2:
                return 250;
            case 3:
                return 350;
            case 4:
                return 500;
            case 5:
                return 700;
            case 6:
                return 900;
            case 7:
                return 1200;
            case 8:
                return 1500;
            case 9:
                return 0;
            default:
                return 0;
        }
    }
    
    public static int getRocketNextUpgradeCost(int currentUpgradeLevel) {
        switch (currentUpgradeLevel) {
            case 0:
                return 100;
            case 1:
                return 200;
            case 2:
                return 350;
            case 3:
                return 500;
            case 4:
                return 700;
            case 5:
                return 900;
            case 6:
                return 1200;
            case 7:
                return 1500;
            case 8:
                return 2000;
            case 9:
                return 0;
            default:
                return 0;
        }
    }
    
    public static int getNextLifeUpgradeCost(int currentUpgradeLevel) {
        switch (currentUpgradeLevel) {
            case 0:
                return 200;
            case 1:
                return 300;
            case 2:
                return 400;
            case 3:
                return 0;
            default:
                return 0;
        }
    }
}
