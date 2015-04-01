package com.symbolplay.tria.desktop;

import com.symbolplay.gamelibrary.util.Logger;
import com.symbolplay.tria.net.FacebookInterface;
import com.symbolplay.tria.net.FacebookListener;

final class FacebookDesktop implements FacebookInterface {
    
    private static final String MESSAGE = "Facebook not available for desktop.";
    
    @Override
    public void setFacebookListener(FacebookListener listener) {
        Logger.info(MESSAGE);
    }
    
    @Override
    public void login() {
        Logger.info(MESSAGE);
    }
    
    @Override
    public void logout() {
        Logger.info(MESSAGE);
    }
    
    @Override
    public boolean isLoggedIn() {
        Logger.info(MESSAGE);
        return false;
    }
    
    @Override
    public void requestProfileAndFriends() {
        Logger.info(MESSAGE);
    }
}
