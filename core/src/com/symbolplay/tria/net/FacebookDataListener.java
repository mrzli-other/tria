package com.symbolplay.tria.net;

import com.badlogic.gdx.utils.Array;

public interface FacebookDataListener {
    void dataReceived(FacebookProfileData profileData, Array<FacebookFriendData> friendsData);
    
    void dataError(String message);
}
