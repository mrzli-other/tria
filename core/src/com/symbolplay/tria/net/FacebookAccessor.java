package com.symbolplay.tria.net;

import com.badlogic.gdx.utils.Array;
import com.symbolplay.gamelibrary.game.GameContainerUpdateable;

public final class FacebookAccessor implements GameContainerUpdateable {
    
    private static final float LONIN_STATE_REFRESH_INTERVAL = 5.0f;
    
    //private static final int DEFAULT_IMAGE_BUFFER_SIZE = 65536;
    
    private final FacebookInterface facebookInterface;
    
    private boolean isLoggedInState;
    private String loginStatusMessage;
    private FacebookProfileData profileData;
    //private byte[] profileImageData;
    private Array<FacebookFriendData> friendsData;
    private String dataErrorMessage;
    
    private boolean isLoginStateChangeReceived;
    private boolean isDataReceived;
    
    private float loginStateRefreshElapsed;
    
    private Array<FacebookLoginStateListener> loginStateListeners;
    private Array<FacebookDataListener> dataListeners;
    
    private final Object loginStateLock = new Object();
    private final Object dataLock = new Object();
    
    public FacebookAccessor(FacebookInterface facebookInterface) {
        this.facebookInterface = facebookInterface;
        facebookInterface.setFacebookListener(getFacebookListener());
        
        clearData();
        
        loginStateListeners = new Array<FacebookLoginStateListener>(true, 10);
        dataListeners = new Array<FacebookDataListener>(true, 10);
        
        refreshLogin();
    }
    
    @Override
    public void update(float delta) {
        loginStateRefreshElapsed += delta;
        if (loginStateRefreshElapsed >= LONIN_STATE_REFRESH_INTERVAL) {
            refreshLogin();
            loginStateRefreshElapsed = 0.0f;
        }
        
        if (isLoginStateChangeReceived) {
            handleLoginStateChange();
        }
        
        if (isDataReceived) {
            handleDataReceived();
        }
        
        if (dataErrorMessage != null) {
            handleDataErrorMessage();
        }
    }
    
    private void refreshLogin() {
        boolean isLogingedIn = isLoggedIn();
        if (!isLogingedIn && isLoggedInState) {
            changeLoginState(false, "There is a problem with login.");
        } else if (isLogingedIn && !isLoggedInState) {
            changeLoginState(true, "");
        }
    }
    
    private void clearData() {
        synchronized (loginStateLock) {
            isLoggedInState = false;
            loginStatusMessage = "";
            isLoginStateChangeReceived = false;
        }
        
        synchronized (dataLock) {
            profileData = null;
            //profileImageData = null;
            friendsData = null;
            dataErrorMessage = null;
            isDataReceived = false;
        }
    }
    
    public void login() {
        facebookInterface.login();
    }
    
    public void logout() {
        facebookInterface.logout();
    }
    
    public boolean isLoggedIn() {
        return facebookInterface.isLoggedIn();
    }
    
    public void requestProfileAndFriends() {
        if (!isLoggedInState) {
            return;
        }
        
        if (tryUseCachedData()) {
            return;
        }
        
        facebookInterface.requestProfileAndFriends();
    }
    
//    private void requestImage(String imageUrl) {
//        final HttpRequestTask httpRequestTask = HttpRequestTask.createGetRequestTask("GetFacebookOptionsProfileImageTask", imageUrl, true, DEFAULT_IMAGE_BUFFER_SIZE);
//        httpRequestTask.setFinishedListener(new HttpRequestTaskFinishedListener() {
//            @Override
//            public void httpRequestTaskFinished() {
//                byte[] imageData = (byte[]) httpRequestTask.getData();
//                setProfileImageData(imageData);
//            }
//        });
//        httpRequestTask.start();
//    }
    
