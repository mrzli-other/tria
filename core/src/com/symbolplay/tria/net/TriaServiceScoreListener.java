package com.symbolplay.tria.net;

import com.badlogic.gdx.utils.ObjectMap;

public interface TriaServiceScoreListener {
    
    void scoresReceived(ObjectMap<String, TriaServiceScoreData> scoreDataMap);
    
    void error(String message);
}
