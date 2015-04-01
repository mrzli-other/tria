package com.symbolplay.tria.net;

import com.badlogic.gdx.utils.Array;

public interface NewsListener {
    
    void lastNewsIndexReceived(int lastNewsIndex);
    
    void newsDataReceived(Array<NewsAndImageData> newsAndImageDataList);
    
    void error(String message);
}
