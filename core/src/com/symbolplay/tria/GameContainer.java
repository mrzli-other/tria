package com.symbolplay.tria;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Logger;
import com.symbolplay.gamelibrary.game.CameraData;
import com.symbolplay.gamelibrary.game.GameBase;
import com.symbolplay.gamelibrary.game.Screen;
import com.symbolplay.tria.game.Sounds;
import com.symbolplay.tria.game.Vibration;
import com.symbolplay.tria.net.FacebookAccessor;
import com.symbolplay.tria.net.FacebookInterface;
import com.symbolplay.tria.net.NewsAccessor;
import com.symbolplay.tria.net.TriaServiceAccessor;
import com.symbolplay.tria.persistence.GameData;
import com.symbolplay.tria.persistence.gamepreferences.GamePreferencesWrapper;
import com.symbolplay.tria.resources.Resources;
import com.symbolplay.tria.screens.ConfirmScreen;
import com.symbolplay.tria.screens.CreditsScreen;
import com.symbolplay.tria.screens.GameOverScreen;
import com.symbolplay.tria.screens.HighScoreScreen;
import com.symbolplay.tria.screens.HowToPlayScreen;
import com.symbolplay.tria.screens.LoadScreen;
import com.symbolplay.tria.screens.MainMenuScreen;
import com.symbolplay.tria.screens.NewsScreen;
import com.symbolplay.tria.screens.PauseScreen;
import com.symbolplay.tria.screens.PlayScreen;
import com.symbolplay.tria.screens.RateRequestScreen;
import com.symbolplay.tria.screens.OptionsScreen;
import com.symbolplay.tria.screens.UpgradesScreen;
    
public class GameContainer extends GameBase {
    
    public static final String LOG = GameContainer.class.getSimpleName();
    
    public static final String LOAD_SCREEN_NAME = "Load";
    public static final String MAIN_MENU_SCREEN_NAME = "MainMenu";
    public static final String PLAY_SCREEN_NAME = "Play";
    public static final String PAUSE_SCREEN_NAME = "Pause";
    public static final String HIGH_SCORE_SCREEN_NAME = "HighScore";
    public static final String UPGRADES_SCREEN_NAME = "Upgrades";
    public static final String OPTIONS_SCREEN_NAME = "Options";
    public static final String NEWS_SCREEN_NAME = "News";
    public static final String CREDITS_SCREEN_NAME = "Credits";
    public static final String HOW_TO_PLAY_SCREEN_NAME = "HowToPlay";
    public static final String RATE_REQUEST_SCREEN_NAME = "RateRequest";
    public static final String GAME_OVER_SCREEN_NAME = "GameOver";
    public static final String CONFIRM_SCREEN_NAME = "Confirm";
    
    public static final float METER_TO_PIXEL = 40.0f;
    public static final float PIXEL_TO_METER = 1.0f / METER_TO_PIXEL;
    
    public static final float VIEWPORT_WIDTH = 450.0f;
    public static final float VIEWPORT_HEIGHT = 800.0f;
    
    public static final float GAME_AREA_WIDTH = VIEWPORT_WIDTH * PIXEL_TO_METER;
    public static final float GAME_AREA_HEIGHT = VIEWPORT_HEIGHT * PIXEL_TO_METER;
    
    //private FPSLogger mFpsLogger;
    
    private final PlatformSpecificInterface platformSpecificInterface;
    private final FacebookInterface facebookInterface;
    
    private Resources resources;
    private boolean isResourcesLoaded;
    private GameData gameData;
    private CameraData cameraData;
    
    private NewsAccessor newsAccessor;
    private FacebookAccessor facebookAccessor;
    private TriaServiceAccessor triaServiceAccessor;
    
    public GameContainer(PlatformSpecificInterface platformSpecificInterface, FacebookInterface facebookInterface) {
        this.platformSpecificInterface = platformSpecificInterface;
        this.facebookInterface = facebookInterface;
    }
    
