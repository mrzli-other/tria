package com.symbolplay.tria.screens;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.gamelibrary.controls.AnimatedImage;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.background.GameBackground;
import com.symbolplay.tria.net.NewsAccessor;
import com.symbolplay.tria.net.NewsAndImageData;
import com.symbolplay.tria.net.NewsData;
import com.symbolplay.tria.net.NewsListener;
import com.symbolplay.tria.resources.ResourceNames;
import com.symbolplay.tria.screens.general.ChangeParamKeys;
import com.symbolplay.tria.screens.general.GameScreenUtils;

public final class NewsScreen extends ScreenBase {
    
    private final GameBackground background;
    private Table table;
    
    private final NewsAccessor newsAccessor;
    
    private String selectedAndroidAddress;
    private String selectedGenericAddress;
    
    public NewsScreen(GameContainer game) {
        super(game);
        
        guiStage.addListener(getStageInputListener(this));
        
        background = new GameBackground(assetManager);
        
        float offsetFromTop = GameScreenUtils.addTitleLabel("NEWS", guiSkin, guiStage);
        table = GameScreenUtils.addScrollTable(offsetFromTop, 0.0f, guiSkin, guiStage);
        
        newsAccessor = game.getNewsAccessor();
        newsAccessor.addNewsListener(getNewsListener());
        
        resetSelectedAddresses();
    }
    
    @Override
    public void show(ObjectMap<String, Object> changeParams) {
        super.show(changeParams);
        
        table.clear();
        table.center();
        
        AnimatedImage waitAnimatedImage = GameScreenUtils.getWaitAnimatedImage(assetManager);
        table.add(waitAnimatedImage);
        
        newsAccessor.requestLastNewsIndex();
    }
    
    @Override
    public void resume(ObjectMap<String, Object> changeParams) {
        super.resume(changeParams);
        
        if (changeParams != null) {
            Boolean isConfirmOk = (Boolean) changeParams.get(ChangeParamKeys.CONFIRM_RESULT, false);
            if (isConfirmOk) {
                game.getPlatformSpecificInterface().goToAddress(selectedAndroidAddress, selectedGenericAddress);
            }
        }
        
        resetSelectedAddresses();
    }
    
    @Override
    public void renderImpl() {
        batch.setProjectionMatrix(cameraData.getGameAreaMatrix());
        batch.begin();
        background.render(batch);
        batch.end();
    }
    
    private void setTextContent(String text) {
        table.clear();
        table.center();
        
        LabelStyle labelStyle = guiSkin.get("font32", LabelStyle.class);
        
        float labelPadding = 10.0f;
        float labelWidth = GameContainer.VIEWPORT_WIDTH - 2.0f * labelPadding;
        
        Label label = getLabel(text, labelStyle);
        label.setAlignment(Align.center);
        table.add(label).width(labelWidth).pad(labelPadding);
    }
    
    private void refreshContent(Array<NewsAndImageData> newsAndImageDataList) {
        table.clear();
        table.top().left();
        
        LabelStyle newsTitleLabelStyle = guiSkin.get("font32", LabelStyle.class);
        LabelStyle newsTextLabelStyle = guiSkin.get("font24", LabelStyle.class);
        
        TextureAtlas atlas = assetManager.get(ResourceNames.GRAPHICS_GUI_ATLAS);
        
        float labelPadding = 10.0f;
        float topNewsPadding = labelPadding * 3.5f;
        float bottomNewsPadding = labelPadding * 1.5f;
        float labelWidth = GameContainer.VIEWPORT_WIDTH - 2.0f * labelPadding;
        
        float checkItOutButtonPadding = 10.0f;
        float checkItOutButtonWidth = 300.0f;
        
        float moreNewsButtonPadding = 20.0f;
        float moreNewsButtonWidth = 300.0f;
        
        for (int i = 0; i < newsAndImageDataList.size; i++) {
            NewsAndImageData newsAndImageData = newsAndImageDataList.get(i);
            NewsData newsData = newsAndImageData.getNewsData();
            
            if (i != 0) {
                Image newsSeparatorImage = getImage(ResourceNames.GUI_NEWS_NEWS_SEPARATOR_IMAGE_NAME, atlas);
                table.add(newsSeparatorImage);
                table.row();
            }
            
            float topDatePadding = i != 0 ? topNewsPadding : 0.0f;
            
            Label dateLabel = getLabel(newsData.getDate(), newsTitleLabelStyle);
            table.add(dateLabel).width(labelWidth).pad(labelPadding).padTop(topDatePadding).padBottom(0.0f);
            table.row();
            
            Label titleLabel = getLabel(newsData.getTitle(), newsTitleLabelStyle);
            table.add(titleLabel).width(labelWidth).pad(labelPadding).padTop(0.0f);
            table.row();
            
            String imageName = newsData.getImage();
            if (!StringUtils.isEmpty(imageName)) {
                byte[] newsImageData = newsAndImageData.getImageData();
                if (newsImageData != null) {
                    Image newsImage = GameScreenUtils.getNewsImage(newsImageData);
                    table.add(newsImage);
                } else {
                    AnimatedImage waitAnimatedImage = GameScreenUtils.getWaitAnimatedImage(assetManager);
                    table.add(waitAnimatedImage);
                }
                table.row();
            }
            
            boolean hasCheckItOutButton = StringUtils.isNotEmpty(newsData.getAndroidLink()) || StringUtils.isNotEmpty(newsData.getGeneralLink());
            
            String newsText = newsData.getText();
            Label newsTextLabel = getLabel(newsText, newsTextLabelStyle);
            table.add(newsTextLabel).width(labelWidth).pad(labelPadding).padBottom(hasCheckItOutButton ? labelPadding : bottomNewsPadding);
            table.row();
            
            if (hasCheckItOutButton) {
                TextButton checkItOutTextButton = getCheckItOutButton(newsData.getAndroidLink(), newsData.getGeneralLink());
                table.add(checkItOutTextButton).padTop(checkItOutButtonPadding).padBottom(bottomNewsPadding).width(checkItOutButtonWidth).center();
                table.row();
            }
        }
        
        if (newsAccessor.hasMoreNewsToRequest()) {
            table.add(getMoreNewsButton()).padTop(moreNewsButtonPadding).padBottom(moreNewsButtonPadding).width(moreNewsButtonWidth).center();
            table.row();
        }
    }
    
