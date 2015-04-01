package com.symbolplay.tria.screens;

import com.symbolplay.gamelibrary.controls.AnimatedImage;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.background.GameBackground;
import com.symbolplay.tria.screens.general.GameScreenUtils;

public final class LoadScreen extends ScreenBase {
    
    private final GameBackground background;
    
    public LoadScreen(GameContainer game) {
        super(game);
        
        background = new GameBackground(assetManager);
        
        AnimatedImage waitAnimatedImage = GameScreenUtils.getWaitAnimatedImage(assetManager);
        waitAnimatedImage.setPosition(
                (GameContainer.VIEWPORT_WIDTH - waitAnimatedImage.getWidth()) / 2.0f,
                (GameContainer.VIEWPORT_HEIGHT - waitAnimatedImage.getHeight()) / 2.0f);
        guiStage.addActor(waitAnimatedImage);
    }
    
    @Override
    public void renderImpl() {
        batch.setProjectionMatrix(cameraData.getGameAreaMatrix());
        batch.begin();
        background.render(batch);
        batch.end();
    }
}