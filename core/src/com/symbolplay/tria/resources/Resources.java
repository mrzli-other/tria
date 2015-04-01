package com.symbolplay.tria.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.symbolplay.tria.game.Sounds;

public final class Resources {
    
    private static final int LOADING_NOT_STARTED = 0;
    private static final int LOADING_INITIAL = 1;
    private static final int LOADING_GENERATING_FONTS = 2;
    private static final int LOADING_SKIN = 3;
    private static final int LOADING_COMPLETE = 4;
    
    private static final int NUM_FONTS = 4;
    
    private final AssetManager assetManager;
    private Skin guiSkin;
    private BitmapFont gameAreaFont;
    
    private BitmapFont[] fonts;
    private int[] fontSizes;
    private int currentFontGeneratedIndex;
    
    private static int loadingState;
    
    public Resources() {
        assetManager = new AssetManager();
        
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }
    
    public void initialize() {
        assetManager.load(ResourceNames.GRAPHICS_GUI_ATLAS, TextureAtlas.class);
        assetManager.load(ResourceNames.BACKGROUND_ATLAS, TextureAtlas.class);
        assetManager.finishLoading(); // required for load screen
        
        loadingState = LOADING_NOT_STARTED;
    }
    
    private void startLoadingInitial() {
        assetManager.load(ResourceNames.PLATFORMS_ATLAS, TextureAtlas.class);
        assetManager.load(ResourceNames.CHARACTER_ATLAS, TextureAtlas.class);
        assetManager.load(ResourceNames.ENEMIES_ATLAS, TextureAtlas.class);
        assetManager.load(ResourceNames.ITEMS_ATLAS, TextureAtlas.class);
        
        Sounds.initialize(assetManager);
    }
    
    private void startFontGeneration() {
        fonts = new BitmapFont[NUM_FONTS];
        fontSizes = new int[] { 18, 24, 32, 40 };
        currentFontGeneratedIndex = 0;
    }
    
    private boolean areAllFontsGenerated() {
        return currentFontGeneratedIndex >= NUM_FONTS;
    }
    
    private void generateNextFont() {
        fonts[currentFontGeneratedIndex] = FontGenerator.generate(fontSizes[currentFontGeneratedIndex]);
        currentFontGeneratedIndex++;
    }
    
    private void endFontGeneration() {
        gameAreaFont = fonts[0];
        gameAreaFont.setColor(Color.BLACK);
    }
    
    private void startLoadingSkin() {
        TextureAtlas guiSkinAtlas = new TextureAtlas(ResourceNames.GUI_SKIN_ATLAS);
        
        guiSkin = new Skin();
        guiSkin.addRegions(guiSkinAtlas);
        guiSkin.add(ResourceNames.FONT_SKIN_NAME_DEFAULT, fonts[0]);
        guiSkin.add(ResourceNames.FONT_SKIN_NAME_18, fonts[0]);
        guiSkin.add(ResourceNames.FONT_SKIN_NAME_24, fonts[1]);
        guiSkin.add(ResourceNames.FONT_SKIN_NAME_32, fonts[2]);
        guiSkin.add(ResourceNames.FONT_SKIN_NAME_40, fonts[3]);
        guiSkin.load(Gdx.files.internal(ResourceNames.GUI_SKIN));
    }
    
    public boolean update() {
        if (loadingState == LOADING_NOT_STARTED) {
            startLoadingInitial();
            loadingState = LOADING_INITIAL;
            return false;
        } else if (loadingState == LOADING_INITIAL) {
            if (assetManager.update()) {
                startFontGeneration();
                loadingState = LOADING_GENERATING_FONTS;
            }
            return false;
        } else if (loadingState == LOADING_GENERATING_FONTS) {
            if (areAllFontsGenerated()) {
                endFontGeneration();
                startLoadingSkin();
                loadingState = LOADING_SKIN;
            } else {
                generateNextFont();
            }
            return false;
        } else if (loadingState == LOADING_SKIN) {
            if (assetManager.update()) {
                loadingState = LOADING_COMPLETE;
            }
            return false;
        } else {
            return true;
        }
    }
    
    public Skin getGuiSkin() {
        return guiSkin;
    }
    
    public AssetManager getAssetManager() {
        return assetManager;
    }
    
    public BitmapFont getGameAreaFont() {
        return gameAreaFont;
    }
    
    public void dispose() {
        // same font is used twice in skin - this prevents exception when disposing skin
        for (BitmapFont font : fonts) {
            font.setOwnsTexture(false);
        }
        
        guiSkin.dispose();
        
        // now dispose fonts
        for (BitmapFont font : fonts) {
            font.setOwnsTexture(true);
            font.dispose();
        }
        
        // items font is the same as one of the font in fonts so it will be disposed there
        //itemFont.dispose();
        
        assetManager.dispose();
    }
}
