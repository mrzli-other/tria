package com.symbolplay.tria.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.gamelibrary.controls.HighlightImageButton;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.background.GameBackground;
import com.symbolplay.tria.net.NewsAccessor;
import com.symbolplay.tria.net.NewsAndImageData;
import com.symbolplay.tria.net.NewsListener;
import com.symbolplay.tria.persistence.gamepreferences.GamePreferencesWrapper;
import com.symbolplay.tria.resources.ResourceNames;
import com.symbolplay.tria.screens.general.ChangeParamKeys;
import com.symbolplay.tria.screens.general.FacebookLoginButton;

public final class MainMenuScreen extends ScreenBase {
    
    private static final float BOTTOM_CONTROLS_PADDING = 5.0f;
    
    private static final float MENU_BUTTON_X = 75.0f;
    private static final float BUTTON_Y_STEP = 120.0f;
    private static final float TOP_BUTTON_Y = 540.0f;
    private static final float MENU_BUTTON_WIDTH = 300.0f;
    private static final float MENU_BUTTON_HEIGHT = 80.0f;
    
    private static final int CONFIRM_DIALOG_TARGET_NONE = 0;
    private static final int CONFIRM_DIALOG_TARGET_COMPANY = 1;
    private static final int CONFIRM_DIALOG_TARGET_FACEBOOK_LOGOUT = 2;
    
    private final GameBackground background;
    
    private final HighlightImageButton companyLogoButton;
    private final HighlightImageButton newsButton;
    private final HighlightImageButton creditsButton;
    private final HighlightImageButton howToPlayButton;
    private final FacebookLoginButton facebookLoginButton;
    
    private final NewsAccessor newsAccessor;
    
    private float elapsedSinceShown;
    private boolean isCheckRatingRequestCompleted;
    
    private int confirmTarget;
    
