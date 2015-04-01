package com.symbolplay.tria.screens;

import java.util.Locale;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.background.GameBackground;
import com.symbolplay.tria.game.character.UpgradesUtil;
import com.symbolplay.tria.persistence.userdata.CareerData;
import com.symbolplay.tria.resources.ResourceNames;
import com.symbolplay.tria.screens.general.GameScreenUtils;
import com.symbolplay.tria.screens.general.UpgradeItemDisplay;

public final class UpgradesScreen extends ScreenBase {
    
    private final GameBackground background;
    
    private final UpgradeItemDisplay upgradeItemDisplayAntiGravity;
    private final UpgradeItemDisplay upgradeItemDisplayRocket;
    private final UpgradeItemDisplay upgradeItemDisplayLife;
    
    private final Label upgradeTextLabel;
    private final float upgradeTextLabelTop;
    
    private final TextButton upgradeButton;
    
    public UpgradesScreen(GameContainer game) {
        super(game);
        
        guiStage.addListener(getStageInputListener(this));
        
        background = new GameBackground(assetManager);
        
        float offsetFromTop = GameScreenUtils.addTitleLabel("UPGRADES", guiSkin, guiStage);
        
        float upgradeItemY = GameContainer.VIEWPORT_HEIGHT - offsetFromTop - 90.0f;
        upgradeItemDisplayAntiGravity = new UpgradeItemDisplay(
                ResourceNames.GUI_UPGRADE_UPGRADE_ITEM_ANTI_GRAVITY_IMAGE_NAME,
                0.0f,
                upgradeItemY,
                9,
                getUpgradeItemClickListener(0),
                assetManager);
        upgradeItemDisplayAntiGravity.addToStage(guiStage);
        
        upgradeItemY -= 80.0f;
        upgradeItemDisplayRocket = new UpgradeItemDisplay(
                ResourceNames.GUI_UPGRADE_UPGRADE_ITEM_ROCKET_IMAGE_NAME,
                0.0f,
                upgradeItemY,
                9,
                getUpgradeItemClickListener(1),
                assetManager);
        upgradeItemDisplayRocket.addToStage(guiStage);
        
        upgradeItemY -= 80.0f;
        upgradeItemDisplayLife = new UpgradeItemDisplay(
                ResourceNames.GUI_UPGRADE_UPGRADE_ITEM_LIFE_IMAGE_NAME,
                0.0f,
                upgradeItemY,
                3,
                getUpgradeItemClickListener(2),
                assetManager);
        upgradeItemDisplayLife.addToStage(guiStage);
        
        upgradeTextLabelTop = upgradeItemY - 20.0f;
        
        upgradeTextLabel = new Label("", guiSkin, "font24");
        upgradeTextLabel.setAlignment(Align.left);
        guiStage.addActor(upgradeTextLabel);
        
        upgradeButton = new TextButton("UPGRADE", guiSkin, "font40");
        upgradeButton.setBounds(75.0f, 40.0f, 300.0f, 80.0f);
        upgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradeSelectedItem();
            }
        });
        guiStage.addActor(upgradeButton);
        
        selectUpgradeItem(0);
    }
    
    @Override
    public void show(ObjectMap<String, Object> changeParams) {
        super.show(changeParams);
        refreshFromCareerData();
    }
    
    @Override
    protected void updateImpl(float delta) {
        updateUpgradeText();
        upgradeButton.setDisabled(!canUpgrade());
    }
    
    @Override
    public void renderImpl() {
        batch.setProjectionMatrix(cameraData.getGameAreaMatrix());
        batch.begin();
        background.render(batch);
        batch.end();
    }
    
    private void refreshFromCareerData() {
        CareerData careerData = gameData.getCareerData();
        
        upgradeItemDisplayAntiGravity.setUpgradeLevel(careerData.getAntiGravityLevel());
        upgradeItemDisplayRocket.setUpgradeLevel(careerData.getRocketLevel());
        upgradeItemDisplayLife.setUpgradeLevel(careerData.getInitialLivesNextGame());
    }
    
    private void selectUpgradeItem(int index) {
        upgradeItemDisplayAntiGravity.setSelected(index == 0);
        upgradeItemDisplayRocket.setSelected(index == 1);
        upgradeItemDisplayLife.setSelected(index == 2);
    }
    
    private void updateUpgradeText() {
        upgradeTextLabel.setText(getUpgradeText());
        
        TextBounds textBounds = upgradeTextLabel.getStyle().font.getMultiLineBounds(upgradeTextLabel.getText());
        upgradeTextLabel.setBounds(20.0f, upgradeTextLabelTop - textBounds.height, textBounds.width, textBounds.height);
    }
    
    private String getUpgradeText() {
        String text = "";
        
        int coins = gameData.getCareerData().getCoins();
        
        int selectedUpgradeItemIndex = getUpgradeItemIndex();
        
        if (selectedUpgradeItemIndex == 0) {
            int currentUpgradeLevel = upgradeItemDisplayAntiGravity.getCurrentUpgradeLevel();
            int totalUpgradeLevels = upgradeItemDisplayAntiGravity.getTotalUpgradeLevels();
            float currentLevelDuration = UpgradesUtil.getAntiGravityDuration(currentUpgradeLevel);
            float nextLevelDuration = UpgradesUtil.getAntiGravityDuration(currentUpgradeLevel + 1);
            int upgradeCost = UpgradesUtil.getAntiGravityNextUpgradeCost(currentUpgradeLevel);
            
            text += "Increase anti-gravity duration.\n";
            text += "Permanent upgrade.\n\n";
            text += String.format(Locale.US, "Current: %.1fs\n", currentLevelDuration);
            
            text += "Next: ";
            String nextLevelDurationString = (currentUpgradeLevel < totalUpgradeLevels) ? String.format(Locale.US, "%.1fs", nextLevelDuration) : "-";
            text += nextLevelDurationString + "\n";
            
            text += "Upgrade cost: ";
            String nextLevelUpgradeCostString = (currentUpgradeLevel < totalUpgradeLevels) ? String.valueOf(upgradeCost) : "-";
            text += nextLevelUpgradeCostString + "\n\n";
            
        } else if (selectedUpgradeItemIndex == 1) {
            int currentUpgradeLevel = upgradeItemDisplayRocket.getCurrentUpgradeLevel();
            int totalUpgradeLevels = upgradeItemDisplayRocket.getTotalUpgradeLevels();
            float currentLevelDuration = UpgradesUtil.getRocketDuration(currentUpgradeLevel);
            float nextLevelDuration = UpgradesUtil.getRocketDuration(currentUpgradeLevel + 1);
            int upgradeCost = UpgradesUtil.getRocketNextUpgradeCost(currentUpgradeLevel);
            
            text += "Increase rocket duration.\n";
            text += "Permanent upgrade.\n\n";
            text += String.format(Locale.US, "Current: %.1fs\n", currentLevelDuration);
            
            text += "Next: ";
            String nextLevelDurationString = (currentUpgradeLevel < totalUpgradeLevels) ? String.format(Locale.US, "%.1fs", nextLevelDuration) : "-";
            text += nextLevelDurationString + "\n";
            
            text += "Upgrade cost: ";
            String nextLevelUpgradeCostString = (currentUpgradeLevel < totalUpgradeLevels) ? String.valueOf(upgradeCost) : "-";
            text += nextLevelUpgradeCostString + "\n\n";
            
        } else if (selectedUpgradeItemIndex == 2) {
            int currentUpgradeLevel = upgradeItemDisplayLife.getCurrentUpgradeLevel();
            int totalUpgradeLevels = upgradeItemDisplayLife.getTotalUpgradeLevels();
            int upgradeCost = UpgradesUtil.getNextLifeUpgradeCost(currentUpgradeLevel);
            
            text += "Increase initial life count.\n";
            text += "Applies ONLY to the next game.\n\n";
            text += "Current: " + currentUpgradeLevel + "\n";
            
            text += "Next: ";
            String nextLevelInitialLivesString = (currentUpgradeLevel < totalUpgradeLevels) ? String.valueOf(currentUpgradeLevel + 1) : "-";
            text += nextLevelInitialLivesString + "\n";
            
            text += "Upgrade cost: ";
            String nextLevelUpgradeCostString = (currentUpgradeLevel < totalUpgradeLevels) ? String.valueOf(upgradeCost) : "-";
            text += nextLevelUpgradeCostString + "\n\n";
        }
        
        text += "Your coins: " + coins;
        
        return text;
    }
    
    private void upgradeSelectedItem() {
        if (!canUpgrade()) {
            return;
        }
        
        CareerData careerData = gameData.getCareerData();
        
        int coins = careerData.getCoins();
        int selectedUpgradeItemIndex = getUpgradeItemIndex();
        
        if (selectedUpgradeItemIndex == 0) {
            int currentUpgradeLevel = upgradeItemDisplayAntiGravity.getCurrentUpgradeLevel();
            int upgradeCost = UpgradesUtil.getAntiGravityNextUpgradeCost(currentUpgradeLevel);
            
            careerData.setCoins(coins - upgradeCost);
            careerData.setAntiGravityLevel(currentUpgradeLevel + 1);
            
            upgradeItemDisplayAntiGravity.setUpgradeLevel(currentUpgradeLevel + 1);
            
        } else if (selectedUpgradeItemIndex == 1) {
            int currentUpgradeLevel = upgradeItemDisplayRocket.getCurrentUpgradeLevel();
            int upgradeCost = UpgradesUtil.getRocketNextUpgradeCost(currentUpgradeLevel);
            
            careerData.setCoins(coins - upgradeCost);
            careerData.setRocketLevel(currentUpgradeLevel + 1);
            
            upgradeItemDisplayRocket.setUpgradeLevel(currentUpgradeLevel + 1);
            
        } else if (selectedUpgradeItemIndex == 2) {
            int currentUpgradeLevel = upgradeItemDisplayLife.getCurrentUpgradeLevel();
            int upgradeCost = UpgradesUtil.getNextLifeUpgradeCost(currentUpgradeLevel);
            
            careerData.setCoins(coins - upgradeCost);
            careerData.setInitialLivesNextGame(currentUpgradeLevel + 1);
            
            upgradeItemDisplayLife.setUpgradeLevel(currentUpgradeLevel + 1);
        }
    }
    
    private boolean canUpgrade() {
        int coins = gameData.getCareerData().getCoins();
        
        int currentUpgradeLevel;
        int totalUpgradeLevels;
        int upgradeCost;
        
        int selectedUpgradeItemIndex = getUpgradeItemIndex();
        if (selectedUpgradeItemIndex == 0) {
            currentUpgradeLevel = upgradeItemDisplayAntiGravity.getCurrentUpgradeLevel();
            totalUpgradeLevels = upgradeItemDisplayAntiGravity.getTotalUpgradeLevels();
            upgradeCost = UpgradesUtil.getAntiGravityNextUpgradeCost(currentUpgradeLevel);
        } else if (selectedUpgradeItemIndex == 1) {
            currentUpgradeLevel = upgradeItemDisplayRocket.getCurrentUpgradeLevel();
            totalUpgradeLevels = upgradeItemDisplayRocket.getTotalUpgradeLevels();
            upgradeCost = UpgradesUtil.getRocketNextUpgradeCost(currentUpgradeLevel);
        } else {
            currentUpgradeLevel = upgradeItemDisplayLife.getCurrentUpgradeLevel();
            totalUpgradeLevels = upgradeItemDisplayLife.getTotalUpgradeLevels();
            upgradeCost = UpgradesUtil.getNextLifeUpgradeCost(currentUpgradeLevel);
        }
        
        return currentUpgradeLevel < totalUpgradeLevels && upgradeCost <= coins;
    }
    
    private int getUpgradeItemIndex() {
        if (upgradeItemDisplayAntiGravity.isSelected()) {
            return 0;
        } else if (upgradeItemDisplayRocket.isSelected()) {
            return 1;
        } else if (upgradeItemDisplayLife.isSelected()) {
            return 2;
        } else {
            return 0;
        }
    }
    
    private static InputListener getStageInputListener(final UpgradesScreen screen) {
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
    
    private ClickListener getUpgradeItemClickListener(final int index) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectUpgradeItem(index);
            }
        };
    }
}