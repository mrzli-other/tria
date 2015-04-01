package com.symbolplay.tria.game;

import com.badlogic.gdx.Gdx;

public final class Vibration {
    
    public static boolean isVibrationOn;
    
    static {
        isVibrationOn = true;
    }
    
    public static void setVibrationOn(boolean isVibrationOn) {
        Vibration.isVibrationOn = isVibrationOn;
    }
    
    public static void vibrate() {
        if (isVibrationOn) {
            Gdx.input.vibrate(300);
        }
    }
}