    public MainMenuScreen(GameContainer game) {
        super(game);
        
        guiStage.addListener(getStageInputListener());
        
        background = new GameBackground(assetManager);
        
        TextureAtlas atlas = assetManager.get(ResourceNames.GRAPHICS_GUI_ATLAS);
        
        addGameTitleLabel();
        
        companyLogoButton = new HighlightImageButton(
                BOTTOM_CONTROLS_PADDING,
                BOTTOM_CONTROLS_PADDING,
                atlas,
                ResourceNames.GUI_CREDITS_COMPANY_LOGO_IMAGE_NAME,
                ResourceNames.GUI_CREDITS_COMPANY_LOGO_IMAGE_NAME,
                assetManager);
        companyLogoButton.addListener(getCompanyLogoClickListener());
        guiStage.addActor(companyLogoButton);
        
        newsButton = new HighlightImageButton(
                companyLogoButton.getRight() + BOTTOM_CONTROLS_PADDING,
                BOTTOM_CONTROLS_PADDING,
                atlas,
                ResourceNames.GUI_MAIN_MENU_NEWS_BUTTON_IMAGE_NAME,
                ResourceNames.GUI_MAIN_MENU_NEWS_BUTTON_HIGHLIGHT_IMAGE_NAME,
                assetManager);
        newsButton.addListener(getMenuButtonClickListener(GameContainer.NEWS_SCREEN_NAME));
        guiStage.addActor(newsButton);
        
        creditsButton = new HighlightImageButton(
                newsButton.getRight() + BOTTOM_CONTROLS_PADDING,
                BOTTOM_CONTROLS_PADDING,
                atlas,
                ResourceNames.GUI_MAIN_MENU_CREDITS_BUTTON_IMAGE_NAME,
                ResourceNames.GUI_MAIN_MENU_CREDITS_BUTTON_IMAGE_NAME,
                assetManager);
        creditsButton.addListener(getMenuButtonClickListener(GameContainer.CREDITS_SCREEN_NAME));
        guiStage.addActor(creditsButton);
        
        howToPlayButton = new HighlightImageButton(
                creditsButton.getRight() + BOTTOM_CONTROLS_PADDING,
                BOTTOM_CONTROLS_PADDING,
                atlas,
                ResourceNames.GUI_MAIN_MENU_INSTRUCTIONS_BUTTON_IMAGE_NAME,
                ResourceNames.GUI_MAIN_MENU_INSTRUCTIONS_BUTTON_HIGHLIGHT_IMAGE_NAME,
                assetManager);
        howToPlayButton.addListener(getMenuButtonClickListener(GameContainer.HOW_TO_PLAY_SCREEN_NAME));
        guiStage.addActor(howToPlayButton);
        
        facebookLoginButton = new FacebookLoginButton(
                howToPlayButton.getRight() + BOTTOM_CONTROLS_PADDING,
                BOTTOM_CONTROLS_PADDING,
                atlas,
                game.getFacebookAccessor()) {
            
            @Override
            public void startLogoutProcedure() {
                MainMenuScreen.this.startLogoutProcedure();
            }
        };
        guiStage.addActor(facebookLoginButton);
        
        // menu buttons
        TextButton startButton = new TextButton("START", guiSkin, "font40");
        startButton.setBounds(MENU_BUTTON_X, TOP_BUTTON_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainMenuScreen.this.game.changeScreen(GameContainer.PLAY_SCREEN_NAME);
            }
        });
        guiStage.addActor(startButton);
        
        TextButton highScoreButton = new TextButton("HIGH SCORE", guiSkin, "font40");
        highScoreButton.setBounds(MENU_BUTTON_X, TOP_BUTTON_Y - BUTTON_Y_STEP, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        highScoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainMenuScreen.this.game.changeScreen(GameContainer.HIGH_SCORE_SCREEN_NAME);
            }
        });
        guiStage.addActor(highScoreButton);
        
        TextButton upgradesButton = new TextButton("UPGRADES", guiSkin, "font40");
        upgradesButton.setBounds(MENU_BUTTON_X, TOP_BUTTON_Y - BUTTON_Y_STEP * 2.0f, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        upgradesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainMenuScreen.this.game.changeScreen(GameContainer.UPGRADES_SCREEN_NAME);
            }
        });
        guiStage.addActor(upgradesButton);
        
        TextButton optionsButton = new TextButton("OPTIONS", guiSkin, "font40");
        optionsButton.setBounds(MENU_BUTTON_X, TOP_BUTTON_Y - BUTTON_Y_STEP * 3.0f, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainMenuScreen.this.game.pushScreen(GameContainer.OPTIONS_SCREEN_NAME);
            }
        });
        guiStage.addActor(optionsButton);
        
        newsAccessor = game.getNewsAccessor();
        newsAccessor.addNewsListener(getNewsListener());
    }
    
    @Override
    public void show(ObjectMap<String, Object> changeParams) {
        super.show(changeParams);
        initialDisplayActions();
        
        confirmTarget = CONFIRM_DIALOG_TARGET_NONE;
    }
    
    @Override
    public void hide() {
        super.hide();
        facebookLoginButton.setActive(false);
    }
    
    @Override
    public void pause() {
        super.pause();
        facebookLoginButton.setActive(false);
    }
    
    @Override
    public void resume(ObjectMap<String, Object> changeParams) {
        super.resume(changeParams);
        initialDisplayActions();
        
        if (changeParams != null) {
            Boolean isConfirmOk = (Boolean) changeParams.get(ChangeParamKeys.CONFIRM_RESULT, false);
            if (isConfirmOk) {
                if (confirmTarget == CONFIRM_DIALOG_TARGET_COMPANY) {
                    goToCompanyAddress();
                } else if (confirmTarget == CONFIRM_DIALOG_TARGET_FACEBOOK_LOGOUT) {
                    facebookLoginButton.logout();
                }
            }
        }
        confirmTarget = CONFIRM_DIALOG_TARGET_NONE;
    }
    
    @Override
    protected void updateImpl(float delta) {
        elapsedSinceShown += delta;
        if (elapsedSinceShown >= 2.0f && !isCheckRatingRequestCompleted) {
            checkRateRequest();
            isCheckRatingRequestCompleted = true;
        }
    }
    
    @Override
    public void renderImpl() {
        batch.setProjectionMatrix(cameraData.getGameAreaMatrix());
        batch.begin();
        background.render(batch);
        batch.end();
    }
    
    private void addGameTitleLabel() {
        float padding = 15.0f;
        float titleLabelHeight = 100.0f;
        
        Label titleLabel = new Label("SYMBOL JUMP", guiSkin, "font40");
        titleLabel.setBounds(padding, GameContainer.VIEWPORT_HEIGHT - titleLabelHeight, GameContainer.VIEWPORT_WIDTH - 2.0f * padding, titleLabelHeight);
        titleLabel.setAlignment(Align.left);
        guiStage.addActor(titleLabel);
    }
    
    private void initialDisplayActions() {
        boolean isHowToPlayShown = gameData.getGamePreferences().isHowToPlayShown();
        howToPlayButton.setHighlight(!isHowToPlayShown);
        
        elapsedSinceShown = 0.0f;
        isCheckRatingRequestCompleted = false;
        
        newsButton.setHighlight(false);
        newsAccessor.requestLastNewsIndex();
        
        facebookLoginButton.setActive(true);
    }
    
    private void checkRateRequest() {
        GamePreferencesWrapper gamePreferences = gameData.getGamePreferences();
        
        if (gamePreferences.isRatingRequestDisabled()) {
            return;
        }
        
        // if less than 3 days have passed do not request rating; 3 days = 259.200.000 milliseconds
        if (System.currentTimeMillis() - gamePreferences.getLastRatingRequestTime() < 259200000) {
            return;
        }
        
        // need to play at least 20 games between rating requests
        if (gamePreferences.getGamesPlayedSinceLastRatingRequest() < 20) {
            return;
        }
        
        game.pushScreen(GameContainer.RATE_REQUEST_SCREEN_NAME);
    }
    
    private void goToCompanyAddress() {
        game.getPlatformSpecificInterface().goToAddress(null, "http://symbolplay.com");
    }
    
    private static ObjectMap<String, Object> getChangeParams(String dialogText) {
        ObjectMap<String, Object> changeParams = new ObjectMap<String, Object>(1);
        changeParams.put(ChangeParamKeys.CONFIRM_TEXT, dialogText);
        return changeParams;
    }
    
    private void setNewsButtonHighlight(int lastNewsIndex) {
        boolean isNewsButtonHighlight = lastNewsIndex > 0 && lastNewsIndex > gameData.getGamePreferences().getLastNewsShownIndex();
        newsButton.setHighlight(isNewsButtonHighlight);
    }
    
    private void startLogoutProcedure() {
        if (game.getGameData().getGamePreferences().isConfirmDialogsEnabled()) {
            confirmTarget = CONFIRM_DIALOG_TARGET_FACEBOOK_LOGOUT;
            game.pushScreen(GameContainer.CONFIRM_SCREEN_NAME, getChangeParams("Do you wish to logout from Facebook?"));
        } else {
            facebookLoginButton.logout();
        }
    }
    
    private InputListener getStageInputListener() {
        return new InputListener() {
            
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
                    Gdx.app.exit();
                    return true;
                }
                
                return false;
            }
        };
    }
    
    private ClickListener getMenuButtonClickListener(final String screenName) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.changeScreen(screenName);
            }
        };
    }
    
    private ClickListener getCompanyLogoClickListener() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.getGameData().getGamePreferences().isConfirmDialogsEnabled()) {
                    confirmTarget = CONFIRM_DIALOG_TARGET_COMPANY;
                    game.pushScreen(GameContainer.CONFIRM_SCREEN_NAME, getChangeParams("This action will open a browser window."));
                } else {
                    goToCompanyAddress();
                }
            }
        };
    }
    
    private NewsListener getNewsListener() {
        return new NewsListener() {
            
            @Override
            public void lastNewsIndexReceived(int lastNewsIndex) {
                setNewsButtonHighlight(lastNewsIndex);
            }
            
            @Override
            public void newsDataReceived(Array<NewsAndImageData> newsAndImageDataList) {
            }
            
            @Override
            public void error(String message) {
            }
        };
    }
}