package com.symbolplay.tria.screens.general;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.gamelibrary.controls.AnimatedImage;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.resources.ResourceNames;

public final class GameScreenUtils {
    
    public static float addTitleLabel(String text, Skin guiSkin, Stage guiStage) {
        LabelStyle labelStyle = guiSkin.get("font40", LabelStyle.class);
        
        TextBounds textBounds = labelStyle.font.getBounds(text);
        
        float padding = 10.0f;
        float labelHeight = textBounds.height;
        float labelY = GameContainer.VIEWPORT_HEIGHT - labelHeight - padding;
        
        Label label = new Label(text, labelStyle);
        label.setBounds(0.0f, labelY, GameContainer.VIEWPORT_WIDTH, labelHeight);
        label.setAlignment(Align.center);
        guiStage.addActor(label);
        
        return labelHeight + 3.0f * padding;
    }
    
    public static Table addScrollTable(float offsetFromTop, float offsetFromBottom, Skin guiSkin, Stage guiStage) {
        
        Table table = new Table(guiSkin);
        table.left().top();
        
        ScrollPane scrollPane = addScrollPane(offsetFromTop, offsetFromBottom, guiSkin, guiStage);
        scrollPane.setWidget(table);
        
        guiStage.addActor(scrollPane);
        
        return table;
    }
    
    public static ScrollPane addScrollPane(float offsetFromTop, float offsetFromBottom, Skin guiSkin, Stage guiStage) {
        
        float scrollPaneHeight = GameContainer.VIEWPORT_HEIGHT - offsetFromTop - offsetFromBottom;
        
        ScrollPane scrollPane = new ScrollPane(null, guiSkin);
        scrollPane.setBounds(0.0f, offsetFromBottom, GameContainer.VIEWPORT_WIDTH, scrollPaneHeight);
        scrollPane.setOverscroll(false, false);
        
        guiStage.addActor(scrollPane);
        
        return scrollPane;
    }
    
    public static AnimatedImage getWaitAnimatedImage(AssetManager assetManager) {
        TextureAtlas atlas = assetManager.get(ResourceNames.GRAPHICS_GUI_ATLAS);
        Array<AtlasRegion> waitAnimationAtlasRegions = atlas.findRegions(ResourceNames.GUI_WAIT_ANIMATION_32_IMAGE_NAME);
        Animation waitAnimation = new Animation(0.1f, waitAnimationAtlasRegions, Animation.PlayMode.LOOP);
        AnimatedImage waitAnimatedImage = new AnimatedImage(waitAnimation);
        return waitAnimatedImage;
    }
    
    public static Image getNewsImage(byte[] imageData) {
        Pixmap pixmap = new Pixmap(imageData, 0, imageData.length);
        Texture texture = new Texture(pixmap);
        Image image = new Image(texture);
        return image;
    }
}
