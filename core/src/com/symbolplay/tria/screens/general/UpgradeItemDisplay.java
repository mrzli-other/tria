package com.symbolplay.tria.screens.general;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.symbolplay.tria.resources.ResourceNames;

public final class UpgradeItemDisplay {
    
    private final Group group;
    private final ImageButton button;
    private final Image upgradeItemSelectionImage;
    private final Image upgradeItemImage;
    private final Image[] upgradeLevelEmptyImages;
    private final Image[] upgradeLevelFullImages;
    
    private final int totalUpgradeLevels;
    private int currentUpgradeLevel;
    private boolean isSelected;
    
    public UpgradeItemDisplay(String upgradeItemImageName, float x, float y, int totalUpgradeLevels, ClickListener clickListener, AssetManager assetManager) {
        
        this.totalUpgradeLevels = totalUpgradeLevels;
        isSelected = false;
        
        TextureAtlas atlas = assetManager.get(ResourceNames.GRAPHICS_GUI_ATLAS);
        
        group = new Group();
        
        button = new ImageButton((Drawable) null);
        button.setBounds(x, y, 450.0f, 80.0f);
        button.addListener(clickListener);
        group.addActor(button);
        
        TextureRegion upgradeItemSelectionImageRegion = atlas.findRegion(ResourceNames.GUI_UPGRADE_UPGRADE_ITEM_SELECTION_IMAGE_NAME);
        upgradeItemSelectionImage = new Image(upgradeItemSelectionImageRegion);
        upgradeItemSelectionImage.setPosition(x, y);
        upgradeItemSelectionImage.setVisible(isSelected);
        upgradeItemSelectionImage.setTouchable(Touchable.disabled);
        group.addActor(upgradeItemSelectionImage);
        
        TextureRegion upgradeItemImageRegion = atlas.findRegion(upgradeItemImageName);
        upgradeItemImage = new Image(upgradeItemImageRegion);
        upgradeItemImage.setPosition(x + (80.0f - upgradeItemImage.getWidth()) / 2.0f, y + (80.0f - upgradeItemImage.getHeight()) / 2.0f);
        upgradeItemImage.setTouchable(Touchable.disabled);
        group.addActor(upgradeItemImage);
        
        TextureRegion upgradeLevelEmptyImageRegion = atlas.findRegion(ResourceNames.GUI_UPGRADE_UPGRADE_LEVEL_EMPTY_IMAGE_NAME);
        TextureRegion upgradeLevelFullImageRegion = atlas.findRegion(ResourceNames.GUI_UPGRADE_UPGRADE_LEVEL_FULL_IMAGE_NAME);
        
        upgradeLevelEmptyImages = new Image[totalUpgradeLevels];
        upgradeLevelFullImages = new Image[totalUpgradeLevels];
        
        float upgradeLevelImageX = x + 90.0f;
        float upgradeLevelImageY = y + 10.0f;
        
        for (int i = 0; i < totalUpgradeLevels; i++) {
            upgradeLevelEmptyImages[i] = createUpgradeLevelImage(upgradeLevelEmptyImageRegion, upgradeLevelImageX, upgradeLevelImageY);
            upgradeLevelFullImages[i] = createUpgradeLevelImage(upgradeLevelFullImageRegion, upgradeLevelImageX, upgradeLevelImageY);
            
            upgradeLevelImageX += 40.0f;
        }
        
        setUpgradeLevel(0);
    }
    
    private Image createUpgradeLevelImage(TextureRegion region, float x, float y) {
        Image upgradeLevelImage = new Image(new TextureRegionDrawable(region));
        upgradeLevelImage.setPosition(x, y);
        upgradeLevelImage.setTouchable(Touchable.disabled);
        group.addActor(upgradeLevelImage);
        
        return upgradeLevelImage;
    }
    
    public void addToStage(Stage stage) {
        stage.addActor(group);
    }
    
    public void setUpgradeLevel(int upgradeLevel) {
        currentUpgradeLevel = Math.min(upgradeLevel, totalUpgradeLevels);
        
        for (int i = 0; i < totalUpgradeLevels; i++) {
            if (i < currentUpgradeLevel) {
                upgradeLevelEmptyImages[i].setVisible(false);
                upgradeLevelFullImages[i].setVisible(true);
            } else {
                upgradeLevelEmptyImages[i].setVisible(true);
                upgradeLevelFullImages[i].setVisible(false);
            }
        }
    }
    
    public boolean isSelected() {
        return isSelected;
    }
    
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        upgradeItemSelectionImage.setVisible(isSelected);
    }
    
    public int getTotalUpgradeLevels() {
        return totalUpgradeLevels;
    }
    
    public int getCurrentUpgradeLevel() {
        return currentUpgradeLevel;
    }
}
