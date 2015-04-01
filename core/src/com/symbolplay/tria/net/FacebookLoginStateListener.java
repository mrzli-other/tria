package com.symbolplay.tria.net;

public interface FacebookLoginStateListener {
    
    void loginStateChanged(boolean isLoggedInState, String loginStatusMessage);
}
