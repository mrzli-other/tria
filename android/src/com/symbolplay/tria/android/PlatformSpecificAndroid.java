package com.symbolplay.tria.android;

import org.apache.commons.lang3.StringUtils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.symbolplay.tria.PlatformSpecificInterface;

final class PlatformSpecificAndroid implements PlatformSpecificInterface {
    
    private final Context context;
    
    public PlatformSpecificAndroid(Context context) {
        this.context = context;
    }
    
    @Override
    public void rate() {
        try {
            startActivity("market://details?id=" + context.getPackageName());
        } catch (ActivityNotFoundException e) {
            startActivity("http://play.google.com/store/apps/details?id=" + context.getPackageName());
        }
    }
    
    private void startActivity(String uriString) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivity(intent);
    }
    
    @Override
    public void goToAddress(String androidAddress, String genericAddress) {
        boolean isAndroidOk = !StringUtils.isEmpty(androidAddress);
        if (isAndroidOk) {
            try {
                startActivity(androidAddress);
            } catch (ActivityNotFoundException e) {
                isAndroidOk = false;
            }
        }
        
        if (!isAndroidOk && !StringUtils.isEmpty(genericAddress)) {
            startActivity(genericAddress);
        }
    }
}
