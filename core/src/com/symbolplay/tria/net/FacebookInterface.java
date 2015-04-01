package com.symbolplay.tria.net;

public interface FacebookInterface {
    
    void setFacebookListener(FacebookListener listener);
    
    void login();
    
    void logout();
    
    boolean isLoggedIn();
    
    void requestProfileAndFriends();
}
