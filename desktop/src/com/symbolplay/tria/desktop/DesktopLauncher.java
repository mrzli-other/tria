package com.symbolplay.tria.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.symbolplay.tria.GameContainer;

public class DesktopLauncher {
    
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Tria";
        config.width = 600;
        config.height = 800;
        config.resizable = true;
        
        boolean isPackTextures = arg.length > 0 && Boolean.valueOf(arg[0]);
        if (isPackTextures) {
            packTextures();
            System.out.println("FINSIHED PACKING TEXTURES...");
        } else {
            PlatformSpecificDesktop platformSpecificDesktop = new PlatformSpecificDesktop();
            new LwjglApplication(new GameContainer(platformSpecificDesktop, 0), config);
        }
    }
    
    private static void packTextures() {
        Settings settings = new Settings();
        settings.pot = false;
        settings.maxWidth = 1024;
        settings.maxHeight = 1024;
        settings.filterMin = TextureFilter.Linear;
        settings.filterMag = TextureFilter.Linear;
        settings.combineSubdirectories = true;
        
        TexturePacker.process(settings, "../android/assets/graphics/raw/background", "../android/assets/graphics/packed", "background");
        TexturePacker.process(settings, "../android/assets/graphics/raw/platforms", "../android/assets/graphics/packed", "platforms");
        TexturePacker.process(settings, "../android/assets/graphics/raw/character", "../android/assets/graphics/packed", "character");
        TexturePacker.process(settings, "../android/assets/graphics/raw/enemies", "../android/assets/graphics/packed", "enemies");
        TexturePacker.process(settings, "../android/assets/graphics/raw/items", "../android/assets/graphics/packed", "items");
        TexturePacker.process(settings, "../android/assets/graphics/raw/gui", "../android/assets/graphics/packed", "gui");
        TexturePacker.process(settings, "../android/assets/graphics/raw/guiskin", "../android/assets/graphics/packed", "guiskin");
    }
}
