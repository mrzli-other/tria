package com.symbolplay.tria.android;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.symbolplay.gamelibrary.util.Logger;
import com.symbolplay.tria.net.FacebookInterface;
import com.symbolplay.tria.net.FacebookListener;

final class FacebookAndroid implements FacebookInterface {
    
    private static final String APP_ID = "268015413405992";
    //private static final String APP_NAMESPACE = "symboljump";
    
    //private final Activity activity;
    private FacebookListenerWrapper facebookListenerWrapper;
    
    private UiLifecycleHelper uiHelper;
    private FacebookLoginHelper facebookLoginHelper;
    
    private boolean isResumed;
    
    private final Session.StatusCallback sessionStatusCallBack;
    
    public FacebookAndroid(Activity activity, Bundle savedInstanceState) {
        //this.activity = activity;
        facebookListenerWrapper = new FacebookListenerWrapper();
        
        sessionStatusCallBack = getSessionStatusCallback();
        
        uiHelper = new UiLifecycleHelper(activity, sessionStatusCallBack);
        uiHelper.onCreate(savedInstanceState);
        facebookLoginHelper = new FacebookLoginHelper(APP_ID, activity, facebookListenerWrapper);
        
        isResumed = false;
    }
    
    @Override
    public void setFacebookListener(FacebookListener facebookListener) {
        facebookListenerWrapper.setFacebookListener(facebookListener);
    }
    
    @Override
    public void login() {
        facebookLoginHelper.login();
    }
    
    @Override
    public void logout() {
        facebookLoginHelper.logout();
    }
    
    @Override
    public boolean isLoggedIn() {
        Session session = Session.getActiveSession();
        return session != null && session.isOpened();
    }
    
    @Override
    public void requestProfileAndFriends() {
        Session session = Session.getActiveSession();
        if (session == null || !session.isOpened()) {
            Logger.error("Cannot request profile and friends, not logged in.");
            return;
        }
        
        Request profileRequest = Request.newMeRequest(session, new GraphUserCallback() {
            
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                    facebookListenerWrapper.notifyProfileReceived(user);
                } else {
                    facebookListenerWrapper.notifyProfileRequestError();
                }
            }
        });
        
        Request friendsRequest = Request.newMyFriendsRequest(session, new GraphUserListCallback() {
            
            @Override
            public void onCompleted(List<GraphUser> users, Response response) {
                if (users != null) {
                    facebookListenerWrapper.notifyFriendsReceived(users);
                } else {
                    facebookListenerWrapper.notifyProfileRequestError();
                }
            }
        });
        
        final RequestBatch requestBatch = new RequestBatch();
        requestBatch.add(profileRequest);
        requestBatch.add(friendsRequest);
        
        // must be executed on main thread or there will be an exception
        executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                requestBatch.executeAsync();
            }
        });
    }
    
    public void onResume() {
        uiHelper.onResume();
        
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        }
        
        isResumed = true;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }
    
    public void onSaveInstanceState(Bundle outState) {
        uiHelper.onSaveInstanceState(outState);
    }
    
    public void onPause() {
        uiHelper.onPause();
        isResumed = false;
    }
    
    public void onDestroy() {
        uiHelper.onDestroy();
    }
    
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (isResumed) {
            if (state.isOpened()) {
                facebookListenerWrapper.notifyLoginSuccess();
            } else if (state.isClosed()) {
                facebookListenerWrapper.notifyLogoutSuccess();
            }
        }
    }
    
    private static void executeOnMainThread(Runnable runnable) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(runnable);
    }
    
    private Session.StatusCallback getSessionStatusCallback() {
        return new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChange(session, state, exception);
            }
        };
    }
}
