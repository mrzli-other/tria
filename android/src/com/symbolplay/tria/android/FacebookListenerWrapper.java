package com.symbolplay.tria.android;

import java.util.List;

import com.badlogic.gdx.utils.Array;
import com.facebook.model.GraphUser;
import com.symbolplay.tria.net.FacebookFriendData;
import com.symbolplay.tria.net.FacebookListener;
import com.symbolplay.tria.net.FacebookProfileData;

final class FacebookListenerWrapper {
    
    private FacebookListener facebookListener;
    
    public FacebookListenerWrapper() {
        facebookListener = null;
    }
    
    public void setFacebookListener(FacebookListener facebookListener) {
        this.facebookListener = facebookListener;
    }
    
    public void notifyLoginSuccess() {
        if (facebookListener != null) {
            facebookListener.loginSuccess();
        }
    }
    
    public void notifyLoginFail(String message) {
        if (facebookListener != null) {
            facebookListener.loginFail(message);
        }
    }
    
    public void notifyLogoutSuccess() {
        if (facebookListener != null) {
            facebookListener.logoutSuccess();
        }
    }
    
    public void notifyLogoutFail(String message) {
        if (facebookListener != null) {
            facebookListener.logoutFail(message);
        }
    }
    
    public void notifyProfileReceived(GraphUser user) {
        if (facebookListener != null && user != null) {
            String id = user.getId();
            String name = user.getName();
            FacebookProfileData profileData = new FacebookProfileData(id, name);
            facebookListener.profileReceived(profileData);
        }
    }
    
    public void notifyProfileRequestError() {
        if (facebookListener != null) {
            facebookListener.profileRequestError();
        }
    }
    
    public void notifyFriendsReceived(List<GraphUser> users) {
        if (facebookListener != null && users != null) {
            Array<FacebookFriendData> friendsData = new Array<FacebookFriendData>(true, users.size());
            for (GraphUser user : users) {
                FacebookFriendData friendData = new FacebookFriendData(user.getId(), user.getName());
                friendsData.add(friendData);
            }
            facebookListener.friendsReceived(friendsData);
        }
    }
    
    public void notifyFriendsRequestError() {
        if (facebookListener != null) {
            facebookListener.friendsRequestError();
        }
    }
}
