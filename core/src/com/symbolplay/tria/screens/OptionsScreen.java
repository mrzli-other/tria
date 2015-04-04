package com.symbolplay.tria.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.Sounds;
import com.symbolplay.tria.game.Vibration;
import com.symbolplay.tria.game.background.GameBackground;
import com.symbolplay.tria.persistence.gamepreferences.GamePreferencesWrapper;
import com.symbolplay.tria.screens.general.GameScreenUtils;
import com.symbolplay.tria.screens.general.ScoreLines;

public final class OptionsScreen extends ScreenBase {
    
    private final GameBackground background;
    
    private CheckBox soundCheckBox;
    private CheckBox vibrationCheckBox;
    private CheckBox comfirmDialogsCheckBox;
    private SelectBox<String> scoreLinesSelectBox;
    
    public OptionsScreen(GameContainer game) {
        super(game);
        
        guiStage.addListener(getStageInputListener(this));
        
        background = new GameBackground(assetManager);
        
        float offsetFromTop = GameScreenUtils.addTitleLabel("OPTIONS", guiSkin, guiStage);
        createContent(offsetFromTop, 0.0f);
    }
    
    @Override
    public void show(ObjectMap<String, Object> changeParams) {
        super.show(changeParams);
        
        GamePreferencesWrapper gamePreferencesWrapper = game.getGameData().getGamePreferences();
        soundCheckBox.setChecked(gamePreferencesWrapper.isSoundOn());
        vibrationCheckBox.setChecked(gamePreferencesWrapper.isVibrationOn());
        comfirmDialogsCheckBox.setChecked(gamePreferencesWrapper.isConfirmDialogsEnabled());
        scoreLinesSelectBox.setSelected(gamePreferencesWrapper.getSelectedScoreLines());
    }
    
    @Override
    public void renderImpl() {
        batch.setProjectionMatrix(cameraData.getGameAreaMatrix());
        batch.begin();
        background.render(batch);
        batch.end();
    }
    
    private void createContent(float offsetFromTop, float offsetFromBottom) {
        
        final GamePreferencesWrapper gamePreferencesWrapper = game.getGameData().getGamePreferences();
        
        float padding = 10.0f;
        
        soundCheckBox = getCheckBox("Sound");
        soundCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isChecked = ((CheckBox) actor).isChecked();
                gamePreferencesWrapper.setSoundOn(isChecked);
                Sounds.setSoundOn(isChecked);
            }
        });
        
        vibrationCheckBox = getCheckBox("Vibration");
        vibrationCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isChecked = ((CheckBox) actor).isChecked();
                gamePreferencesWrapper.setVibrationOn(isChecked);
                Vibration.setVibrationOn(isChecked);
            }
        });
        
        comfirmDialogsCheckBox = getCheckBox("Confirm dialogs");
        comfirmDialogsCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isChecked = ((CheckBox) actor).isChecked();
                gamePreferencesWrapper.setConfirmDialogsEnabled(isChecked);
            }
        });
        
        Label scoreLinesLabel = new Label("Score lines:", guiSkin, "font24");
        
        scoreLinesSelectBox = new SelectBox<String>(guiSkin, "font24");
        scoreLinesSelectBox.setItems(ScoreLines.NONE, ScoreLines.LOCAL, ScoreLines.GLOBAL);
        scoreLinesSelectBox.addListener(new ChangeListener() {
            
            @SuppressWarnings("rawtypes")
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedScoreLines = (String) ((SelectBox) actor).getSelected();
                gamePreferencesWrapper.setSelectedScoreLines(selectedScoreLines);
            }
        });
        //scoreLinesSelectBox.setWidth(200.0f);
        
        Table scoreLinesTable = new Table(guiSkin);
        scoreLinesTable.add(scoreLinesLabel).padRight(padding);
        scoreLinesTable.add(scoreLinesSelectBox);
        
        Table table = GameScreenUtils.addScrollTable(offsetFromTop, offsetFromBottom, guiSkin, guiStage);
        table.top().left();
        table.add(soundCheckBox).pad(padding).left();
        table.row();
        table.add(vibrationCheckBox).pad(padding).left();
        table.row();
        table.add(comfirmDialogsCheckBox).pad(padding).left();
        table.row();
        table.add(scoreLinesTable).pad(padding).left();
    }
    
    @SuppressWarnings("rawtypes")
    private CheckBox getCheckBox(String text) {
        CheckBox checkBox = new CheckBox(text, guiSkin, "font24");
        Cell cell0 = checkBox.getCells().get(0);
        cell0.size(32.0f);
        cell0.padRight(10.0f);
        
        checkBox.getLabel().setWrap(true);
        
        return checkBox;
    }
    
    private static InputListener getStageInputListener(final OptionsScreen screen) {
        return new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
                    screen.game.popScreen();
                    return true;
                }
                
                return false;
            }
        };
    }
}
