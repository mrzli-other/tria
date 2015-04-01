package com.symbolplay.tria.net;

import com.badlogic.gdx.utils.Array;

public interface FacebookListener {
    
    void loginSuccess();
    
    void loginFail(String message);
    
    void logoutSuccess();
    
    void logoutFail(String message);
    
    void profileReceived(FacebookProfileData profileData);
    
    void profileRequestError();
    
    void friendsReceived(Array<FacebookFriendData> friendsData);
    
    void friendsRequestError();
}
