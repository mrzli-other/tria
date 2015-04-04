package com.symbolplay.tria.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.background.GameBackground;
import com.symbolplay.tria.screens.general.GameScreenUtils;

public final class HighScoreScreen extends ScreenBase {
    
    private static final float TAB_BUTTON_WIDTH = 210.0f;
    private static final float TAB_BUTTON_HEIGHT = 40.0f;
    
    private final GameBackground background;
    
    private TextButton localButton;
    private TextButton facebookButton;
    
    private ScrollPane scrollPane;
    
    private final HighScoreLocalSubscreen highScoreLocalSubscreen;
    private final HighScoreFacebookSubscreen highScoreFacebookSubscreen;
    
    public HighScoreScreen(GameContainer game) {
        super(game);
        
        guiStage.addListener(getStageInputListener(this));
        
        background = new GameBackground(assetManager);
        
        float titleHeight = GameScreenUtils.addTitleLabel("SCORE", guiSkin, guiStage);
        float tabButtonsHeight = addTabButtons(titleHeight);
        float offsetFromTop = titleHeight + tabButtonsHeight;
        scrollPane = GameScreenUtils.addScrollPane(offsetFromTop, 0.0f, guiSkin, guiStage);
        
        highScoreLocalSubscreen = new HighScoreLocalSubscreen(
                guiSkin,
                scrollPane,
                game.getGameData());
        highScoreFacebookSubscreen = new HighScoreFacebookSubscreen(
                assetManager,
                guiSkin,
                scrollPane,
                game.getFacebookAccessor(),
                game.getTriaServiceAccessor());
    }
    
    @Override
    public void show(ObjectMap<String, Object> changeParams) {
        super.show(changeParams);
        checkLocalButton();
    }
    
    @Override
    protected void updateImpl(float delta) {
        super.updateImpl(delta);
        highScoreFacebookSubscreen.update(delta);
    }
    
    @Override
    public void renderImpl() {
        batch.setProjectionMatrix(cameraData.getGameAreaMatrix());
        batch.begin();
        background.render(batch);
        batch.end();
    }
    
    private float addTabButtons(float offsetFromTop) {
        
        float padding = 10.0f;
        
        Table tabButtonsTable = new Table(guiSkin);
        tabButtonsTable.setBounds(0.0f, GameContainer.VIEWPORT_HEIGHT - offsetFromTop - TAB_BUTTON_HEIGHT, GameContainer.VIEWPORT_WIDTH, TAB_BUTTON_HEIGHT);
        tabButtonsTable.top().left();
        tabButtonsTable.padBottom(padding);
        
        localButton = new TextButton("LOCAL", guiSkin, "font24-checked");
        localButton.setSize(TAB_BUTTON_WIDTH, TAB_BUTTON_HEIGHT);
        localButton.addListener(new ChangeListener() {
            
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (((TextButton) actor).isChecked()) {
                    selectLocalSubscreen();
                }
            }
        });
        
        facebookButton = new TextButton("FACEBOOK", guiSkin, "font24-checked");
        facebookButton.setSize(TAB_BUTTON_WIDTH, TAB_BUTTON_HEIGHT);
        facebookButton.addListener(new ChangeListener() {
            
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (((TextButton) actor).isChecked()) {
                    selectFacebookSubscreen();
                }
            }
        });
        
        tabButtonsTable.add(localButton).width(TAB_BUTTON_WIDTH).padLeft(padding);
        tabButtonsTable.add(facebookButton).width(TAB_BUTTON_WIDTH).padLeft(padding);
        guiStage.addActor(tabButtonsTable);
        
        new ButtonGroup<TextButton>(localButton, facebookButton);
        
        return TAB_BUTTON_HEIGHT + padding;
    }
    
    private void checkLocalButton() {
        if (!localButton.isChecked()) {
            localButton.setChecked(true);
        } else {
            selectLocalSubscreen();
        }
    }
    
//    private void checkFacebookButton() {
//        if (!facebookButton.isChecked()) {
//            facebookButton.setChecked(true);
//        } else {
//            selectFacebookSubscreen();
//        }
//    }
    
    private void selectLocalSubscreen() {
        highScoreFacebookSubscreen.unselectSubscreen();
        highScoreLocalSubscreen.selectSubscreen();
    }
    
    private void selectFacebookSubscreen() {
        highScoreLocalSubscreen.unselectSubscreen();
        highScoreFacebookSubscreen.selectSubscreen();
    }
    
    private static InputListener getStageInputListener(final HighScoreScreen screen) {
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
    
    // private void addHighScoreSelectBox() {
    // mHighScoreSelectBox = new SelectBox(new String[] { "Android", "Windows", "Linux", "OSX" }, mGuiSkin);
    // mHighScoreSelectBox.setBounds(0.0f, 0.0f, 100.0f, 50.0f);
    // mGuiStage.addActor(mHighScoreSelectBox);
    // }
}