    private static Label getLabel(String text, LabelStyle labelStyle) {
        Label label = new Label(text, labelStyle);
        label.setWrap(true);
        return label;
    }
    
    private static Image getImage(String imageName, TextureAtlas atlas) {
        TextureRegion imageRegion = atlas.findRegion(imageName);
        Image image = new Image(imageRegion);
        return image;
    }
    
    private TextButton getCheckItOutButton(final String androidAddress, final String genericAddress) {
        TextButton moreNewsButton = new TextButton("CHECK IT OUT", guiSkin, "font32");
        moreNewsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.getGameData().getGamePreferences().isConfirmDialogsEnabled()) {
                    selectedAndroidAddress = androidAddress;
                    selectedGenericAddress = genericAddress;
                    game.pushScreen(GameContainer.CONFIRM_SCREEN_NAME, getChangeParams());
                } else {
                    game.getPlatformSpecificInterface().goToAddress(androidAddress, genericAddress);
                }
            }
        });
        
        return moreNewsButton;
    }
    
    private TextButton getMoreNewsButton() {
        TextButton moreNewsButton = new TextButton("MORE NEWS", guiSkin, "font40");
        moreNewsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                newsAccessor.requestInitialOrNextNewsGroup(false);
            }
        });
        
        return moreNewsButton;
    }
    
    private static ObjectMap<String, Object> getChangeParams() {
        ObjectMap<String, Object> changeParams = new ObjectMap<String, Object>(1);
        changeParams.put(ChangeParamKeys.CONFIRM_TEXT, "This action will open a browser window or Google Play application.");
        return changeParams;
    }
    
    private void resetSelectedAddresses() {
        selectedAndroidAddress = "";
        selectedGenericAddress = "";
    }
    
    private static InputListener getStageInputListener(final NewsScreen screen) {
        return new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
                    screen.game.changeScreen(GameContainer.MAIN_MENU_SCREEN_NAME);
                    return true;
                }
                
                return false;
            }
        };
    }
    
    private NewsListener getNewsListener() {
        return new NewsListener() {
            
            @Override
            public void lastNewsIndexReceived(int lastNewsIndex) {
                // only handle this if NewsScreen is active
                if (game.peekScreen() != NewsScreen.this) {
                    return;
                }
                
                if (lastNewsIndex == -1) {
                    setTextContent("Failed to connect!");
                } else if (lastNewsIndex == 0) {
                    setTextContent("No news.");
                } else {
                    gameData.getGamePreferences().setLastNewsShownIndex(lastNewsIndex);
                    newsAccessor.requestInitialOrNextNewsGroup(true);
                }
            }
            
            @Override
            public void newsDataReceived(Array<NewsAndImageData> newsAndImageDataList) {
                refreshContent(newsAndImageDataList);
            }
            
            @Override
            public void error(String message) {
                setTextContent(message);
            }
        };
    }
}