    private boolean tryUseCachedData() {
        if (profileData != null && friendsData != null) {
            synchronized (dataLock) {
                if (profileData != null && friendsData != null) {
                    isDataReceived = true;
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void changeLoginState(boolean isLoggedInState, String loginStatusMessage) {
        clearData();
        synchronized (loginStateLock) {
            this.isLoggedInState = isLoggedInState;
            this.loginStatusMessage = loginStatusMessage;
            isLoginStateChangeReceived = true;
        }
    }
    
    private void setProfileData(FacebookProfileData profileData) {
        synchronized (dataLock) {
            this.profileData = profileData;
//            if (profileData != null && profileData.getPictureLink() != null) {
//                requestImage(profileData.getPictureLink());
//            }
            isDataReceived = true;
        }
    }
    
//    private void setProfileImageData(byte[] imageData) {
//        synchronized (profileLock) {
//            this.profileImageData = imageData;
//            isProfileDataReceived = true;
//        }
//    }
    
    private void setFriendsData(Array<FacebookFriendData> friendsData) {
        synchronized (dataLock) {
            this.friendsData = friendsData;
            isDataReceived = true;
        }
    }
    
    private void setDataErrorMessage(String message) {
        synchronized (dataLock) {
            dataErrorMessage = message;
        }
    }
    
    private void handleLoginStateChange() {
        synchronized (loginStateLock) {
            if (!isLoginStateChangeReceived) {
                return;
            }
            
            notifyLoginStateChanged(isLoggedInState, loginStatusMessage);
            
            isLoginStateChangeReceived = false;
        }
    }
    
    private void handleDataReceived() {
        synchronized (dataLock) {
            if (!isDataReceived) {
                return;
            }
            
            Array<FacebookFriendData> friendsDataCopy = friendsData != null ? new Array<FacebookFriendData>(friendsData) : null;
            notifyDataReceived(profileData, friendsDataCopy);
            
            isDataReceived = false;
        }
    }
    
    private void handleDataErrorMessage() {
        synchronized (dataLock) {
            if (dataErrorMessage == null) {
                return;
            }
            
            notifyDataError(dataErrorMessage);
            
            dataErrorMessage = null;
        }
    }
    
    public String getUserId() {
        synchronized (dataLock) {
            return profileData != null ? profileData.getId() : null;
        }
    }
    
    public void addLoginStateListener(FacebookLoginStateListener loginStateListener) {
        loginStateListeners.add(loginStateListener);
    }
    
    public void removeLoginStateListener(FacebookLoginStateListener loginStateListener) {
        loginStateListeners.removeValue(loginStateListener, true);
    }
    
    public void clearLoginStateListener() {
        loginStateListeners.clear();
    }
    
    private void notifyLoginStateChanged(boolean isLoggedInState, String loginStatusMessage) {
        for (FacebookLoginStateListener loginStateListener : loginStateListeners) {
            loginStateListener.loginStateChanged(isLoggedInState, loginStatusMessage);
        }
    }
    
    public void addDataListener(FacebookDataListener dataListener) {
        dataListeners.add(dataListener);
    }
    
    public void removeDataListener(FacebookDataListener dataListener) {
        dataListeners.removeValue(dataListener, true);
    }
    
    public void clearDataListeners() {
        dataListeners.clear();
    }
    
    private void notifyDataReceived(FacebookProfileData profileData, Array<FacebookFriendData> friendsData) {
        for (FacebookDataListener dataListener : dataListeners) {
            dataListener.dataReceived(profileData, friendsData);
        }
    }
    
    private void notifyDataError(String message) {
        for (FacebookDataListener dataListener : dataListeners) {
            dataListener.dataError(message);
        }
    }
    
    private FacebookListener getFacebookListener() {
        return new FacebookListener() {
            
            @Override
            public void loginSuccess() {
                changeLoginState(true, "");
            }
            
            @Override
            public void loginFail(String message) {
                changeLoginState(false, "Failed to log in.");
            }
            
            @Override
            public void logoutSuccess() {
                changeLoginState(false, "");
            }
            
            @Override
            public void logoutFail(String reason) {
                //loginStatusLabel.setText(reason);
            }
            
            @Override
            public void profileReceived(FacebookProfileData profileData) {
                setProfileData(profileData);
            }
            
            @Override
            public void profileRequestError() {
                setDataErrorMessage("Failed to receive Facebook profile data.");
            }
            
            @Override
            public void friendsReceived(Array<FacebookFriendData> friendsData) {
                setFriendsData(friendsData);
            }
            
            @Override
            public void friendsRequestError() {
                setDataErrorMessage("Failed to receive Facebook friends data.");
            }
        };
    }
}
