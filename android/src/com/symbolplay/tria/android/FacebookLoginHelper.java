package com.symbolplay.tria.android;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.Session.AuthorizationRequest;
import com.symbolplay.gamelibrary.util.Logger;

import android.app.Activity;

// some code based on SessionManager from facebook-simple-api
final class FacebookLoginHelper {
    
    private final String facebookAppId;
    
    private final Activity activity;
    private final FacebookListenerWrapper facebookListenerWrapper;
    
    public FacebookLoginHelper(String facebookAppId, Activity activity, FacebookListenerWrapper facebookListenerWrapper) {
        this.facebookAppId = facebookAppId;
        this.activity = activity;
        this.facebookListenerWrapper = facebookListenerWrapper;
    }
    
    public void login() {
        if (isLogin(true)) {
            facebookListenerWrapper.notifyLoginSuccess();
            return;
        }
        
        Session session = getOrCreateActiveSession();
        if (hasPendingRequest(session)) {
            Logger.info("Previous login request is being processed, skipping login.");
            return;
        }
        
        if (!session.isOpened()) {
            openReadSession(session);
        } else {
            facebookListenerWrapper.notifyLoginSuccess();
        }
    }
    
    private boolean isLogin(boolean isTryReopen) {
        Session session = getActiveSession();
        if (session == null) {
            session = createSession();
            Session.setActiveSession(session);
        }
        
        if (session.isOpened()) {
            return true;
        }
        
        if (isTryReopen && canReopenSession(session)) {
            reopenSession();
            return true;
        }
        
        return false;
    }
    
    private boolean hasPendingRequest(Session session) {
        try {
            Field f = session.getClass().getDeclaredField("pendingAuthorizationRequest");
            f.setAccessible(true);
            AuthorizationRequest authorizationRequest = (AuthorizationRequest) f.get(session);
            if (authorizationRequest != null) {
                return true;
            }
        } catch (Exception e) {
            // do nothing
        }
        
        return false;
    }
    
    private boolean canReopenSession(Session session) {
        return SessionState.CREATED_TOKEN_LOADED.equals(session.getState());
    }
    
    private void reopenSession() {
        Session session = getActiveSession();
        if (session != null && session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
            openReadSession(session);
        }
    }
    
    private void openReadSession(Session session) {
        Session.OpenRequest request = new Session.OpenRequest(activity);
        
        List<String> permissions = new ArrayList<String>(2);
        permissions.add("public_profile");
        permissions.add("user_friends");
        
        request.setDefaultAudience(SessionDefaultAudience.NONE);
        request.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
        request.setPermissions(permissions);
        session.openForRead(request);
    }
    
    private Session getOrCreateActiveSession() {
        if (getActiveSession() == null || getActiveSession().isClosed()) {
            Session session = createSession();
            Session.setActiveSession(session);
        }
        return getActiveSession();
    }
    
    private Session createSession() {
        return new Session.Builder(activity.getApplicationContext()).setApplicationId(facebookAppId).build();
    }
    
    private Session getActiveSession() {
        return Session.getActiveSession();
    }
    
    public void logout() {
        Session session = getActiveSession();
        if (session == null || session.isClosed()) {
            facebookListenerWrapper.notifyLogoutSuccess();
            return;
        }
        
        session.closeAndClearTokenInformation();
    }
}
