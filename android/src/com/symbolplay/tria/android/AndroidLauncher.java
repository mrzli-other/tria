package com.symbolplay.tria.android;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.symbolplay.tria.GameContainer;

public class AndroidLauncher extends AndroidApplication {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = true;
        config.useCompass = false;
        
        PlatformSpecificAndroid platformSpecificAndroid = new PlatformSpecificAndroid(this);
        
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // do nothing
        }
        
        int appVersion = packageInfo != null ? packageInfo.versionCode : 0;
        
        initialize(new GameContainer(platformSpecificAndroid, appVersion), config);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
//    public void printHashKey() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.symbolplay.tria.android", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.i("TEMPTAGHASH KEY:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (NameNotFoundException e) {
//        } catch (NoSuchAlgorithmException e) {
//        }
//    }
}