    @Override
    public void create() {
        
        //mFpsLogger = new FPSLogger();
        Gdx.app.setLogLevel(Logger.DEBUG);
        Gdx.input.setCatchBackKey(true);
        
        com.symbolplay.gamelibrary.util.Logger.setTag("Tria");
        
        cameraData = new CameraData(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), VIEWPORT_WIDTH, VIEWPORT_HEIGHT, PIXEL_TO_METER);
        
        gameData = new GameData();
        handleGamePreferences(gameData.getGamePreferences());
        
        resources = new Resources();
        resources.initialize();
        isResourcesLoaded = false;
        
        newsAccessor = new NewsAccessor();
        gameContainerUpdateables.add(newsAccessor);
        facebookAccessor = new FacebookAccessor(facebookInterface);
        gameContainerUpdateables.add(facebookAccessor);
        triaServiceAccessor = new TriaServiceAccessor();
        gameContainerUpdateables.add(triaServiceAccessor);
        
        // pre-request some stuff the game will need at some point
        newsAccessor.requestLastNewsIndex();
        if (facebookAccessor.isLoggedIn()) {
            facebookAccessor.requestProfileAndFriends();
        }
        
        addScreen(LOAD_SCREEN_NAME, new LoadScreen(this));
        changeScreen(LOAD_SCREEN_NAME);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        gameData.dispose();
        resources.dispose();
    }
    
    @Override
    public void pause() {
        Screen topScreen = peekScreen();
        if (topScreen != null && topScreen instanceof PlayScreen) {
            pushScreen(PAUSE_SCREEN_NAME);
        }
        super.pause();
    }
    
    @Override
    public void render() {
        //mFpsLogger.log();
        
        if (!isResourcesLoaded && resources.update()) {
            initializeScreens();
            changeScreen(MAIN_MENU_SCREEN_NAME);
            isResourcesLoaded = true;
        }
        
        super.render();
    }
    
    @Override
    public void resize(int width, int height) {
        cameraData.resize(width, height);
        super.resize(width, height);
    }
    
    private void handleGamePreferences(GamePreferencesWrapper gamePreferencesWrapper) {
        Sounds.setSoundOn(gamePreferencesWrapper.isSoundOn());
        Vibration.setVibrationOn(gamePreferencesWrapper.isVibrationOn());
    }
    
    public PlatformSpecificInterface getPlatformSpecificInterface() {
        return platformSpecificInterface;
    }
    
    public FacebookInterface getFacebookInterface() {
        return facebookInterface;
    }
    
    public Resources getResources() {
        return resources;
    }
    
    public GameData getGameData() {
        return gameData;
    }
    
    public CameraData getCameraData() {
        return cameraData;
    }
    
    public NewsAccessor getNewsAccessor() {
        return newsAccessor;
    }
    
    public FacebookAccessor getFacebookAccessor() {
        return facebookAccessor;
    }
    
    public TriaServiceAccessor getTriaServiceAccessor() {
        return triaServiceAccessor;
    }
    
    private void initializeScreens() {
        addScreen(MAIN_MENU_SCREEN_NAME, new MainMenuScreen(this));
        addScreen(PLAY_SCREEN_NAME, new PlayScreen(this));
        addScreen(PAUSE_SCREEN_NAME, new PauseScreen(this));
        addScreen(HIGH_SCORE_SCREEN_NAME, new HighScoreScreen(this));
        addScreen(UPGRADES_SCREEN_NAME, new UpgradesScreen(this));
        addScreen(OPTIONS_SCREEN_NAME, new OptionsScreen(this));
        addScreen(NEWS_SCREEN_NAME, new NewsScreen(this));
        addScreen(CREDITS_SCREEN_NAME, new CreditsScreen(this));
        addScreen(HOW_TO_PLAY_SCREEN_NAME, new HowToPlayScreen(this));
        addScreen(RATE_REQUEST_SCREEN_NAME, new RateRequestScreen(this));
        addScreen(GAME_OVER_SCREEN_NAME, new GameOverScreen(this));
        addScreen(CONFIRM_SCREEN_NAME, new ConfirmScreen(this));
    }
}
