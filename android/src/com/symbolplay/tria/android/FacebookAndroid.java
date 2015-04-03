package com.symbolplay.tria.android;

import com.symbolplay.gamelibrary.util.Logger;
import com.symbolplay.tria.net.FacebookInterface;
import com.symbolplay.tria.net.FacebookListener;

final class FacebookAndroid implements FacebookInterface {
    
    private static final String MESSAGE = "Facebook no longer supported.";
    
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
