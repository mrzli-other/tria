package com.symbolplay.tria.desktop;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;

import com.symbolplay.gamelibrary.util.Logger;
import com.symbolplay.tria.PlatformSpecificInterface;

final class PlatformSpecificDesktop implements PlatformSpecificInterface {
    
    @Override
    public void rate() {
        Logger.info("Rating is not available for desktop plaform.");
    }
    
    @Override
    public void goToAddress(String androidAddress, String genericAddress) {
        if (StringUtils.isEmpty(genericAddress) || !Desktop.isDesktopSupported())
        {
            return;
        }
        
        try {
            Desktop.getDesktop().browse(new URI(genericAddress));
        } catch (IOException e) {
            Logger.error(e.getMessage());
            e.printStackTrace();
        } catch (URISyntaxException e) {
            Logger.error(e.getMessage());
        }
    }
}
