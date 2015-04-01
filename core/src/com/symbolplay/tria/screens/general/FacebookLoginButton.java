package com.symbolplay.tria.screens.general;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.gamelibrary.controls.CustomImageButton;
import com.symbolplay.gamelibrary.controls.CustomImageButtonStyleData;
import com.symbolplay.tria.net.FacebookAccessor;
import com.symbolplay.tria.net.FacebookLoginStateListener;
import com.symbolplay.tria.resources.ResourceNames;

public abstract class FacebookLoginButton extends CustomImageButton {
    
    private static final float DISABLE_DURATION = 0.5f;
    
    private final FacebookAccessor facebookAccessor;
    
    private boolean isActive;
    
    private boolean isLoggedIn;
    
    private float disabledElapsed;
    
    public FacebookLoginButton(float positionX, float positionY, TextureAtlas atlas, FacebookAccessor facebookAccessor) {
        super(positionX, positionY, atlas, getStylesData());
        
        this.facebookAccessor = facebookAccessor;
        facebookAccessor.addLoginStateListener(new FacebookLoginStateListener() {
            
            @Override
            public void loginStateChanged(boolean isLoggedInState, String loginStatusMessage) {
                setLoggedInState(isLoggedInState);
            }
        });
        
        setActive(false);
        
        disabledElapsed = DISABLE_DURATION;
        
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isEnabled()) {
                    return;
                }
                
                setEnabled(false);
                disabledElapsed = 0.0f;
                if (isLoggedIn) {
                    startLogoutProcedure();
                } else {
                    FacebookLoginButton.this.facebookAccessor.login();
                }
            }
        });
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);
        
        if (!isActive) {
            return;
        }
        
        if (!isEnabled()) {
            disabledElapsed += delta;
            if (disabledElapsed >= DISABLE_DURATION) {
                setEnabled(true);
            }
        }
    }
    
    private static Array<CustomImageButtonStyleData> getStylesData() {
        Array<CustomImageButtonStyleData> stylesData = new Array<CustomImageButtonStyleData>(true, 2);
        CustomImageButtonStyleData styleData;
        
        styleData = new CustomImageButtonStyleData(
                0,
                ResourceNames.GUI_MAIN_MENU_FACEBOOK_LOGIN_BUTTON_IMAGE_NAME,
                ResourceNames.GUI_MAIN_MENU_FACEBOOK_LOGIN_BUTTON_IMAGE_NAME,
                ResourceNames.GUI_MAIN_MENU_FACEBOOK_LOGIN_BUTTON_DISABLED_IMAGE_NAME);
        stylesData.add(styleData);
        
        styleData = new CustomImageButtonStyleData(
                1,
                ResourceNames.GUI_MAIN_MENU_FACEBOOK_LOGOUT_BUTTON_IMAGE_NAME,
                ResourceNames.GUI_MAIN_MENU_FACEBOOK_LOGOUT_BUTTON_IMAGE_NAME,
                ResourceNames.GUI_MAIN_MENU_FACEBOOK_LOGOUT_BUTTON_DISABLED_IMAGE_NAME);
        stylesData.add(styleData);
        
        return stylesData;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
        setLoggedInState(facebookAccessor.isLoggedIn());
    }
    
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    
    public abstract void startLogoutProcedure();
    
    public void logout() {
        FacebookLoginButton.this.facebookAccessor.logout();
    }
    
    private void setLoggedInState(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
        setState(isLoggedIn ? 1 : 0);
    }
}
