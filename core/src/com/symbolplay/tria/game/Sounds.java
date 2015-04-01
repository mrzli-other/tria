package com.symbolplay.tria.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.resources.ResourceNames;

public final class Sounds {
    
    private static final int SOUNDS_CAPACITY = 20;
    
    private static final Array<String> soundNames;
    
    private static AssetManager assetManager;
    
    private static boolean isSoundOn;
    
    static {
        soundNames = new Array<String>(true, SOUNDS_CAPACITY);
        soundNames.add(ResourceNames.SOUND_JUMP);
        soundNames.add(ResourceNames.SOUND_JUMP_BOOST);
        soundNames.add(ResourceNames.SOUND_COIN);
        soundNames.add(ResourceNames.SOUND_ITEM);
        soundNames.add(ResourceNames.SOUND_OUCH);
        soundNames.add(ResourceNames.SOUND_SAW);
        soundNames.add(ResourceNames.SOUND_SPIKES);
        soundNames.add(ResourceNames.SOUND_FALL);
        soundNames.add(ResourceNames.SOUND_ROCKET);
        soundNames.add(ResourceNames.SOUND_ANTI_GRAVITY);
        
        isSoundOn = true;
    }
    
    public static void initialize(AssetManager assetManagerInput) {
        assetManager = assetManagerInput;
        
        for (String soundName : soundNames) {
            assetManager.load(soundName, Sound.class);
        }
    }
    
    public static void setSoundOn(boolean isSoundOn) {
        Sounds.isSoundOn = isSoundOn;
    }
    
    public static void play(String soundName) {
        if (isSoundOn) {
            get(soundName).play();
        }
    }
    
    public static void play(String soundName, float volume) {
        if (isSoundOn) {
            get(soundName).play(volume);
        }
    }
    
    public static void loop(String soundName) {
        if (isSoundOn) {
            get(soundName).loop();
        }
    }
    
    public static void stop(String soundName) {
        get(soundName).stop();
    }
    
    public static void pause(String soundName) {
        get(soundName).pause();
    }
    
    public static void resume(String soundName) {
        if (isSoundOn) {
            get(soundName).resume();
        } else {
            stop(soundName);
        }
    }
    
    public static void stopAll() {
        for (String soundName : soundNames) {
            stop(soundName);
        }
    }
    
    public static void pauseAll() {
        for (String soundName : soundNames) {
            pause(soundName);
        }
    }
    
    public static void resumeAll() {
        for (String soundName : soundNames) {
            resume(soundName);
        }
    }
    
    private static Sound get(String soundName) {
        return assetManager.get(soundName);
    }
}
