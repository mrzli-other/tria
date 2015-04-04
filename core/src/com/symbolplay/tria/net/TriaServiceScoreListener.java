package com.symbolplay.tria.net;

import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.persistence.userdata.HighScoreData;

public interface TriaServiceScoreListener {
    
    void scoresReceived(Array<HighScoreData> scoresData);
    
    void error(String message);
}
